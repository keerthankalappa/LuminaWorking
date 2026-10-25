package com.example.lumina.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.lumina.audio.LuminaSoundManager
import com.example.lumina.data.storage.LuminaDatabase
import com.example.lumina.ui.components.NowPlayingBottomBar
import com.example.lumina.ui.components.SoundSanctuaryModal
import com.example.lumina.ui.screens.GalleryScreen
import com.example.lumina.ui.screens.RippleScreen
import com.example.lumina.ui.screens.SanctuaryScreen
import com.example.lumina.ui.screens.StudioScreen
import com.example.lumina.ui.theme.LuminaPrimary
import com.example.lumina.ui.theme.LuminaPrimaryContainer

enum class LuminaTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val testTag: String) {
    SANCTUARY("Sanctuary", Icons.Default.Spa, "tab_sanctuary"),
    GALLERY("Gallery", Icons.Default.AutoStories, "tab_gallery"),
    RIPPLE("Ripple", Icons.Default.Waves, "tab_ripple"),
    STUDIO("Studio", Icons.Default.Architecture, "tab_studio")
}

@Composable
fun LuminaApp(
    database: LuminaDatabase,
    soundManager: LuminaSoundManager
) {
    var currentTab by remember { mutableStateOf(LuminaTab.SANCTUARY) }
    var selectedDeckSlug by remember { mutableStateOf<String?>(null) }
    var initialComposeText by remember { mutableStateOf("") }
    var initialStudioDraft by remember { mutableStateOf("") }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Column {
                NowPlayingBottomBar(
                    soundManager = soundManager
                )

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    tonalElevation = 8.dp
                ) {
                    LuminaTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(imageVector = tab.icon, contentDescription = tab.title)
                            },
                            label = {
                                Text(text = tab.title, style = MaterialTheme.typography.labelSmall)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = LuminaPrimary,
                                selectedTextColor = LuminaPrimary,
                                indicatorColor = LuminaPrimaryContainer.copy(alpha = 0.25f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                LuminaTab.SANCTUARY -> {
                    SanctuaryScreen(
                        database = database,
                        soundManager = soundManager,
                        onNavigateToGalleryDeck = { slug ->
                            selectedDeckSlug = slug
                            currentTab = LuminaTab.GALLERY
                        },
                        onNavigateToRippleCompose = { text ->
                            initialComposeText = text
                            currentTab = LuminaTab.RIPPLE
                        }
                    )
                }
                LuminaTab.GALLERY -> {
                    GalleryScreen(
                        database = database,
                        soundManager = soundManager,
                        initialDeckSlug = selectedDeckSlug
                    )
                }
                LuminaTab.RIPPLE -> {
                    RippleScreen(
                        database = database,
                        initialComposeText = initialComposeText
                    )
                }
                LuminaTab.STUDIO -> {
                    StudioScreen(
                        database = database,
                        soundManager = soundManager,
                        initialDraftText = initialStudioDraft
                    )
                }
            }

            // Expanded Sound Sanctuary Dialog / Bottom Sheet
            SoundSanctuaryModal(
                soundManager = soundManager,
                onSaveMantraCard = { text ->
                    database.saveMantra(
                        com.example.lumina.data.model.CustomMantra(
                            id = "",
                            text = text,
                            attribution = "Sanctuary Reflection"
                        )
                    )
                },
                onOpenStudioWithText = { text ->
                    initialStudioDraft = text
                    currentTab = LuminaTab.STUDIO
                }
            )
        }
    }
}
