package com.example.lumina.data.model

object SeedData {

    val decks: List<Deck> = listOf(
        Deck(
            id = "deck-1",
            slug = "changemakers-inner-sanctuary",
            title = "The Changemaker’s Inner Sanctuary",
            category = "Global Changemakers",
            theme = "Inner Sanctuary",
            description = "Restorative mantras and audio meditations for people doing the long, patient work of changing the world — written for burnout, doubt and fierce hope.",
            epigraph = "In the center of action, I carry stillness like a river.",
            guide = "Dr. Elena Vance",
            palette = "earth-dawn",
            audioMinutes = 14,
            featured = true,
            affirmations = listOf(
                Affirmation("aff-101", "In the center of action, I carry stillness like a river.", "changemakers-inner-sanctuary"),
                Affirmation("aff-102", "My rest is not a retreat from the work; it is how the work endures.", "changemakers-inner-sanctuary"),
                Affirmation("aff-103", "I can hold the weight of the world gently, one task at a time.", "changemakers-inner-sanctuary"),
                Affirmation("aff-104", "Small faithful acts, repeated, are how mountains move.", "changemakers-inner-sanctuary"),
                Affirmation("aff-105", "I release outcomes I cannot control and tend the ground I can.", "changemakers-inner-sanctuary"),
                Affirmation("aff-106", "My hope is a discipline, and today I practise it.", "changemakers-inner-sanctuary"),
                Affirmation("aff-107", "I am one of many hands, and that is enough.", "changemakers-inner-sanctuary")
            )
        ),
        Deck(
            id = "deck-2",
            slug = "radical-empathy",
            title = "Radical Empathy & Listening",
            category = "Radical Compassion",
            theme = "Compassion",
            description = "Practices for listening without defending, and meeting others in their whole humanity.",
            epigraph = "I do not listen to defeat another; I listen to discover the sacred wholeness in our shared struggle.",
            guide = "Maya Lin",
            palette = "sand-gold",
            audioMinutes = 4,
            affirmations = listOf(
                Affirmation("aff-201", "I do not listen to defeat another; I listen to discover the sacred wholeness in our shared struggle.", "radical-empathy"),
                Affirmation("aff-202", "I cultivate unwavering peace within my core, knowing that every act of patient compassion creates ripples.", "radical-empathy"),
                Affirmation("aff-203", "I can disagree with an idea and still honour the person who holds it.", "radical-empathy"),
                Affirmation("aff-204", "Curiosity softens what certainty would harden.", "radical-empathy"),
                Affirmation("aff-205", "Every face I meet today carries a story I have not heard.", "radical-empathy"),
                Affirmation("aff-206", "My kindness is not weakness; it is courage made visible.", "radical-empathy")
            )
        ),
        Deck(
            id = "deck-3",
            slug = "creative-fearlessness",
            title = "Creative Fearlessness",
            category = "Creative Resilience",
            theme = "Creativity",
            description = "Permission to make imperfect, honest things — and to share them.",
            epigraph = "Perfection is an illusion of control; my honest, imperfect offering is what the world truly thirsts for.",
            guide = "Dr. Elena Vance",
            palette = "sunburst",
            audioMinutes = 3,
            affirmations = listOf(
                Affirmation("aff-301", "Perfection is an illusion of control; my honest, imperfect offering is what the world truly thirsts for.", "creative-fearlessness"),
                Affirmation("aff-302", "I make room for the first clumsy draft; it is the doorway, not the destination.", "creative-fearlessness"),
                Affirmation("aff-303", "My voice is needed precisely because it is mine.", "creative-fearlessness"),
                Affirmation("aff-304", "I trade the fear of being seen for the joy of being useful.", "creative-fearlessness"),
                Affirmation("aff-305", "Play is a serious form of courage.", "creative-fearlessness"),
                Affirmation("aff-306", "I begin before I feel ready, and readiness follows.", "creative-fearlessness")
            )
        ),
        Deck(
            id = "deck-4",
            slug = "ecosystem-care",
            title = "Ecosystem Care & Sacred Earth",
            category = "Eco-Stewardship & Earth",
            theme = "Earth Healing",
            description = "Affirmations of belonging to the living world, and tending it in return.",
            epigraph = "I belong to this living earth. In tending to soil, leaf, and neighbor, I restore my own wild soul.",
            guide = "Tariq Vance",
            palette = "forest",
            audioMinutes = 5,
            affirmations = listOf(
                Affirmation("aff-401", "I belong to this living earth. In tending to soil, leaf, and neighbor, I restore my own wild soul.", "ecosystem-care"),
                Affirmation("aff-402", "The trees do not hurry, yet everything is accomplished.", "ecosystem-care"),
                Affirmation("aff-403", "I take only what I need and return what I can.", "ecosystem-care"),
                Affirmation("aff-404", "Every seed I plant is a letter to a future I will not see.", "ecosystem-care"),
                Affirmation("aff-405", "I breathe with the forest: receiving, releasing, renewing.", "ecosystem-care"),
                Affirmation("aff-406", "Caring for this place is caring for everyone who will ever love it.", "ecosystem-care")
            )
        ),
        Deck(
            id = "deck-5",
            slug = "stillness-in-the-storm",
            title = "Stillness in the Storm",
            category = "Overcoming Anxiety",
            theme = "Resilience",
            description = "Grounding words for anxious seasons — anchors for when the water is rough.",
            epigraph = "Between stimulus and response lies my quiet sanctuary of sovereign choice.",
            guide = "Kavita Chen",
            palette = "lake-dusk",
            audioMinutes = 6,
            affirmations = listOf(
                Affirmation("aff-501", "Between stimulus and response lies my quiet sanctuary of sovereign choice.", "stillness-in-the-storm"),
                Affirmation("aff-502", "This feeling is a wave, and I am the ocean that holds it.", "stillness-in-the-storm"),
                Affirmation("aff-503", "I can do hard things slowly.", "stillness-in-the-storm"),
                Affirmation("aff-504", "My breath is an anchor I carry everywhere.", "stillness-in-the-storm"),
                Affirmation("aff-505", "I am safe in this moment, and this moment is where I live.", "stillness-in-the-storm"),
                Affirmation("aff-506", "I let my thoughts pass like weather; I remain the sky.", "stillness-in-the-storm")
            )
        ),
        Deck(
            id = "deck-6",
            slug = "generosity-of-spirit",
            title = "Generosity of Spirit",
            category = "Radical Compassion",
            theme = "Abundance",
            description = "On giving freely, receiving gracefully, and the quiet economics of grace.",
            epigraph = "There is no scarcity in grace. The more compassion I sow into uncultivated ground, the richer life grows.",
            guide = "Maya Lin",
            palette = "peach-bloom",
            audioMinutes = 4,
            affirmations = listOf(
                Affirmation("aff-601", "There is no scarcity in grace. The more compassion I sow into uncultivated ground, the richer life grows.", "generosity-of-spirit"),
                Affirmation("aff-602", "What I give with an open hand returns in ways I cannot predict.", "generosity-of-spirit"),
                Affirmation("aff-603", "I celebrate another’s light without dimming my own.", "generosity-of-spirit"),
                Affirmation("aff-604", "Receiving is also a gift I offer to the giver.", "generosity-of-spirit"),
                Affirmation("aff-605", "I have enough, I do enough, I am enough.", "generosity-of-spirit"),
                Affirmation("aff-606", "Generosity begins with the attention I give.", "generosity-of-spirit")
            )
        ),
        Deck(
            id = "deck-7",
            slug = "courageous-boundaries",
            title = "Courageous Boundaries",
            category = "Mindful Leadership",
            theme = "Inner Peace",
            description = "Holding clear, tender limits that protect what matters most.",
            epigraph = "My clear, tender boundary is not a wall of division, but a garden gate keeping sacred things alive.",
            guide = "Tariq Vance",
            palette = "stone-mist",
            audioMinutes = 3,
            affirmations = listOf(
                Affirmation("aff-701", "My clear, tender boundary is not a wall of division, but a garden gate keeping sacred things alive.", "courageous-boundaries"),
                Affirmation("aff-702", "No is a complete sentence, and I can say it kindly.", "courageous-boundaries"),
                Affirmation("aff-703", "Protecting my energy is how I keep showing up for others.", "courageous-boundaries"),
                Affirmation("aff-704", "I lead best from a full cup, not an empty one.", "courageous-boundaries"),
                Affirmation("aff-705", "I honour the rhythm of my own unfolding; I do not need to rush what is meant to grow with deep roots.", "courageous-boundaries"),
                Affirmation("aff-706", "I am allowed to change my mind as I learn.", "courageous-boundaries")
            )
        )
    )

