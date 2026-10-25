package com.example.lumina.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumina.audio.ActiveTrack
import com.example.lumina.audio.LuminaSoundManager
import com.example.lumina.data.model.Deck
import com.example.lumina.data.model.SeedData
import com.example.lumina.data.storage.LuminaDatabase
import com.example.lumina.ui.components.DeckArtCard
import com.example.lumina.ui.components.shareMantraIntent
import com.example.lumina.ui.theme.LuminaPrimary
import com.example.lumina.ui.theme.LuminaPrimaryContainer
import com.example.lumina.ui.theme.LuminaPrimaryFixed
import com.example.lumina.ui.theme.LuminaSecondary
import com.example.lumina.ui.theme.LuminaSecondaryContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    database: LuminaDatabase,
    soundManager: LuminaSoundManager,
    initialDeckSlug: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookmarks by database.bookmarksFlow.collectAsState()
    val isPlaying by soundManager.isPlaying.collectAsState()
    val activeTrack by soundManager.currentTrack.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var inspectedDeck by remember {
        mutableStateOf(SeedData.decks.find { it.slug == initialDeckSlug })
    }

    val categories = remember {
        listOf("All") + SeedData.decks.map { it.category }.distinct()
    }

    val filteredDecks = remember(searchQuery, selectedCategory) {
        SeedData.decks.filter { deck ->
            val matchesCategory = selectedCategory == null || selectedCategory == "All" || deck.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    deck.title.contains(searchQuery, ignoreCase = true) ||
                    deck.theme.contains(searchQuery, ignoreCase = true) ||
                    deck.category.contains(searchQuery, ignoreCase = true) ||
                    deck.affirmations.any { it.text.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesSearch
        }
    }

    val featuredDeck = remember { SeedData.decks.firstOrNull { it.featured } ?: SeedData.decks[0] }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "• Curated visual artifacts & audio transmissions",
                style = MaterialTheme.typography.labelSmall,
                color = LuminaPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Visual Wisdom & Decks",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Immerse yourself in curated collections of transformative mantras, fine editorial art, and contemplative audio tracks.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search affirmations, emotions, changemakers, or themes…",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gallery_search_input"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    focusedBorderColor = LuminaPrimaryContainer,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Pills
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = (selectedCategory == null && cat == "All") || (selectedCategory == cat)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) LuminaPrimaryContainer else MaterialTheme.colorScheme.surfaceContainerLow)
                            .clickable {
                                selectedCategory = if (cat == "All") null else cat
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Spotlight of the Week (when no query is active)
        if (searchQuery.isBlank() && (selectedCategory == null || selectedCategory == "All")) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = LuminaPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Curator Spotlight of the Week", style = MaterialTheme.typography.labelSmall, color = LuminaPrimary)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = featuredDeck.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = featuredDeck.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Artwork preview container
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(18.dp))
                        ) {
                            DeckArtCard(palette = featuredDeck.palette)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "“${featuredDeck.epigraph}”",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontStyle = FontStyle.Italic,
                                    maxLines = 2
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    soundManager.playTrack(
                                        ActiveTrack(
                                            id = featuredDeck.id,
                                            title = featuredDeck.title,
                                            subtitle = featuredDeck.category,
                                            guide = featuredDeck.guide,
                                            affirmations = featuredDeck.affirmations,
                                            deckSlug = featuredDeck.slug
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = LuminaPrimaryContainer, contentColor = Color.White),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Icon(imageVector = Icons.Default.AutoStories, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Begin Journey", style = MaterialTheme.typography.labelMedium)
                            }

                            Button(
                                onClick = { inspectedDeck = featuredDeck },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow, contentColor = MaterialTheme.colorScheme.onSurface),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "Explore Deck", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }

        // Section header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Curated Archetypes (${filteredDecks.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Serif
                )
            }
        }

        // Decks List
        items(filteredDecks) { deck ->
            val isBookmarked = bookmarks.contains(deck.id)
            val isNowPlaying = activeTrack?.deckSlug == deck.slug && isPlaying

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { inspectedDeck = deck }
            ) {
                Column {
                    // Deck Header Artwork
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    ) {
                        DeckArtCard(palette = deck.palette)

                        // Top right actions
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                        ) {
                            IconButton(
                                onClick = { database.toggleBookmark(deck.id) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.85f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isBookmarked) LuminaPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = { shareMantraIntent(context, deck.epigraph, deck.title) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.85f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Bottom left badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = deck.theme,
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    // Content
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${deck.cardCount} cards • ${deck.audioMinutes}m audio guide • ${deck.guide}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = deck.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "“${deck.epigraph}”",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    soundManager.playTrack(
                                        ActiveTrack(
                                            id = deck.id,
                                            title = deck.title,
                                            subtitle = deck.category,
                                            guide = deck.guide,
                                            affirmations = deck.affirmations,
                                            deckSlug = deck.slug
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = LuminaPrimaryContainer, contentColor = Color.White),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Icon(
                                    imageVector = if (isNowPlaying) Icons.Default.GraphicEq else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isNowPlaying) "Now Playing" else "Play Meditation",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { inspectedDeck = deck }
                            ) {
                                Text(
                                    text = "Explore Deck",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = LuminaPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = LuminaPrimary, modifier = Modifier.size(16.dp))
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

    // Deck Detail Bottom Sheet
    if (inspectedDeck != null) {
        val d = inspectedDeck!!
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { inspectedDeck = null },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(18.dp))
                    ) {
                        DeckArtCard(palette = d.palette)
                        IconButton(
                            onClick = { inspectedDeck = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.White.copy(alpha = 0.85f), CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }

                item {
                    Text(
                        text = "${d.category} • ${d.cardCount} cards • ${d.guide}",
                        style = MaterialTheme.typography.labelSmall,
                        color = LuminaPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = d.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = d.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                soundManager.playTrack(
                                    ActiveTrack(
                                        id = d.id,
                                        title = d.title,
                                        subtitle = d.category,
                                        guide = d.guide,
                                        affirmations = d.affirmations,
                                        deckSlug = d.slug
                                    )
                                )
                                soundManager.setPlayerExpanded(true)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LuminaPrimaryContainer, contentColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Play All Cards", style = MaterialTheme.typography.labelMedium)
                        }

                        Button(
                            onClick = { database.toggleBookmark(d.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow, contentColor = MaterialTheme.colorScheme.onSurface),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (bookmarks.contains(d.id)) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (bookmarks.contains(d.id)) "Saved" else "Save Deck", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                item {
                    Text(
                        text = "CARDS IN THIS COLLECTION",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(d.affirmations) { aff ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "“${aff.text}”",
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily.Serif,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    val idx = d.affirmations.indexOf(aff)
                                    soundManager.playTrack(
                                        ActiveTrack(
                                            id = d.id,
                                            title = d.title,
                                            subtitle = d.category,
                                            guide = d.guide,
                                            affirmations = d.affirmations,
                                            deckSlug = d.slug
                                        ),
                                        startIndex = idx
                                    )
                                }
                            ) {
                                Icon(imageVector = Icons.Default.PlayCircle, contentDescription = "Play card", tint = LuminaPrimary)
                            }
                            IconButton(
                                onClick = { shareMantraIntent(context, aff.text, d.title) }
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
