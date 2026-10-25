package com.example.lumina.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumina.data.model.CommunityChallenge
import com.example.lumina.data.model.RippleItem
import com.example.lumina.data.model.SeedData
import com.example.lumina.data.storage.LuminaDatabase
import com.example.lumina.ui.components.shareMantraIntent
import com.example.lumina.ui.theme.LuminaParchment
import com.example.lumina.ui.theme.LuminaPrimary
import com.example.lumina.ui.theme.LuminaPrimaryContainer
import com.example.lumina.ui.theme.LuminaPrimaryFixed
import com.example.lumina.ui.theme.LuminaSecondary
import com.example.lumina.ui.theme.LuminaSecondaryContainer
import com.example.lumina.ui.theme.LuminaTertiary
import com.example.lumina.ui.theme.LuminaTertiaryFixed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RippleScreen(
    database: LuminaDatabase,
    initialComposeText: String = "",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userStats by database.userStatsFlow.collectAsState()
    val ripples by database.ripplesFlow.collectAsState()

    var selectedFilterCategory by remember { mutableStateOf("") }

    // Composer state
    var composeCategory by remember { mutableStateOf("kindness") }
    var composeBody by remember { mutableStateOf(if (initialComposeText.isNotBlank()) "Today I practised “$initialComposeText”: " else "") }
    var composeMantraTag by remember { mutableStateOf("") }
    var composeLocation by remember { mutableStateOf("") }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    val categories = listOf(
        "" to "All Stories",
        "environment" to "Environmental Actions",
        "kindness" to "Acts of Kindness",
        "mentorship" to "Creative Mentorship",
        "local" to "Local Change"
    )

    val filteredRipples = remember(ripples, selectedFilterCategory) {
        if (selectedFilterCategory.isBlank()) ripples else ripples.filter { it.category == selectedFilterCategory }
    }

    val challenge = remember { CommunityChallenge() }
    val progressPct = (challenge.progress.toFloat() / challenge.goal.toFloat() * 100).toInt()

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
                    .background(LuminaTertiaryFixed)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Cyclone, contentDescription = null, tint = LuminaTertiary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Collective momentum", style = MaterialTheme.typography.labelSmall, color = LuminaTertiary, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Community Ripple & Real-World Impact",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Affirmation into action. Discover how our collective intentions ripple out into tangible compassion, neighborhood restoration, and mutual care.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Stat Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatTile(
                    title = "4,280",
                    label = "Actions",
                    icon = Icons.Default.VolunteerActivism,
                    tint = LuminaPrimary,
                    containerColor = LuminaPrimaryFixed,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    title = "${ripples.size + 1846}",
                    label = "Ripples",
                    icon = Icons.Default.Waves,
                    tint = LuminaSecondary,
                    containerColor = LuminaSecondaryContainer,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    title = "8,920",
                    label = "Lights",
                    icon = Icons.Default.Lightbulb,
                    tint = LuminaTertiary,
                    containerColor = LuminaTertiaryFixed,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    title = "1,240",
                    label = "Kin",
                    icon = Icons.Default.Groups,
                    tint = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Monthly Collective Challenge Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "MAY COLLECTIVE CHALLENGE • 6 DAYS REMAINING",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = challenge.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Goal: Spark ${challenge.goal} acts of unprompted community care this month. Every action and shared ripple counts.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${challenge.progress} / ${challenge.goal} ripples", style = MaterialTheme.typography.labelMedium)
                        Text(text = "$progressPct% Complete", style = MaterialTheme.typography.labelMedium, color = LuminaPrimary, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progressPct / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = LuminaPrimaryContainer,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                }
            }
        }

        // Daily Spark Composer
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = LuminaTertiaryFixed.copy(alpha = 0.45f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Flare, contentDescription = null, tint = LuminaPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Daily Spark — Log Your Ripple",
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "What positive change did you spark today? No ripple is too small to begin an avalanche of good.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Category dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryDropdownExpanded,
                        onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = categories.find { it.first == composeCategory }?.second ?: "Acts of Kindness",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = categoryDropdownExpanded,
                            onDismissRequest = { categoryDropdownExpanded = false }
                        ) {
                            categories.filter { it.first.isNotBlank() }.forEach { (id, label) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        composeCategory = id
                                        categoryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Text area
                    OutlinedTextField(
                        value = composeBody,
                        onValueChange = { composeBody = it },
                        placeholder = { Text("What positive change did you spark today?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("ripple_body_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = LuminaPrimaryContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = composeMantraTag,
                            onValueChange = { composeMantraTag = it },
                            placeholder = { Text("Mantra tag") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                        )
                        OutlinedTextField(
                            value = composeLocation,
                            onValueChange = { composeLocation = it },
                            placeholder = { Text("City (optional)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (composeBody.trim().length >= 10) {
                                database.addRipple(composeCategory, composeBody.trim(), composeMantraTag.trim(), composeLocation.trim())
                                composeBody = ""
                                composeMantraTag = ""
                                composeLocation = ""
                            }
                        },
                        enabled = composeBody.trim().length >= 10,
                        colors = ButtonDefaults.buttonColors(containerColor = LuminaPrimaryContainer, contentColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_ripple_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Log a New Ripple Action (+50 Karma)", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { (id, label) ->
                    val isSelected = selectedFilterCategory == id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.onSurface else Color.White)
                            .clickable { selectedFilterCategory = id }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Feed of Ripples
        items(filteredRipples) { r ->
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(LuminaSecondaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = r.authorName.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = LuminaSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = r.authorName, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = if (r.location.isNotBlank()) "${r.location} • Moments ago" else "Moments ago",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (r.mantraTag.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = LuminaPrimary, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = r.mantraTag, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = r.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Light bulb button
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (r.litByMe) LuminaPrimaryFixed else MaterialTheme.colorScheme.surfaceContainerLow)
                                .clickable { database.toggleLight(r.id) }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Send Light",
                                tint = if (r.litByMe) LuminaPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${r.lights} Lights",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (r.litByMe) LuminaPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (r.litByMe) FontWeight.Bold else FontWeight.Normal
                            )
                        }

                        Row {
                            IconButton(onClick = { shareMantraIntent(context, r.body, r.authorName) }) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (r.isMine) {
                                IconButton(onClick = { database.deleteRipple(r.id) }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }

        // User Footprint & Badges
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
                        Text(text = "Your Footprint", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(LuminaSecondaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Beacon Lvl ${userStats.beaconLevel}", style = MaterialTheme.typography.labelSmall, color = LuminaSecondary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FootprintItem(value = "${userStats.actionsLogged}", label = "Actions logged", modifier = Modifier.weight(1f))
                        FootprintItem(value = "${userStats.lightsReceived}", label = "Lights received", modifier = Modifier.weight(1f))
                        FootprintItem(value = "${userStats.karma}", label = "Karma", modifier = Modifier.weight(1f))
                        FootprintItem(value = "${userStats.streak}", label = "Day streak", modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Earned Badges", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
                    Spacer(modifier = Modifier.height(10.dp))

                    SeedData.badges.forEach { b ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(if (b.earned) LuminaPrimaryFixed else MaterialTheme.colorScheme.surfaceContainerHigh, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (b.earned) Icons.Default.WorkspacePremium else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (b.earned) LuminaPrimary else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = b.name, style = MaterialTheme.typography.labelLarge)
                                Text(text = b.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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

@Composable
private fun StatTile(
    title: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(containerColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
        }
    }
}

@Composable
private fun FootprintItem(value: String, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 9.sp)
        }
    }
}