    val actions: List<DailyAction> = listOf(
        DailyAction(
            id = "unseen-pillar",
            title = "Extend the Light to an Unseen Pillar",
            description = "Send a handwritten or heartfelt note of genuine gratitude to someone whose quiet, steady work makes your daily life possible — a postal worker, colleague, maintenance staff, or quiet friend.",
            karma = 40
        ),
        DailyAction(
            id = "silent-walk",
            title = "Walk Without a Screen",
            description = "Take a 20-minute silent walk without your phone. Notice three living things and write one kind note when you return.",
            karma = 30
        ),
        DailyAction(
            id = "listen-fully",
            title = "Listen Without Fixing",
            description = "In one conversation today, listen fully without offering advice. Ask one more question than you normally would.",
            karma = 35
        ),
        DailyAction(
            id = "earth-tending",
            title = "Tend a Small Patch of Earth",
            description = "Pick up litter on your street, water a neglected plant, or plant a seed. Leave one place better than you found it.",
            karma = 45
        ),
        DailyAction(
            id = "mentor-moment",
            title = "Share What You Know",
            description = "Spend fifteen minutes helping someone learn something you know well — a skill, a shortcut, or a lesson learned the hard way.",
            karma = 50
        ),
        DailyAction(
            id = "repair-bridge",
            title = "Mend a Small Bridge",
            description = "Reach out to someone you have drifted from or had friction with. A short, sincere message is enough.",
            karma = 45
        ),
        DailyAction(
            id = "anonymous-gift",
            title = "Give Anonymously",
            description = "Do one kind thing today that no one will ever trace back to you.",
            karma = 40
        )
    )

