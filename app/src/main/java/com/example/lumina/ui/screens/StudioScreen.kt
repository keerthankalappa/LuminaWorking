package com.example.lumina.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Texture
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumina.audio.LuminaSoundManager
import com.example.lumina.data.model.CustomMantra
import com.example.lumina.data.model.SeedData
import com.example.lumina.data.storage.LuminaDatabase
import com.example.lumina.ui.components.MantraCanvasCard
import com.example.lumina.ui.components.PALETTES_MAP
import com.example.lumina.ui.components.shareMantraIntent
import com.example.lumina.ui.theme.LuminaParchment
import com.example.lumina.ui.theme.LuminaPrimary
import com.example.lumina.ui.theme.LuminaPrimaryContainer
import com.example.lumina.ui.theme.LuminaPrimaryFixed
import com.example.lumina.ui.theme.LuminaSecondary
import com.example.lumina.ui.theme.LuminaSecondaryContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

val INTENTS_LIST = listOf(
    "Unapologetic Purpose",
    "Grounded Calm",
    "Courageous Compassion",
    "Creative Flow",
    "Restful Boundaries",
    "Abundant Gratitude"
)

val AURAS_LIST = listOf(
    "luminous-glow" to ("Luminous Glow" to Icons.Default.Flare),
    "petal-waves" to ("Petal Waves" to Icons.Default.Waves),
    "starlight" to ("Starlight" to Icons.Default.AutoAwesome),
    "raw-parchment" to ("Raw Parchment" to Icons.Default.Texture)
)

