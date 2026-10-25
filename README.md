# Lumina — Mindset to Impact (Android)

Lumina is an editorial wellness application built natively for Android using Kotlin and Jetpack Compose. It turns calm contemplative presence into real-world kindness through daily spoken affirmations, procedural ambient soundscapes, themed wisdom decks, a printable mantra art studio, and a community ripple feed.

## Architecture & Framework

- **Framework**: Modern Android (Kotlin + Jetpack Compose)
- **Design System**: Material Design 3 (M3) with Lumina Editorial Wellness design tokens
- **Persistence**: Local SQLite storage via Coroutines Flow and custom database helper
- **Audio Engine**: Native `AudioTrack` procedural sound synthesis (Morning Mist, Singing Bowls, Temple Chimes, Pine Forest Rain, Binaural 432Hz) & native `TextToSpeech` unhurried spoken narration
- **Adaptive Launcher Icon**: Custom generated adaptive icon with safe-zone layer list and circular launcher mipmaps

## Screens & Core Features

| Screen | Core Capabilities |
| --- | --- |
| **🌿 Sanctuary** | Rotating daily focal mantra narrated with real-time waveform, interactive 4-4-4 breathing ring pacer, daily action in the world with karma rewards, private mood check-in (Grounded, Expansive, Gentle, Reflective, Determined), live community resonance ticker. |
| **📚 Gallery** | Searchable wisdom decks, curator spotlight of the week, generative canvas cover art, bookmarking, full deck detail sheet with individual audio tracks. |
| **🌊 Ripple** | Community kindness feed, collective monthly challenge tracker with live progress bar, "Daily Spark" action composer, Send Lightbulb reactions, footprint stats (Karma, Beacon Level, Streak), and unlockable badges. |
| **🎨 Studio** | 3-step mantra builder: Inscription & sub-attribution with inspirational epigraph drawer, 5 color palettes (Dawn Amber, Eucalyptus, Twilight, Solstice, Midnight), editorial typography, 4 ambient aura patterns (Luminous Glow, Petal Waves, Starlight, Raw Parchment), live preview canvas, 30-day emotional rhythm matrix, and private journal reflections. |
| **🎧 Sound Sanctuary Player** | Persistent bottom player & expanded bottom sheet modal with 432Hz badge, dual volume immersion mixer (Voice Narration & Ambient Soundscape), atmosphere pills, card progression list with timestamps, and sleep timer (5m, 15m, 30m). |

## Build & Run

- **JDK**: Java 21
- **Gradle**: 9.3.1
- **Android SDK**: CompileSdk 36, MinSdk 26
- **Build task**: `gradle assembleDebug` (outputs to `app/build/outputs/apk/debug/app-debug.apk`)