    val ripples: List<RippleItem> = listOf(
        RippleItem(
            id = "rip-1",
            authorName = "Anja Lind",
            authorAvatar = "",
            location = "Copenhagen, Denmark",
            category = "kindness",
            body = "Organized our apartment building’s first shared winter coat and pantry swap. We gathered 64 warm coats for the community shelter and shared hot spiced cider in the courtyard.",
            mantraTag = "Radical Empathy",
            lights = 42,
            litByMe = false,
            createdAt = System.currentTimeMillis() - 7200000
        ),
        RippleItem(
            id = "rip-2",
            authorName = "Thabo Mbeki",
            authorAvatar = "",
            location = "Cape Town, South Africa",
            category = "mentorship",
            body = "Started a free weekly breathwork and mindfulness circle for emergency room nursing staff at our regional hospital. Ten caregivers took their first mindful breath pause of the week.",
            mantraTag = "Courageous Beginnings",
            lights = 67,
            litByMe = true,
            createdAt = System.currentTimeMillis() - 18000000
        ),
        RippleItem(
            id = "rip-3",
            authorName = "Ren Tanaka",
            authorAvatar = "",
            location = "Kyoto, Japan",
            category = "environment",
            body = "Planted 25 native flowering shrubs along our neighborhood riverbank with local school children to support native honeybees and stabilize seasonal embankment soils.",
            mantraTag = "Sacred Earth Stewardship",
            lights = 89,
            litByMe = false,
            createdAt = System.currentTimeMillis() - 36000000
        ),
        RippleItem(
            id = "rip-4",
            authorName = "Elena Rostova",
            authorAvatar = "",
            location = "Vienna, Austria",
            category = "local",
            body = "Left three sealed handwritten notes containing words of encouragement and transit passes in the central subway station for commuters having difficult mornings.",
            mantraTag = "Unseen Compassion",
            lights = 31,
            litByMe = false,
            createdAt = System.currentTimeMillis() - 86400000
        )
    )

    val badges: List<Badge> = listOf(
        Badge("b-1", "Dawn Practitioner", "Completed your first focal mantra and reflection.", true),
        Badge("b-2", "Radiant Ripple", "Shared a verified community kindness action.", true),
        Badge("b-3", "7-Day Luminary", "Practiced continuous presence for 7 consecutive days.", false),
        Badge("b-4", "Earth Guardian", "Logged 3 environmental or sacred nature restorations.", true),
        Badge("b-5", "Deep Listener", "Practiced empathetic listening without fixing.", false),
        Badge("b-6", "Beacon of Grace", "Achieved Beacon Level 5 in community resonance.", false)
    )
}
