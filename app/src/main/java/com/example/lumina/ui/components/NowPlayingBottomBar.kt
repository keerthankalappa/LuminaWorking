package com.example.lumina.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lumina.audio.LuminaSoundManager
import com.example.lumina.ui.theme.LuminaSecondary
import com.example.lumina.ui.theme.LuminaSecondaryContainer

@Composable
fun NowPlayingBottomBar(
    soundManager: LuminaSoundManager,
    modifier: Modifier = Modifier
) {
    val track = soundManager.currentTrack.value
    val isPlaying = soundManager.isPlaying.value
    val currentIndex = soundManager.currentIndex.value

    AnimatedVisibility(
        visible = track != null,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = modifier
    ) {
        if (track == null) return@AnimatedVisibility

        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { soundManager.setPlayerExpanded(true) },
            shape = RoundedCornerShape(32.dp),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surfaceContainerLowest
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Pulse icon
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(LuminaSecondaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RadioButtonChecked,
                        contentDescription = "Playing indicator",
                        tint = LuminaSecondary,
                        modifier = Modifier
                            .size(20.dp)
                            .scale(if (isPlaying) pulseScale else 1f)
                    )
                }

                // Title and card index
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = "${if (isPlaying) "Now Playing" else "Paused"} • Card ${currentIndex + 1} of ${track.affirmations.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = track.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { soundManager.skip(-1) },
                        enabled = currentIndex > 0,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous card",
                            tint = if (currentIndex > 0) MaterialTheme.colorScheme.onSurface else Color.LightGray
                        )
                    }

                    IconButton(
                        onClick = { soundManager.togglePlay() },
                        modifier = Modifier
                            .size(42.dp)
                            .background(LuminaSecondary, CircleShape)
                            .testTag("now_playing_toggle_btn")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { soundManager.skip(1) },
                        enabled = currentIndex < track.affirmations.size - 1,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next card",
                            tint = if (currentIndex < track.affirmations.size - 1) MaterialTheme.colorScheme.onSurface else Color.LightGray
                        )
                    }

                    IconButton(
                        onClick = { soundManager.setPlayerExpanded(true) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInFull,
                            contentDescription = "Expand Player",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { soundManager.stop() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Player",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
