package com.example.badnewgym.feature.screenpulse

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

/**
 * Vault contracts, exercised against a temp directory.
 *
 * The vault takes its root as a parameter precisely so these tests never
 * touch a real device or the app sandbox.
 */
class ScreenPulseVaultTest {

    private lateinit var root: File

    @Before
    fun setUp() {
        root = Files.createTempDirectory("sp_vault_test").toFile()
    }

    @After
    fun tearDown() {
        root.deleteRecursively()
    }

    @Test
    fun ensureCreatesDirAndNomedia() {
        val d = ScreenPulseVault.ensure(root)
        assertTrue(d.isDirectory)
        assertTrue(File(d, ScreenPulseConfig.NOMEDIA_NAME).exists())
    }

    @Test
    fun tokenIsStableAcrossCalls() {
        val first = ScreenPulseVault.token(root)
        val second = ScreenPulseVault.token(root)
        assertTrue(first.isNotEmpty())
        assertEquals(first, second)
    }

    @Test
    fun saveAndFramesAreOldestFirst() {
        val older = ScreenPulseConfig.frameName(1_700_000_000_000L, "wa")
        val newer = ScreenPulseConfig.frameName(1_700_000_002_000L, "wa")
        // Saved out of order on purpose: listing must still be chronological.
        assertNotNull(ScreenPulseVault.save(root, newer, byteArrayOf(2)))
        assertNotNull(ScreenPulseVault.save(root, older, byteArrayOf(1)))
        val names = ScreenPulseVault.frames(root).map { it.name }
        assertEquals(listOf(older, newer), names)
    }

    @Test
    fun sha256MatchesKnownVector() {
        val hex = ScreenPulseVault.sha256Hex("abc".toByteArray())
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", hex)
    }

    @Test
    fun manifestRoundTripsEveryFrame() {
        val names = listOf(
            ScreenPulseConfig.frameName(1_700_000_000_000L, "wa"),
            ScreenPulseConfig.frameName(1_700_000_001_000L, "unk")
        )
        names.forEach { ScreenPulseVault.save(root, it, it.toByteArray()) }
        assertNotNull(ScreenPulseVault.writeManifest(root))
        val entries = ScreenPulseVault.readManifest(root)
        assertEquals(names.sorted(), entries.map { it.second }.sorted())
        entries.forEach { (hash, name) ->
            assertEquals(ScreenPulseVault.sha256Hex(name.toByteArray()), hash)
        }
    }

    @Test
    fun pruneDropsOldestFirstWhenOverCount() {
        repeat(5) { i ->
            ScreenPulseVault.save(root, ScreenPulseConfig.frameName(1_700_000_000_000L + i * 1000, "wa"), ByteArray(10))
        }
        val removed = ScreenPulseVault.prune(root, maxFiles = 3, maxBytes = Long.MAX_VALUE, maxAgeMs = Long.MAX_VALUE)
        assertEquals(2, removed)
        val remaining = ScreenPulseVault.frames(root).map { it.name }
        assertEquals(3, remaining.size)
        assertTrue(remaining.none { it.contains("1700000000000") })
    }

    @Test
    fun pruneKeepsTokenAndNomedia() {
        ScreenPulseVault.token(root)
        ScreenPulseVault.save(root, ScreenPulseConfig.frameName(1_700_000_000_000L, "wa"), ByteArray(10))
        ScreenPulseVault.prune(root, maxFiles = 0, maxBytes = 0, maxAgeMs = 0)
        assertTrue(ScreenPulseVault.token(root).isNotEmpty())
        assertTrue(File(ScreenPulseVault.dir(root), ScreenPulseConfig.NOMEDIA_NAME).exists())
    }

    @Test
    fun purgeRemovesFramesButKeepsTokenByDefault() {
        val token = ScreenPulseVault.token(root)
        ScreenPulseVault.save(root, ScreenPulseConfig.frameName(1_700_000_000_000L, "wa"), ByteArray(100))
        ScreenPulseVault.writeManifest(root)
        val erased = ScreenPulseVault.purge(root)
        assertTrue("purge must report success, was $erased", erased >= 0)
        assertEquals(0, ScreenPulseVault.frames(root).size)
        assertEquals(token, ScreenPulseVault.token(root))
    }

    @Test
    fun purgeFullWipeDropsTokenToo() {
        ScreenPulseVault.token(root)
        ScreenPulseVault.save(root, ScreenPulseConfig.frameName(1_700_000_000_000L, "wa"), ByteArray(100))
        ScreenPulseVault.purge(root, keepToken = false)
        assertEquals(0, ScreenPulseVault.frames(root).size)
        assertTrue(
            File(ScreenPulseVault.dir(root), ScreenPulseConfig.TOKEN_NAME).exists().not()
        )
    }

    @Test
    fun statsReflectSavedFrames() {
        ScreenPulseVault.save(root, ScreenPulseConfig.frameName(1_700_000_000_000L, "wa"), ByteArray(100))
        ScreenPulseVault.save(root, ScreenPulseConfig.frameName(1_700_000_001_000L, "wa"), ByteArray(50))
        val s = ScreenPulseVault.stats(root)
        assertEquals(2, s.frames)
        assertEquals(150L, s.bytes)
        assertTrue(s.oldest <= s.newest)
    }
}
