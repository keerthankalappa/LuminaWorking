package com.example.lumina.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumina.ui.theme.LuminaSecondary
import com.example.lumina.ui.theme.LuminaSecondaryContainer
import com.example.lumina.ui.theme.LuminaSecondaryFixed
import kotlinx.coroutines.delay

@Composable
fun BreathingRingView(
    modifier: Modifier = Modifier
) {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRunning) {
        if (!isRunning) return@LaunchedEffect
        while (isRunning && elapsedSeconds < 60) {
            delay(1000)
            elapsedSeconds += 1
        }
        if (elapsedSeconds >= 60) {
            isRunning = false
            elapsedSeconds = 0
        }
    }

    val cycle = elapsedSeconds % 12
    val phaseIdx = when {
        cycle < 4 -> 0
        cycle < 8 -> 1
        else -> 2
    }
    val phaseLabel = when (phaseIdx) {
        0 -> "Inhale deeply"
        1 -> "Hold softly"
        else -> "Exhale slowly"
    }
    val secondsLeft = 4 - (cycle % 4)

    val targetScale = when {
        !isRunning -> 0.85f
        phaseIdx == 0 -> 0.75f + (cycle / 4f) * 0.25f
        phaseIdx == 1 -> 1.0f
        else -> 1.0f - ((cycle - 8) / 4f) * 0.25f
    }

    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 900),
        label = "breath_scale"
    )

    val progress = if (isRunning) elapsedSeconds / 60f else 0.72f

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(LuminaSecondaryContainer.copy(alpha = 0.35f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(80.dp)) {
                val strokeWidth = 5.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2f
                drawCircle(
                    color = Color(0xFFD1FAE5),
                    radius = radius,
                    style = Stroke(width = strokeWidth)
                )
                drawArc(
                    color = LuminaSecondary,
                    startAngle = -90f,
                    sweepAngle = progress * 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .scale(animatedScale)
                    .shadow(
                        elevation = if (isRunning && phaseIdx == 1) 12.dp else 2.dp,
                        shape = CircleShape
                    )
                    .background(LuminaSecondaryFixed, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = "Breathing icon",
                        tint = LuminaSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isRunning) "${secondsLeft}s" else "4s",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "4-4-4 CADENCE",
                style = MaterialTheme.typography.labelSmall,
                color = LuminaSecondary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (isRunning) "$phaseLabel (${secondsLeft}s)" else "Inhale deeply (4s)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (isRunning) {
                        isRunning = false
                        elapsedSeconds = 0
                    } else {
                        elapsedSeconds = 0
                        isRunning = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LuminaSecondary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.testTag("breathing_toggle_btn")
            ) {
                Text(
                    text = if (isRunning) "End Session" else "Begin 1-Min Breath",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
