package com.example.lumina.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun DeckArtCard(
    palette: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val (skyFrom, skyTo, layerColors, sunColor) = when (palette) {
            "earth-dawn" -> Quad(
                Color(0xFFFBF3E6), Color(0xFFF4E3CC),
                listOf(Color(0xFFD9A066), Color(0xFFC27A4A), Color(0xFF8FA58A), Color(0xFF5F7F6E)),
                Color(0xFFF5B971)
            )
            "sand-gold" -> Quad(
                Color(0xFFFAF6EE), Color(0xFFF3EAD8),
                listOf(Color(0xFFD6A85B), Color(0xFFB8894A), Color(0xFFA9B8A0), Color(0xFFE6C79A)),
                null
            )
            "sunburst" -> Quad(
                Color(0xFFFFF6EA), Color(0xFFFDE3C4),
                listOf(Color(0xFFF59E0B), Color(0xFFFBBF77), Color(0xFFFCD9A8), Color(0xFFFFE9CF)),
                Color(0xFFFFB95F)
            )
            "forest" -> Quad(
                Color(0xFFDFE8D6), Color(0xFFA7BD9A),
                listOf(Color(0xFF476B4F), Color(0xFF2F513B), Color(0xFF1F3B2A), Color(0xFFF4E2B0)),
                Color(0xFFFBE8B5)
            )
            "lake-dusk" -> Quad(
                Color(0xFFC9CDF0), Color(0xFF8E95D6),
                listOf(Color(0xFF6F74C9), Color(0xFF4C4F9E), Color(0xFF2E3270), Color(0xFFE1E0FF)),
                Color(0xFFF3F1FF)
            )
            "peach-bloom" -> Quad(
                Color(0xFFFFF4E8), Color(0xFFFDE0C8),
                listOf(Color(0xFFF6A96B), Color(0xFFF3C19A), Color(0xFFE98852), Color(0xFF8FB08F)),
                null
            )
            else -> Quad( // stone-mist
                Color(0xFFE9ECE6), Color(0xFFCFD6CF),
                listOf(Color(0xFF6D6A63), Color(0xFF8A867C), Color(0xFFA7A296), Color(0xFF5B7563)),
                null
            )
        }

        // Draw sky gradient
        drawRect(
            brush = Brush.verticalGradient(listOf(skyFrom, skyTo)),
            size = size
        )

        // Draw radiant sun if defined
        sunColor?.let { sun ->
            drawCircle(
                color = sun.copy(alpha = 0.85f),
                radius = h * 0.35f,
                center = Offset(w * 0.65f, h * 0.45f)
            )
            drawCircle(
                color = sun.copy(alpha = 0.25f),
                radius = h * 0.55f,
                center = Offset(w * 0.65f, h * 0.45f)
            )
        }

        // Layered rolling dunes/hills
        layerColors.forEachIndexed { index, color ->
            val path = Path()
            val yOffset = h * (0.48f + index * 0.14f)
            path.moveTo(0f, h)
            path.lineTo(0f, yOffset)

            val cp1X = w * 0.25f
            val cp1Y = yOffset - (h * 0.12f * (1f / (index + 1)))
            val cp2X = w * 0.75f
            val cp2Y = yOffset + (h * 0.10f * (1f / (index + 1)))

            path.cubicTo(cp1X, cp1Y, cp2X, cp2Y, w, yOffset - 10f)
            path.lineTo(w, h)
            path.close()

            drawPath(path = path, color = color)
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
