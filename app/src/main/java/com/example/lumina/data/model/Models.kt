package com.example.lumina.data.model

data class Affirmation(
    val id: String,
    val text: String,
    val deckSlug: String = ""
)

data class Deck(
    val id: String,
    val slug: String = "",
    val title: String,
    val category: String,
    val theme: String,
    val description: String,
    val epigraph: String,
    val guide: String,
    val palette: String,
    val audioMinutes: Int,
    val featured: Boolean = false,
    val affirmations: List<Affirmation> = emptyList(),
    val bookmarked: Boolean = false
) {
    val cardCount: Int get() = affirmations.size
}

data class DailyAction(
    val id: String,
    val title: String,
    val description: String,
    val karma: Int,
    val practiced: Boolean = false
)

data class RippleItem(
    val id: String,
    val authorName: String,
    val authorAvatar: String = "",
    val location: String = "",
    val category: String,
    val body: String,
    val mantraTag: String = "",
    val lights: Int = 0,
    val litByMe: Boolean = false,
    val isMine: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class JournalEntry(
    val id: String,
    val mood: String,
    val body: String,
    val rippleAction: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class CustomMantra(
    val id: String,
    val text: String,
    val attribution: String = "",
    val intent: String = "Grounded Calm",
    val palette: String = "dawn-amber",
    val typography: String = "playfair",
    val aura: String = "luminous-glow",
    val microAction: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val earned: Boolean
)

data class UserStats(
    val karma: Int = 165,
    val streak: Int = 3,
    val beaconLevel: Int = 2,
    val actionsLogged: Int = 4,
    val lightsReceived: Int = 18,
    val soundscape: String = "morning-mist",
    val narrationRate: Float = 0.9f
)

data class CommunityChallenge(
    val title: String = "Spring Awakening: 5,000 Ripples of Goodwill",
    val progress: Int = 3420,
    val goal: Int = 5000,
    val endsOn: String = "May 31"
)