val TYPOGRAPHY_LIST = listOf(
    "playfair" to "Playfair Poetic Serif",
    "jakarta" to "Plus Jakarta Modern",
    "classical" to "Classical Contemplative"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(
    database: LuminaDatabase,
    soundManager: LuminaSoundManager,
    initialDraftText: String = "",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val savedMantras by database.mantrasFlow.collectAsState()
    val journalEntries by database.journalFlow.collectAsState()

    var editingId by remember { mutableStateOf<String?>(null) }
    var draftText by remember {
        mutableStateOf(
            if (initialDraftText.isNotBlank()) initialDraftText
            else "I honor the rhythm of my own unfolding; I do not need to rush what is meant to grow with deep roots."
        )
    }
    var draftAttribution by remember { mutableStateOf("Morning Intention") }
    var draftIntent by remember { mutableStateOf(INTENTS_LIST[1]) }
    var draftPalette by remember { mutableStateOf("dawn-amber") }
    var draftTypography by remember { mutableStateOf("playfair") }
    var draftAura by remember { mutableStateOf("luminous-glow") }
    var draftMicroAction by remember { mutableStateOf("Take a 20-minute silent walk without phone and write one kind note.") }
    var intentDropdownExpanded by remember { mutableStateOf(false) }

    val currentDraft = remember(editingId, draftText, draftAttribution, draftIntent, draftPalette, draftTypography, draftAura, draftMicroAction) {
        CustomMantra(
            id = editingId ?: "",
            text = draftText,
            attribution = draftAttribution,
            intent = draftIntent,
            palette = draftPalette,
            typography = draftTypography,
            aura = draftAura,
            microAction = draftMicroAction
        )
    }

    // Mood color dictionary for analytics
    val moodColors = mapOf(
        "grounded" to Color(0xFF006C4A),
        "expansive" to Color(0xFFF59E0B),
        "gentle" to Color(0xFF82F5C1),
        "reflective" to Color(0xFFA7A9FF),
        "determined" to Color(0xFF855300)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(LuminaPrimaryFixed)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Architecture, contentDescription = null, tint = LuminaPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Sacred atelier • Vol. IV", style = MaterialTheme.typography.labelSmall, color = LuminaPrimary, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Mindset Studio & Growth Journal",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "A private sanctuary to track your emotional landscape, compose custom personal affirmations, and generate bespoke contemplative art.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Live Preview Canvas
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(LuminaSecondary, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Live sanctuary canvas", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        IconButton(
                            onClick = {
                                val palettes = PALETTES_MAP.keys.toList()
                                val auras = AURAS_LIST.map { it.first }
                                val types = TYPOGRAPHY_LIST.map { it.first }
                                val rand = Random()
                                draftPalette = palettes[rand.nextInt(palettes.size)]
                                draftAura = auras[rand.nextInt(auras.size)]
                                draftTypography = types[rand.nextInt(types.size)]
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Shuffle, contentDescription = "Shuffle style", tint = LuminaPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    MantraCanvasCard(
                        mantra = currentDraft,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Auto-synced with your sanctuary", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Button(
                            onClick = { shareMantraIntent(context, draftText, draftAttribution) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow, contentColor = MaterialTheme.colorScheme.onSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Share", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // Step 1: Craft Your Mantra
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(LuminaPrimaryFixed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "1", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = LuminaPrimary)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Craft Your Mantra", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
                        }
                        Text(text = "Editorial seed", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Focal intent theme", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = intentDropdownExpanded,
                        onExpandedChange = { intentDropdownExpanded = !intentDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = draftIntent,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = intentDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = LuminaParchment,
                                unfocusedContainerColor = LuminaParchment
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = intentDropdownExpanded,
                            onDismissRequest = { intentDropdownExpanded = false }
                        ) {
                            INTENTS_LIST.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        draftIntent = item
                                        intentDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "The inscription", style = MaterialTheme.typography.labelMedium)
                        Text(text = "${draftText.length} / 280", style = MaterialTheme.typography.labelSmall, color = LuminaPrimary)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = draftText,
                        onValueChange = { if (it.length <= 280) draftText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("mantra_inscription_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = LuminaParchment,
                            unfocusedContainerColor = LuminaParchment,
                            focusedBorderColor = LuminaPrimaryContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Attribution / sub-intention", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = draftAttribution,
                            onValueChange = { draftAttribution = it },
                            placeholder = { Text("— Sarah's Morning Intention") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = LuminaParchment,
                                unfocusedContainerColor = LuminaParchment
                            )
                        )

                        Button(
                            onClick = {
                                val pool = SeedData.decks.map { it.epigraph }
                                val rand = pool[Random().nextInt(pool.size)]
                                draftText = rand
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow, contentColor = MaterialTheme.colorScheme.onSurface),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(imageVector = Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Inspire", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // Step 2: Visual Art & Atmospheric Aura
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(LuminaPrimaryFixed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "2", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = LuminaPrimary)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Visual Art & Aura", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
                        }
                        Text(text = "Generative canvas", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Atmosphere & colour field", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Palettes selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PALETTES_MAP.forEach { (key, pal) ->
                            val isSelected = draftPalette == key
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) LuminaPrimaryFixed.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceContainerLow)
                                    .border(if (isSelected) 2.dp else 0.dp, if (isSelected) LuminaPrimaryContainer else Color.Transparent, RoundedCornerShape(12.dp))
                                    .clickable { draftPalette = key }
                                    .padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(pal.to, CircleShape)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = pal.name.split(" ")[0], style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Editorial typography", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    TYPOGRAPHY_LIST.forEach { (key, label) ->
                        val isSelected = draftTypography == key
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) LuminaPrimaryFixed.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceContainerLow)
                                .clickable { draftTypography = key }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .border(2.dp, if (isSelected) LuminaPrimary else Color.Gray, CircleShape)
                                    .padding(3.dp)
                            ) {
                                if (isSelected) {
                                    Box(modifier = Modifier.fillMaxSize().background(LuminaPrimary, CircleShape))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = label, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Ambient aura pattern", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AURAS_LIST.forEach { (key, pair) ->
                            val (label, icon) = pair
                            val isSelected = draftAura == key
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) LuminaPrimaryFixed else MaterialTheme.colorScheme.surfaceContainerLow)
                                    .clickable { draftAura = key }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(imageVector = icon, contentDescription = null, tint = if (isSelected) LuminaPrimary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = label.split(" ")[0], style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Step 3: Paired Kinetic Ripple & Save
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(LuminaPrimaryFixed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "3", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = LuminaPrimary)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Paired Kinetic Ripple", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(LuminaSecondaryContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(text = "Mindset → Reality", style = MaterialTheme.typography.labelSmall, color = LuminaSecondary, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "What micro-action will ground this mantra today?", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = draftMicroAction,
                        onValueChange = { draftMicroAction = it },
                        leadingIcon = {
                            Icon(imageVector = Icons.AutoMirrored.Filled.DirectionsWalk, contentDescription = null, tint = LuminaSecondary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = LuminaParchment,
                            unfocusedContainerColor = LuminaParchment
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val savedId = database.saveMantra(currentDraft)
                                editingId = savedId
                            },
                            enabled = draftText.trim().length >= 3,
                            colors = ButtonDefaults.buttonColors(containerColor = LuminaPrimaryContainer, contentColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("save_custom_mantra_btn")
                        ) {
                            Icon(imageVector = Icons.Default.BookmarkAdded, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (editingId != null) "Update Mantra" else "Save to Sanctuary", style = MaterialTheme.typography.labelMedium)
                        }

                        Button(
                            onClick = { soundManager.speakText(draftText) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow, contentColor = MaterialTheme.colorScheme.onSurface),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Hear It", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // Saved Mantras Collection
        if (savedMantras.isNotEmpty()) {
            item {
                Text(
                    text = "Your Mantra Collection (${savedMantras.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(savedMantras) { m ->
                        Column(modifier = Modifier.width(220.dp)) {
                            MantraCanvasCard(
                                mantra = m,
                                compact = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Edit",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = LuminaPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        editingId = m.id
                                        draftText = m.text
                                        draftAttribution = m.attribution
                                        draftIntent = m.intent
                                        draftPalette = m.palette
                                        draftTypography = m.typography
                                        draftAura = m.aura
                                        draftMicroAction = m.microAction
                                    }
                                )
                                Text(
                                    text = "Share",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.clickable {
                                        shareMantraIntent(context, m.text, m.attribution)
                                    }
                                )
                                Text(
                                    text = "Delete",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.clickable {
                                        database.deleteMantra(m.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Emotional Analytics Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Sanctuary Rhythm (Last 30 Days)", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(LuminaPrimaryFixed)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "87% Regularity", style = MaterialTheme.typography.labelSmall, color = LuminaPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 30 Dots Grid
                    val sampleMoods = listOf("grounded", "grounded", "expansive", "gentle", "reflective", "determined")
                    val entriesCount = journalEntries.size
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        for (row in 0 until 3) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                for (col in 0 until 10) {
                                    val idx = row * 10 + col
                                    val moodKey = if (idx < entriesCount) {
                                        journalEntries[idx % entriesCount].mood
                                    } else {
                                        sampleMoods[idx % sampleMoods.size]
                                    }
                                    val dotColor = moodColors[moodKey] ?: Color(0xFFE7EEFF)
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .size(16.dp)
                                            .background(dotColor, CircleShape)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Dominant Resonance", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    listOf(
                        "Grounded" to (42 to Color(0xFF006C4A)),
                        "Expansive" to (24 to Color(0xFFF59E0B)),
                        "Reflective" to (18 to Color(0xFFA7A9FF)),
                        "Gentle" to (16 to Color(0xFF82F5C1))
                    ).forEach { (label, pair) ->
                        val (pct, color) = pair
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = label, style = MaterialTheme.typography.labelSmall)
                            Text(text = "$pct%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        LinearProgressIndicator(
                            progress = { pct / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = color,
                            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        // Recent Journal Reflections
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.HistoryEdu, contentDescription = null, tint = LuminaPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Recent Journal Reflections", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
                        }
                        Text(text = "${journalEntries.size} entries", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (journalEntries.isEmpty()) {
                        Text(
                            text = "No reflections yet. Check in with your daily energy in the Sanctuary.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        journalEntries.take(8).forEach { j ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .size(10.dp)
                                        .background(moodColors[j.mood] ?: LuminaSecondary, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = j.body, style = MaterialTheme.typography.bodyMedium)
                                    val dateStr = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(j.createdAt))
                                    Text(
                                        text = "${j.mood.replaceFirstChar { it.uppercase() }}${if (j.rippleAction.isNotBlank()) " • ${j.rippleAction}" else ""} • $dateStr",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = LuminaPrimary
                                    )
                                }
                                IconButton(
                                    onClick = { database.deleteJournal(j.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                }
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
