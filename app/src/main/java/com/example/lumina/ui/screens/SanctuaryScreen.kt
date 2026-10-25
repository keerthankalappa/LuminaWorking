package com.example.lumina.ui.screens

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumina.audio.ActiveTrack
import com.example.lumina.audio.LuminaSoundManager
import com.example.lumina.data.model.SeedData
import com.example.lumina.data.storage.LuminaDatabase
import com.example.lumina.ui.components.BreathingRingView
import com.example.lumina.ui.components.WaveformView
import com.example.lumina.ui.components.shareMantraIntent
import com.example.lumina.ui.theme.LuminaCream
import com.example.lumina.ui.theme.LuminaParchment
import com.example.lumina.ui.theme.LuminaPrimary
import com.example.lumina.ui.theme.LuminaPrimaryContainer
import com.example.lumina.ui.theme.LuminaPrimaryFixed
import com.example.lumina.ui.theme.LuminaSecondary
import com.example.lumina.ui.theme.LuminaSecondaryContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SanctuaryScreen(
    database: LuminaDatabase,
    soundManager: LuminaSoundManager,
    onNavigateToGalleryDeck: (String) -> Unit,
    onNavigateToRippleCompose: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userStats by database.userStatsFlow.collectAsState()
    val bookmarks by database.bookmarksFlow.collectAsState()
    val ripples by database.ripplesFlow.collectAsState()
    val isPlaying by soundManager.isPlaying.collectAsState()
    val activeTrack by soundManager.currentTrack.collectAsState()
    val ambientOn by soundManager.ambientOn.collectAsState()
    val currentSoundscape by soundManager.currentSoundscape.collectAsState()
    val ambientVol by soundManager.ambientVolume.collectAsState()

    val dailyDeck = SeedData.decks[0]
    val dailyAffirmation = dailyDeck.affirmations[0]
    val dailyAction = SeedData.actions[0]

    val isBookmarked = bookmarks.contains(dailyAffirmation.id)
    var isActionPracticed by remember { mutableStateOf(false) }

    var selectedMood by remember { mutableStateOf<String?>("grounded") }
    var intentionText by remember { mutableStateOf("") }
    var intentionSavedToast by remember { mutableStateOf(false) }

    val isThisTrackPlaying = activeTrack?.deckSlug == dailyDeck.slug && isPlaying

    val todayDateStr = remember {
        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Top Header: Date, greeting, and ambient button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(LuminaPrimaryContainer, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$todayDateStr • Day 48 of Spring",
                            style = MaterialTheme.typography.labelSmall,
                            color = LuminaPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Good morning, Luminary.",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "The world awaits your calm presence.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Ambient Pill Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .clickable { soundManager.toggleAmbient() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = LuminaSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ambience",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(if (ambientOn) LuminaSecondary else Color.Gray, CircleShape)
                    )
                }
            }
        }

        // Daily Focal Mantra Hero Card
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    LuminaPrimaryFixed.copy(alpha = 0.45f),
                                    Color.White.copy(alpha = 0.95f),
                                    LuminaSecondaryContainer.copy(alpha = 0.25f)
                                )
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        // Card Tag & Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(LuminaPrimaryFixed)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "DAILY FOCAL MANTRA",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LuminaPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "• ${dailyDeck.theme}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = { database.toggleBookmark(dailyAffirmation.id) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark mantra",
                                        tint = if (isBookmarked) LuminaPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = { shareMantraIntent(context, dailyAffirmation.text, dailyDeck.title) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share mantra",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Inscription Quote
                        Text(
                            text = "“${dailyAffirmation.text}”",
                            style = MaterialTheme.typography.headlineLarge,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 34.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "From “${dailyDeck.title}” →",
                            style = MaterialTheme.typography.labelMedium,
                            color = LuminaPrimary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onNavigateToGalleryDeck(dailyDeck.slug) }
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Audio Narration bar inside card
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.85f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(LuminaPrimaryContainer, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.RecordVoiceOver,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "Guided by ${dailyDeck.guide}",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "~${dailyDeck.audioMinutes} min mindful immersion",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    // Play / Pause button
                                    IconButton(
                                        onClick = {
                                            if (isThisTrackPlaying) {
                                                soundManager.togglePlay()
                                            } else {
                                                soundManager.playTrack(
                                                    ActiveTrack(
                                                        id = "daily_track",
                                                        title = dailyDeck.title,
                                                        subtitle = "Daily Focal Mantra",
                                                        guide = dailyDeck.guide,
                                                        affirmations = dailyDeck.affirmations,
                                                        deckSlug = dailyDeck.slug
                                                    )
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .size(44.dp)
                                            .shadow(4.dp, CircleShape)
                                            .background(LuminaPrimaryContainer, CircleShape)
                                            .testTag("daily_mantra_play_btn")
                                    ) {
                                        Icon(
                                            imageVector = if (isThisTrackPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = if (isThisTrackPlaying) "Pause" else "Play",
                                            tint = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                WaveformView(
                                    active = isThisTrackPlaying && ambientOn,
                                    progress = if (isThisTrackPlaying) 0.5f else 0.1f,
                                    bars = 26
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "00:00", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { soundManager.setPlayerExpanded(true) }
                                    ) {
                                        Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = LuminaPrimary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Pad: ${(ambientVol * 100).toInt()}% · Mixer",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(text = "14:00", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Integrated 4-4-4 Breathing Ring
                        BreathingRingView(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }

        // Action in the World Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(LuminaSecondaryContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ACTION IN THE WORLD",
                                style = MaterialTheme.typography.labelSmall,
                                color = LuminaSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "+${dailyAction.karma} Karma",
                            style = MaterialTheme.typography.labelMedium,
                            color = LuminaPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = dailyAction.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = dailyAction.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (!isActionPracticed) {
                                    isActionPracticed = true
                                    database.practiceAction(dailyAction.karma)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isActionPracticed) LuminaSecondaryContainer else LuminaSecondary,
                                contentColor = if (isActionPracticed) LuminaSecondary else Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("mark_practiced_btn")
                        ) {
                            Icon(
                                imageVector = if (isActionPracticed) Icons.Default.TaskAlt else Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isActionPracticed) "Practised Today" else "Mark as Practiced",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        Button(
                            onClick = { onNavigateToRippleCompose(dailyAction.title) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Waves, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Share Ripple", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        // Daily Energy & Intention Check-in
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PsychologyAlt,
                                contentDescription = null,
                                tint = LuminaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily Energy & Intention",
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Text(
                            text = "Private",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "How does your internal sanctuary feel right now? Choose your prevailing state:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5 Mood Chips
                    val moods = listOf(
                        "grounded" to "Grounded 🌱",
                        "expansive" to "Expansive ☀️",
                        "gentle" to "Gentle 🕊️",
                        "reflective" to "Reflective 🌙",
                        "determined" to "Determined ⚡"
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        moods.forEach { (id, label) ->
                            val isSelected = selectedMood == id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) LuminaPrimaryFixed else LuminaParchment)
                                    .clickable { selectedMood = id }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) LuminaPrimary else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Where will your kindness be most needed before sunset?",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = intentionText,
                        onValueChange = { intentionText = it },
                        placeholder = {
                            Text(
                                text = "Write with tenderness. E.g., Offering patience to my team during deadline friction…",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("intention_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = LuminaParchment,
                            unfocusedContainerColor = LuminaParchment,
                            focusedBorderColor = LuminaPrimaryContainer,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (intentionSavedToast) "Saved to your journal! ✨" else "Private to your journal",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (intentionSavedToast) LuminaSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = {
                                if (intentionText.isNotBlank() && selectedMood != null) {
                                    database.saveJournal(selectedMood!!, intentionText.trim(), dailyAction.title)
                                    intentionText = ""
                                    intentionSavedToast = true
                                }
                            },
                            enabled = intentionText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = LuminaPrimaryContainer, contentColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("save_intention_btn")
                        ) {
                            Text(text = "Save to Studio", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        // Global Resonance Today & Community Moments
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Public,
                                    contentDescription = null,
                                    tint = LuminaSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Global Resonance Today",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                            Text(
                                text = "Unified awareness across the collective",
                                style = MaterialTheme.typography.labelSmall,
                                color = LuminaSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "4,280",
                                style = MaterialTheme.typography.headlineMedium,
                                color = LuminaPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "actions practised today",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "LIVE COMMUNITY MOMENTS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ripples.take(3).forEach { r ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(LuminaSecondary, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${r.authorName}: ${r.body}",
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "Just now",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Monthly Challenge preview pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToRippleCompose("") }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(LuminaPrimaryFixed, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = null,
                                        tint = LuminaPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "May Challenge: 5,000 Ripples",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Text(
                                        text = "3,420 / 5,000 ripples completed",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Join",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
