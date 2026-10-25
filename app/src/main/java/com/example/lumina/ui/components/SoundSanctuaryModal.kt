package com.example.lumina.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumina.audio.ALL_SOUNDSCAPES
import com.example.lumina.audio.LuminaSoundManager
import com.example.lumina.ui.theme.LuminaPrimary
import com.example.lumina.ui.theme.LuminaPrimaryContainer
import com.example.lumina.ui.theme.LuminaPrimaryFixed
import com.example.lumina.ui.theme.LuminaSecondary
import com.example.lumina.ui.theme.LuminaSecondaryContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundSanctuaryModal(
    soundManager: LuminaSoundManager,
    onSaveMantraCard: (String) -> Unit,
    onOpenStudioWithText: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isExpanded by soundManager.isPlayerExpanded.collectAsState()
    val track by soundManager.currentTrack.collectAsState()
    val isPlaying by soundManager.isPlaying.collectAsState()
    val currentIndex by soundManager.currentIndex.collectAsState()
    val ambientOn by soundManager.ambientOn.collectAsState()
    val currentSoundscape by soundManager.currentSoundscape.collectAsState()
    val ambientVolume by soundManager.ambientVolume.collectAsState()
    val voiceVolume by soundManager.voiceVolume.collectAsState()
    val speechRate by soundManager.speechRate.collectAsState()
    val elapsed by soundManager.elapsedSeconds.collectAsState()
    val total by soundManager.totalSeconds.collectAsState()
    val sleepTimer by soundManager.sleepTimer.collectAsState()

    if (!isExpanded || track == null) return

    val currentAffirmation = track?.affirmations?.getOrNull(currentIndex)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { soundManager.setPlayerExpanded(false) },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Header bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(LuminaPrimaryContainer, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LUMINA SOUND SANCTUARY",
                            style = MaterialTheme.typography.labelSmall,
                            color = LuminaPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(LuminaSecondaryContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${track?.affirmations?.size ?: 0} CARDS",
                                style = MaterialTheme.typography.labelSmall,
                                color = LuminaSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Sleep Timer pills
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Sleep timer",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        listOf(5, 15, 30).forEach { mins ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (sleepTimer == mins) LuminaPrimary else MaterialTheme.colorScheme.surfaceContainerLow)
                                    .clickable {
                                        soundManager.setSleepTimer(if (sleepTimer == mins) null else mins)
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${mins}m",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (sleepTimer == mins) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        IconButton(
                            onClick = { soundManager.setPlayerExpanded(false) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close player")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Series Hero Banner / 432Hz Art
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shadowElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        LuminaPrimaryFixed.copy(alpha = 0.45f),
                                        MaterialTheme.colorScheme.surfaceContainerLow,
                                        LuminaSecondaryContainer.copy(alpha = 0.35f)
                                    )
                                )
                            )
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = track?.subtitle?.uppercase() ?: "AUDIO TRANSMISSION",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LuminaSecondaryContainer)
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = LuminaSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Alpha Waves",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LuminaSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Glowing Orb Graphic
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .shadow(12.dp, CircleShape)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(LuminaPrimaryContainer, LuminaPrimary)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.LightMode,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(34.dp)
                                )
                                Text(
                                    text = "432 HERTZ",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = track?.title ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Guided by ${track?.guide ?: "Mindfulness Guide"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Current affirmation card in focus
            if (currentAffirmation != null) {
                item {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "“${currentAffirmation.text}”",
                                style = MaterialTheme.typography.headlineSmall,
                                fontFamily = FontFamily.Serif,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 26.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Waveform & Progress
            item {
                val progress = if (total > 0) (elapsed.toFloat() / total.toFloat()).coerceIn(0f, 1f) else 0f

                WaveformView(
                    active = isPlaying && ambientOn,
                    progress = progress,
                    bars = 32
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = LuminaPrimary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val currentMins = elapsed / 60
                    val currentSecs = elapsed % 60
                    val totalMins = total / 60
                    val totalSecs = total % 60
                    Text(
                        text = String.format("%02d:%02d", currentMins, currentSecs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%02d:%02d", totalMins, totalSecs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Playback controls & Speed
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Speed button
                    val rates = listOf(0.8f, 0.9f, 1.0f, 1.1f)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .clickable {
                                val nextIdx = (rates.indexOf(speechRate) + 1) % rates.size
                                soundManager.setSpeechRate(rates[nextIdx])
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${speechRate}×",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Prev
                    IconButton(
                        onClick = { soundManager.skip(-1) },
                        enabled = currentIndex > 0,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous card",
                            tint = if (currentIndex > 0) MaterialTheme.colorScheme.onSurface else Color.LightGray,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Play/Pause Big button
                    IconButton(
                        onClick = { soundManager.togglePlay() },
                        modifier = Modifier
                            .size(64.dp)
                            .shadow(8.dp, CircleShape)
                            .background(LuminaPrimaryContainer, CircleShape)
                            .testTag("modal_player_toggle_btn")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Next
                    IconButton(
                        onClick = { soundManager.skip(1) },
                        enabled = currentIndex < (track?.affirmations?.size ?: 1) - 1,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next card",
                            tint = if (currentIndex < (track?.affirmations?.size ?: 1) - 1) MaterialTheme.colorScheme.onSurface else Color.LightGray,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Ambient Mute/Unmute
                    IconButton(
                        onClick = { soundManager.toggleAmbient() },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = if (ambientOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                            contentDescription = "Toggle ambience",
                            tint = if (ambientOn) LuminaSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Immersion Mixer Card
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = LuminaPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Soundscape Immersion Mixer",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Binaural 432Hz",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Voice Volume
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Voice Narration (${(voiceVolume * 100).toInt()}%)",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Slider(
                            value = voiceVolume,
                            onValueChange = { soundManager.setVoiceVolume(it) },
                            colors = SliderDefaults.colors(thumbColor = LuminaPrimary, activeTrackColor = LuminaPrimary)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Ambient Volume
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.SelfImprovement,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Ambient Soundscape (${(ambientVolume * 100).toInt()}%)",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Slider(
                            value = ambientVolume,
                            onValueChange = { soundManager.setAmbientVolume(it) },
                            colors = SliderDefaults.colors(thumbColor = LuminaSecondary, activeTrackColor = LuminaSecondary)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Soundscape selection pills
                        Text(
                            text = "ATMOSPHERE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ALL_SOUNDSCAPES.forEach { scape ->
                                val isSelected = currentSoundscape == scape.id
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (isSelected) LuminaSecondary else MaterialTheme.colorScheme.surfaceContainerLowest)
                                        .clickable { soundManager.setSoundscape(scape.id) }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = scape.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Meditation Progression List
            item {
                Text(
                    text = "MEDITATION PROGRESSION",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(track?.affirmations ?: emptyList()) { i, aff ->
                val isDone = i < currentIndex
                val isNow = i == currentIndex
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isNow) LuminaPrimaryFixed.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceContainerLowest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { soundManager.seekTo(i) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    when {
                                        isDone -> LuminaSecondary
                                        isNow -> LuminaPrimaryContainer
                                        else -> MaterialTheme.colorScheme.surfaceContainerHigh
                                    },
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            } else if (isNow) {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            } else {
                                Text(text = "${i + 1}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = aff.text,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        if (isNow) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(LuminaPrimary)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = "PLAYING", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Action shortcuts
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (currentAffirmation != null) {
                        Button(
                            onClick = { onSaveMantraCard(currentAffirmation.text) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Save Mantra", style = MaterialTheme.typography.labelMedium)
                        }

                        Button(
                            onClick = {
                                soundManager.setPlayerExpanded(false)
                                onOpenStudioWithText(currentAffirmation.text)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "To Studio", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
