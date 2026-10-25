package com.example.lumina.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lumina.ui.theme.LuminaPrimary
import com.example.lumina.ui.theme.LuminaPrimaryFixedDim

@Composable
fun WaveformView(
    active: Boolean,
    progress: Float,
    modifier: Modifier = Modifier,
    bars: Int = 28
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")

    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until bars) {
            val barFraction = i.toFloat() / bars
            val isPlayed = barFraction <= progress

            // Wave height modulation
            val baseHeight = kotlin.math.sin((i.toDouble() / bars) * Math.PI).toFloat()
            val dynamicHeight = if (active) {
                val waveFactor = kotlin.math.sin((i.toDouble() * 0.45) + (animPhase * Math.PI * 2)).toFloat()
                (0.25f + 0.75f * kotlin.math.abs(waveFactor * baseHeight)).coerceIn(0.15f, 1f)
            } else {
                (0.2f + 0.35f * baseHeight).coerceIn(0.15f, 0.7f)
            }

            val color = if (isPlayed) {
                LuminaPrimary
            } else {
                LuminaPrimaryFixedDim.copy(alpha = 0.5f)
            }

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((24 * dynamicHeight).dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
            )
        }
    }
}
