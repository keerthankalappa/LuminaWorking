package com.example.lumina.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumina.data.model.CustomMantra

data class StudioPaletteDef(
    val name: String,
    val from: Color,
    val to: Color,
    val ink: Color,
    val accent: Color
)

val PALETTES_MAP = mapOf(
    "dawn-amber" to StudioPaletteDef("Dawn Amber", Color(0xFFFFF3DC), Color(0xFFFBD38D), Color(0xFF2A1700), Color(0xFF855300)),
    "eucalyptus" to StudioPaletteDef("Eucalyptus", Color(0xFFE8FBF1), Color(0xFF9FE6C4), Color(0xFF002114), Color(0xFF006C4A)),
    "twilight" to StudioPaletteDef("Twilight", Color(0xFFEEEEFF), Color(0xFFB9BAFF), Color(0xFF07006C), Color(0xFF494BD6)),
    "solstice" to StudioPaletteDef("Solstice", Color(0xFFFFE9D6), Color(0xFFF6A96B), Color(0xFF3B1D00), Color(0xFFB45309)),
    "midnight" to StudioPaletteDef("Midnight", Color(0xFF263143), Color(0xFF0F172A), Color(0xFFF5F3EF), Color(0xFFFFB95F))
)

@Composable
fun MantraCanvasCard(
    mantra: CustomMantra,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val palette = PALETTES_MAP[mantra.palette] ?: PALETTES_MAP["dawn-amber"]!!

    val (fontFamily, fontStyle, fontWeight) = when (mantra.typography) {
        "jakarta" -> Triple(FontFamily.Default, FontStyle.Normal, FontWeight.SemiBold)
        "classical" -> Triple(FontFamily.Serif, FontStyle.Italic, FontWeight.Normal)
        else -> Triple(FontFamily.Serif, FontStyle.Normal, FontWeight.Medium) // playfair
    }

    Box(
        modifier = modifier
            .aspectRatio(4f / 5f)
            .shadow(elevation = if (compact) 4.dp else 12.dp, shape = RoundedCornerShape(if (compact) 16.dp else 24.dp))
            .clip(RoundedCornerShape(if (compact) 16.dp else 24.dp))
    ) {
        // Background Aura & Palette Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(palette.from, palette.to),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
            )

            when (mantra.aura) {
                "luminous-glow" -> {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = 0.65f), Color.Transparent),
                            center = Offset(size.width * 0.7f, size.height * 0.2f),
                            radius = size.width * 0.45f
                        )
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = 0.4f), Color.Transparent),
                            center = Offset(size.width * 0.2f, size.height * 0.8f),
                            radius = size.width * 0.4f
                        )
                    )
                }
                "petal-waves" -> {
                    for (i in 1..4) {
                        drawCircle(
                            color = palette.accent.copy(alpha = 0.08f),
                            radius = (size.width * 0.25f * i),
                            center = Offset(size.width * 0.5f, size.height * 1.05f)
                        )
                    }
                }
                "starlight" -> {
                    val stars = listOf(
                        Offset(size.width * 0.2f, size.height * 0.3f),
                        Offset(size.width * 0.7f, size.height * 0.15f),
                        Offset(size.width * 0.8f, size.height * 0.7f),
                        Offset(size.width * 0.35f, size.height * 0.85f),
                        Offset(size.width * 0.55f, size.height * 0.5f)
                    )
                    stars.forEach { pt ->
                        drawCircle(color = Color.White.copy(alpha = 0.85f), radius = 3.dp.toPx(), center = pt)
                    }
                }
                else -> { // raw-parchment
                    // subtle warm texture vignette
                    drawCircle(
                        color = Color.Black.copy(alpha = 0.04f),
                        radius = size.maxDimension / 2f,
                        center = Offset(size.width / 2f, size.height / 2f)
                    )
                }
            }
        }

        // Card Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (compact) 16.dp else 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = palette.accent,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "LUMINA",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.ink.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.55f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = mantra.intent,
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.ink,
                        fontSize = 10.sp
                    )
                }
            }

            // Central Affirmation Quote
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "“",
                    style = MaterialTheme.typography.displayLarge,
                    color = palette.accent.copy(alpha = 0.35f),
                    fontSize = if (compact) 36.sp else 48.sp,
                    lineHeight = if (compact) 24.sp else 32.sp
                )
                Text(
                    text = mantra.text.ifBlank { "Your mantra will appear here as you write…" },
                    color = palette.ink,
                    fontFamily = fontFamily,
                    fontStyle = fontStyle,
                    fontWeight = fontWeight,
                    fontSize = if (compact) 13.sp else 18.sp,
                    lineHeight = if (compact) 18.sp else 26.sp,
                    textAlign = TextAlign.Center,
                    maxLines = if (compact) 5 else 8
                )
                if (mantra.attribution.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "— ${mantra.attribution} —",
                        color = palette.accent,
                        style = MaterialTheme.typography.labelMedium,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            // Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = palette.accent.copy(alpha = 0.7f),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = "Daily Sanctuary",
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.ink.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = "Mindset → Reality",
                    style = MaterialTheme.typography.labelSmall,
                    color = palette.ink.copy(alpha = 0.7f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

fun shareMantraIntent(context: Context, text: String, attribution: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "“$text”${if (attribution.isNotBlank()) " — $attribution" else ""} (from Lumina)")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Lumina Mantra")
    context.startActivity(shareIntent)
}
