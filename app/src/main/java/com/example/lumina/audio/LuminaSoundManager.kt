package com.example.lumina.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.lumina.data.model.Affirmation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Random
import kotlin.math.sin

data class Soundscape(
    val id: String,
    val name: String,
    val icon: String,
    val description: String
)

val ALL_SOUNDSCAPES = listOf(
    Soundscape("morning-mist", "Morning Mist", "air", "Soft wind and a warm 432Hz pad"),
    Soundscape("singing-bowls", "Singing Bowls", "self_improvement", "Slow, resonant bowl strikes"),
    Soundscape("temple-chimes", "Temple Chimes", "notifications_active", "Sparse pentatonic chimes"),
    Soundscape("pine-forest-rain", "Pine Forest Rain", "forest", "Gentle rain under the canopy"),
    Soundscape("binaural-432", "Binaural 432Hz", "headphones", "8Hz alpha beat — 432Hz / 440Hz")
)

data class ActiveTrack(
    val id: String,
    val title: String,
    val subtitle: String,
    val guide: String,
    val affirmations: List<Affirmation>,
    val deckSlug: String = ""
)

class LuminaSoundManager private constructor(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _currentTrack = MutableStateFlow<ActiveTrack?>(null)
    val currentTrack: StateFlow<ActiveTrack?> = _currentTrack.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _ambientOn = MutableStateFlow(true)
    val ambientOn: StateFlow<Boolean> = _ambientOn.asStateFlow()

    private val _currentSoundscape = MutableStateFlow("morning-mist")
    val currentSoundscape: StateFlow<String> = _currentSoundscape.asStateFlow()

    private val _ambientVolume = MutableStateFlow(0.65f)
    val ambientVolume: StateFlow<Float> = _ambientVolume.asStateFlow()

    private val _voiceVolume = MutableStateFlow(0.95f)
    val voiceVolume: StateFlow<Float> = _voiceVolume.asStateFlow()

    private val _speechRate = MutableStateFlow(0.9f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private val _totalSeconds = MutableStateFlow(60)
    val totalSeconds: StateFlow<Int> = _totalSeconds.asStateFlow()

    private val _isPlayerExpanded = MutableStateFlow(false)
    val isPlayerExpanded: StateFlow<Boolean> = _isPlayerExpanded.asStateFlow()

    private val _sleepTimer = MutableStateFlow<Int?>(null)
    val sleepTimer: StateFlow<Int?> = _sleepTimer.asStateFlow()

    private val mainHandler = Handler(Looper.getMainLooper())
    private val scope = CoroutineScope(Dispatchers.Default)
    private var ambientJob: Job? = null
    private var timerJob: Job? = null
    private var audioTrack: AudioTrack? = null

    init {
        tts = TextToSpeech(context, this)
        startAmbientLoop()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.ENGLISH
            tts?.setSpeechRate(_speechRate.value)
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    mainHandler.post {
                        onCardUtteranceFinished()
                    }
                }
                override fun onError(utteranceId: String?) {
                    mainHandler.post {
                        onCardUtteranceFinished()
                    }
                }
            })
            isTtsReady = true
        }
    }

    fun playTrack(track: ActiveTrack, startIndex: Int = 0) {
        _currentTrack.value = track
        _currentIndex.value = startIndex.coerceIn(0, (track.affirmations.size - 1).coerceAtLeast(0))
        _totalSeconds.value = (track.affirmations.size * 18).coerceAtLeast(60)
        _elapsedSeconds.value = _currentIndex.value * 18
        _isPlaying.value = true
        speakCurrentCard()
        startTimerJob()
    }

    fun togglePlay() {
        if (_isPlaying.value) {
            pause()
        } else {
            resume()
        }
    }

    fun resume() {
        if (_currentTrack.value == null) return
        _isPlaying.value = true
        speakCurrentCard()
        startTimerJob()
    }

    fun pause() {
        _isPlaying.value = false
        tts?.stop()
        timerJob?.cancel()
    }

    fun stop() {
        _isPlaying.value = false
        _currentTrack.value = null
        _isPlayerExpanded.value = false
        tts?.stop()
        timerJob?.cancel()
    }

    fun skip(delta: Int) {
        val track = _currentTrack.value ?: return
        val newIndex = _currentIndex.value + delta
        if (newIndex in track.affirmations.indices) {
            _currentIndex.value = newIndex
            _elapsedSeconds.value = newIndex * 18
            if (_isPlaying.value) {
                speakCurrentCard()
            }
        }
    }

    fun seekTo(index: Int) {
        val track = _currentTrack.value ?: return
        if (index in track.affirmations.indices) {
            _currentIndex.value = index
            _elapsedSeconds.value = index * 18
            if (_isPlaying.value) {
                speakCurrentCard()
            }
        }
    }

    private fun speakCurrentCard() {
        val track = _currentTrack.value ?: return
        val card = track.affirmations.getOrNull(_currentIndex.value) ?: return
        if (isTtsReady) {
            tts?.stop()
            tts?.setSpeechRate(_speechRate.value)
            val params = android.os.Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, _voiceVolume.value)
            }
            tts?.speak(card.text, TextToSpeech.QUEUE_FLUSH, params, "card_${_currentIndex.value}")
        }
    }

    private fun onCardUtteranceFinished() {
        if (!_isPlaying.value) return
        val track = _currentTrack.value ?: return
        if (_currentIndex.value < track.affirmations.size - 1) {
            mainHandler.postDelayed({
                if (_isPlaying.value) {
                    _currentIndex.value += 1
                    speakCurrentCard()
                }
            }, 3000)
        } else {
            _isPlaying.value = false
            timerJob?.cancel()
        }
    }

    fun speakText(text: String) {
        if (isTtsReady) {
            tts?.stop()
            tts?.setSpeechRate(_speechRate.value)
            val params = android.os.Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, _voiceVolume.value)
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "standalone_${System.currentTimeMillis()}")
        }
    }

    fun toggleAmbient() {
        _ambientOn.value = !_ambientOn.value
    }

    fun setSoundscape(id: String) {
        _currentSoundscape.value = id
    }

    fun setAmbientVolume(vol: Float) {
        _ambientVolume.value = vol.coerceIn(0f, 1f)
    }

    fun setVoiceVolume(vol: Float) {
        _voiceVolume.value = vol.coerceIn(0f, 1f)
    }

    fun setSpeechRate(rate: Float) {
        _speechRate.value = rate
        tts?.setSpeechRate(rate)
    }

    fun setPlayerExpanded(open: Boolean) {
        _isPlayerExpanded.value = open
    }

    fun setSleepTimer(minutes: Int?) {
        _sleepTimer.value = minutes
        if (minutes != null) {
            mainHandler.postDelayed({
                stop()
                _ambientOn.value = false
                _sleepTimer.value = null
            }, (minutes * 60 * 1000).toLong())
        }
    }

    private fun startTimerJob() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while (isActive && _isPlaying.value) {
                delay(1000)
                if (_elapsedSeconds.value < _totalSeconds.value) {
                    _elapsedSeconds.value += 1
                }
            }
        }
    }

    private fun startAmbientLoop() {
        ambientJob?.cancel()
        ambientJob = scope.launch(Dispatchers.Default) {
            val sampleRate = 22050
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(sampleRate * 2)

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack = track
            track.play()

            val buffer = ShortArray(1024)
            val random = Random()
            var phaseL = 0.0
            var phaseR = 0.0
            var strikePhase = 0.0
            var strikeDecay = 0.0f
            var time = 0L

            while (isActive) {
                val isAmbientActive = _ambientOn.value
                val vol = if (isAmbientActive) _ambientVolume.value * 0.45f else 0f
                val soundscape = _currentSoundscape.value

                for (i in 0 until buffer.size step 2) {
                    time++
                    var sampleL = 0.0
                    var sampleR = 0.0

                    when (soundscape) {
                        "binaural-432" -> {
                            // 432 Hz Left, 440 Hz Right (8Hz Alpha Wave Entrainment)
                            phaseL += 2.0 * Math.PI * 432.0 / sampleRate
                            phaseR += 2.0 * Math.PI * 440.0 / sampleRate
                            sampleL = sin(phaseL) * 0.4
                            sampleR = sin(phaseR) * 0.4
                        }
                        "morning-mist" -> {
                            // Warm 432 Hz and 216 Hz pad + gentle pink breath
                            phaseL += 2.0 * Math.PI * 216.0 / sampleRate
                            phaseR += 2.0 * Math.PI * 432.0 / sampleRate
                            val noise = (random.nextDouble() - 0.5) * 0.08
                            sampleL = sin(phaseL) * 0.35 + noise
                            sampleR = sin(phaseR) * 0.35 + noise
                        }
                        "singing-bowls" -> {
                            // Resonant harmonics 216Hz, 288Hz, 324Hz
                            phaseL += 2.0 * Math.PI * 288.0 / sampleRate
                            phaseR += 2.0 * Math.PI * 324.0 / sampleRate
                            if (time % (sampleRate * 7) == 0L) {
                                strikeDecay = 1.0f
                            }
                            strikeDecay *= 0.99995f
                            strikePhase += 2.0 * Math.PI * 216.0 / sampleRate
                            val bell = sin(strikePhase) * strikeDecay * 0.5
                            sampleL = sin(phaseL) * 0.2 + bell
                            sampleR = sin(phaseR) * 0.2 + bell
                        }
                        "temple-chimes" -> {
                            // Meditative pentatonic chimes
                            phaseL += 2.0 * Math.PI * 864.0 / sampleRate
                            phaseR += 2.0 * Math.PI * 972.0 / sampleRate
                            val wind = (random.nextDouble() - 0.5) * 0.05
                            sampleL = sin(phaseL) * 0.15 + wind
                            sampleR = sin(phaseR) * 0.15 + wind
                        }
                        else -> { // pine-forest-rain
                            // Calming natural rainfall noise canopy
                            val rain = (random.nextDouble() - 0.5) * 0.25
                            sampleL = rain
                            sampleR = rain
                        }
                    }

                    val finalL = (sampleL * vol * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    val finalR = (sampleR * vol * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    buffer[i] = finalL
                    buffer[i + 1] = finalR
                }

                track.write(buffer, 0, buffer.size)
            }
            track.stop()
            track.release()
        }
    }

    companion object {
        @Volatile
        private var instance: LuminaSoundManager? = null

        fun getInstance(context: Context): LuminaSoundManager {
            return instance ?: synchronized(this) {
                instance ?: LuminaSoundManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
