package com.example.lumina.data.storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lumina.data.model.CustomMantra
import com.example.lumina.data.model.JournalEntry
import com.example.lumina.data.model.RippleItem
import com.example.lumina.data.model.SeedData
import com.example.lumina.data.model.UserStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class LuminaDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val _journalFlow = MutableStateFlow<List<JournalEntry>>(emptyList())
    val journalFlow: StateFlow<List<JournalEntry>> = _journalFlow.asStateFlow()

    private val _mantrasFlow = MutableStateFlow<List<CustomMantra>>(emptyList())
    val mantrasFlow: StateFlow<List<CustomMantra>> = _mantrasFlow.asStateFlow()

    private val _bookmarksFlow = MutableStateFlow<Set<String>>(emptySet())
    val bookmarksFlow: StateFlow<Set<String>> = _bookmarksFlow.asStateFlow()

    private val _ripplesFlow = MutableStateFlow<List<RippleItem>>(emptyList())
    val ripplesFlow: StateFlow<List<RippleItem>> = _ripplesFlow.asStateFlow()

    private val _userStatsFlow = MutableStateFlow(UserStats())
    val userStatsFlow: StateFlow<UserStats> = _userStatsFlow.asStateFlow()

    init {
        refreshAll()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE journal (
                id TEXT PRIMARY KEY,
                mood TEXT NOT NULL,
                body TEXT NOT NULL,
                rippleAction TEXT,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE custom_mantras (
                id TEXT PRIMARY KEY,
                text TEXT NOT NULL,
                attribution TEXT,
                intent TEXT,
                palette TEXT,
                typography TEXT,
                aura TEXT,
                microAction TEXT,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE bookmarks (
                targetId TEXT PRIMARY KEY,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE ripples (
                id TEXT PRIMARY KEY,
                authorName TEXT NOT NULL,
                authorAvatar TEXT,
                location TEXT,
                category TEXT NOT NULL,
                body TEXT NOT NULL,
                mantraTag TEXT,
                lights INTEGER NOT NULL DEFAULT 0,
                litByMe INTEGER NOT NULL DEFAULT 0,
                isMine INTEGER NOT NULL DEFAULT 0,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE user_profile (
                id INTEGER PRIMARY KEY,
                karma INTEGER NOT NULL,
                streak INTEGER NOT NULL,
                beaconLevel INTEGER NOT NULL,
                actionsLogged INTEGER NOT NULL,
                lightsReceived INTEGER NOT NULL,
                soundscape TEXT NOT NULL,
                narrationRate REAL NOT NULL
            )
            """.trimIndent()
        )

        // Seed initial ripples
        SeedData.ripples.forEach { r ->
            val cv = ContentValues().apply {
                put("id", r.id)
                put("authorName", r.authorName)
                put("authorAvatar", r.authorAvatar)
                put("location", r.location)
                put("category", r.category)
                put("body", r.body)
                put("mantraTag", r.mantraTag)
                put("lights", r.lights)
                put("litByMe", if (r.litByMe) 1 else 0)
                put("isMine", 0)
                put("createdAt", r.createdAt)
            }
            db.insert("ripples", null, cv)
        }

        // Seed initial user stats
        val initStats = ContentValues().apply {
            put("id", 1)
            put("karma", 165)
            put("streak", 3)
            put("beaconLevel", 2)
            put("actionsLogged", 4)
            put("lightsReceived", 18)
            put("soundscape", "morning-mist")
            put("narrationRate", 0.9f)
        }
        db.insert("user_profile", null, initStats)

        // Seed 1 default custom mantra
        val initMantra = ContentValues().apply {
            put("id", "m-init-1")
            put("text", "I honor the rhythm of my own unfolding; I do not need to rush what is meant to grow with deep roots.")
            put("attribution", "Morning Intention")
            put("intent", "Grounded Calm")
            put("palette", "dawn-amber")
            put("typography", "playfair")
            put("aura", "luminous-glow")
            put("microAction", "Take a 20-minute silent walk without phone and write one kind note.")
            put("createdAt", System.currentTimeMillis())
        }
        db.insert("custom_mantras", null, initMantra)

        // Seed initial journal entries
        val initJournal = ContentValues().apply {
            put("id", "j-init-1")
            put("mood", "grounded")
            put("body", "Felt centered during morning stillness. Offering patience to team members during deadline review.")
            put("rippleAction", "Extend the Light to an Unseen Pillar")
            put("createdAt", System.currentTimeMillis() - 86400000)
        }
        db.insert("journal", null, initJournal)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS journal")
        db.execSQL("DROP TABLE IF EXISTS custom_mantras")
        db.execSQL("DROP TABLE IF EXISTS bookmarks")
        db.execSQL("DROP TABLE IF EXISTS ripples")
        db.execSQL("DROP TABLE IF EXISTS user_profile")
        onCreate(db)
    }

    fun refreshAll() {
        refreshJournal()
        refreshMantras()
        refreshBookmarks()
        refreshRipples()
        refreshUserStats()
    }

    private fun refreshJournal() {
        val list = mutableListOf<JournalEntry>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, mood, body, rippleAction, createdAt FROM journal ORDER BY createdAt DESC", null)
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    JournalEntry(
                        id = it.getString(0),
                        mood = it.getString(1),
                        body = it.getString(2),
                        rippleAction = it.getString(3) ?: "",
                        createdAt = it.getLong(4)
                    )
                )
            }
        }
        _journalFlow.value = list
    }

    fun saveJournal(mood: String, body: String, rippleAction: String = "") {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("id", UUID.randomUUID().toString())
            put("mood", mood)
            put("body", body)
            put("rippleAction", rippleAction)
            put("createdAt", System.currentTimeMillis())
        }
        db.insert("journal", null, cv)
        refreshJournal()
    }

    fun deleteJournal(id: String) {
        val db = writableDatabase
        db.delete("journal", "id = ?", arrayOf(id))
        refreshJournal()
    }

    private fun refreshMantras() {
        val list = mutableListOf<CustomMantra>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, text, attribution, intent, palette, typography, aura, microAction, createdAt FROM custom_mantras ORDER BY createdAt DESC", null)
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    CustomMantra(
                        id = it.getString(0),
                        text = it.getString(1),
                        attribution = it.getString(2) ?: "",
                        intent = it.getString(3) ?: "Grounded Calm",
                        palette = it.getString(4) ?: "dawn-amber",
                        typography = it.getString(5) ?: "playfair",
                        aura = it.getString(6) ?: "luminous-glow",
                        microAction = it.getString(7) ?: "",
                        createdAt = it.getLong(8)
                    )
                )
            }
        }
        _mantrasFlow.value = list
    }

    fun saveMantra(mantra: CustomMantra): String {
        val db = writableDatabase
        val id = if (mantra.id.isBlank()) UUID.randomUUID().toString() else mantra.id
        val cv = ContentValues().apply {
            put("id", id)
            put("text", mantra.text)
            put("attribution", mantra.attribution)
            put("intent", mantra.intent)
            put("palette", mantra.palette)
            put("typography", mantra.typography)
            put("aura", mantra.aura)
            put("microAction", mantra.microAction)
            put("createdAt", mantra.createdAt)
        }
        db.insertWithOnConflict("custom_mantras", null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        refreshMantras()
        return id
    }

    fun deleteMantra(id: String) {
        val db = writableDatabase
        db.delete("custom_mantras", "id = ?", arrayOf(id))
        refreshMantras()
    }

    private fun refreshBookmarks() {
        val set = mutableSetOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT targetId FROM bookmarks", null)
        cursor.use {
            while (it.moveToNext()) {
                set.add(it.getString(0))
            }
        }
        _bookmarksFlow.value = set
    }

    fun toggleBookmark(targetId: String): Boolean {
        val db = writableDatabase
        val current = _bookmarksFlow.value.contains(targetId)
        if (current) {
            db.delete("bookmarks", "targetId = ?", arrayOf(targetId))
        } else {
            val cv = ContentValues().apply {
                put("targetId", targetId)
                put("createdAt", System.currentTimeMillis())
            }
            db.insertWithOnConflict("bookmarks", null, cv, SQLiteDatabase.CONFLICT_REPLACE)
        }
        refreshBookmarks()
        return !current
    }

    private fun refreshRipples() {
        val list = mutableListOf<RippleItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, authorName, authorAvatar, location, category, body, mantraTag, lights, litByMe, isMine, createdAt FROM ripples ORDER BY createdAt DESC", null)
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    RippleItem(
                        id = it.getString(0),
                        authorName = it.getString(1),
                        authorAvatar = it.getString(2) ?: "",
                        location = it.getString(3) ?: "",
                        category = it.getString(4),
                        body = it.getString(5),
                        mantraTag = it.getString(6) ?: "",
                        lights = it.getInt(7),
                        litByMe = it.getInt(8) == 1,
                        isMine = it.getInt(9) == 1,
                        createdAt = it.getLong(10)
                    )
                )
            }
        }
        _ripplesFlow.value = list
    }

    fun addRipple(category: String, body: String, mantraTag: String, location: String): RippleItem {
        val db = writableDatabase
        val id = UUID.randomUUID().toString()
        val item = RippleItem(
            id = id,
            authorName = "You",
            authorAvatar = "",
            location = location,
            category = category,
            body = body,
            mantraTag = mantraTag,
            lights = 1,
            litByMe = false,
            isMine = true,
            createdAt = System.currentTimeMillis()
        )
        val cv = ContentValues().apply {
            put("id", item.id)
            put("authorName", item.authorName)
            put("authorAvatar", item.authorAvatar)
            put("location", item.location)
            put("category", item.category)
            put("body", item.body)
            put("mantraTag", item.mantraTag)
            put("lights", item.lights)
            put("litByMe", 0)
            put("isMine", 1)
            put("createdAt", item.createdAt)
        }
        db.insert("ripples", null, cv)
        // Add karma and logged count
        incrementKarma(50, 1)
        refreshRipples()
        return item
    }

    fun toggleLight(id: String) {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT lights, litByMe FROM ripples WHERE id = ?", arrayOf(id))
        var lights = 0
        var litByMe = 0
        cursor.use {
            if (it.moveToFirst()) {
                lights = it.getInt(0)
                litByMe = it.getInt(1)
            }
        }
        val newLit = if (litByMe == 1) 0 else 1
        val newLights = if (newLit == 1) lights + 1 else maxOf(0, lights - 1)
        val cv = ContentValues().apply {
            put("lights", newLights)
            put("litByMe", newLit)
        }
        db.update("ripples", cv, "id = ?", arrayOf(id))
        refreshRipples()
    }

    fun deleteRipple(id: String) {
        val db = writableDatabase
        db.delete("ripples", "id = ?", arrayOf(id))
        refreshRipples()
    }

    private fun refreshUserStats() {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT karma, streak, beaconLevel, actionsLogged, lightsReceived, soundscape, narrationRate FROM user_profile WHERE id = 1", null)
        cursor.use {
            if (it.moveToFirst()) {
                _userStatsFlow.value = UserStats(
                    karma = it.getInt(0),
                    streak = it.getInt(1),
                    beaconLevel = it.getInt(2),
                    actionsLogged = it.getInt(3),
                    lightsReceived = it.getInt(4),
                    soundscape = it.getString(5),
                    narrationRate = it.getFloat(6)
                )
            }
        }
    }

    fun practiceAction(karmaGain: Int) {
        val current = _userStatsFlow.value
        val newKarma = current.karma + karmaGain
        val newStreak = current.streak + 1
        val newBeacon = (newKarma / 100) + 1
        val newLogged = current.actionsLogged + 1
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("karma", newKarma)
            put("streak", newStreak)
            put("beaconLevel", newBeacon)
            put("actionsLogged", newLogged)
        }
        db.update("user_profile", cv, "id = 1", null)
        refreshUserStats()
    }

    fun updatePreferences(soundscape: String, narrationRate: Float) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("soundscape", soundscape)
            put("narrationRate", narrationRate)
        }
        db.update("user_profile", cv, "id = 1", null)
        refreshUserStats()
    }

    private fun incrementKarma(karmaGain: Int, actionsGain: Int) {
        val current = _userStatsFlow.value
        val newKarma = current.karma + karmaGain
        val newLogged = current.actionsLogged + actionsGain
        val newBeacon = (newKarma / 100) + 1
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("karma", newKarma)
            put("beaconLevel", newBeacon)
            put("actionsLogged", newLogged)
        }
        db.update("user_profile", cv, "id = 1", null)
        refreshUserStats()
    }

    companion object {
        private const val DATABASE_NAME = "lumina.db"
        private const val DATABASE_VERSION = 1

        @Volatile
        private var instance: LuminaDatabase? = null

        fun getInstance(context: Context): LuminaDatabase {
            return instance ?: synchronized(this) {
                instance ?: LuminaDatabase(context.applicationContext).also { instance = it }
            }
        }
    }
}
