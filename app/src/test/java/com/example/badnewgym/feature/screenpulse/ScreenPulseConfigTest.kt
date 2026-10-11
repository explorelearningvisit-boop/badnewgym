package com.example.badnewgym.feature.screenpulse

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Filename and tagging contracts.
 *
 * The laptop tool filters and prunes purely on filenames, so the format is a
 * cross-device contract: epoch-ms zero-padded to 13 digits (lexicographic
 * order == chronological order) plus a sanitised tag.
 */
class ScreenPulseConfigTest {

    @Test
    fun frameNameIsZeroPaddedAndSortsChronologically() {
        val a = ScreenPulseConfig.frameName(1_700_000_000_000L, "wa")
        val b = ScreenPulseConfig.frameName(1_700_000_002_000L, "wa")
        assertEquals("sp_1700000000000_wa.jpg", a)
        assertTrue("older name must sort first", a < b)
    }

    @Test
    fun frameNamePadsShortEpochsTo13Digits() {
        val name = ScreenPulseConfig.frameName(123L, "wa")
        assertTrue(name.matches(Regex("^sp_\\d{13}_wa\\.jpg$")))
    }

    @Test
    fun sanitizeLowercasesStripsIllegalAndCapsLength() {
        assertEquals("comexample", ScreenPulseConfig.sanitize("Com.Example!!"))
        assertEquals(16, ScreenPulseConfig.sanitize("abcdefghijklmnopqrstuvwxyz").length)
        assertEquals(ScreenPulseConfig.TAG_UNKNOWN, ScreenPulseConfig.sanitize("!!!"))
    }

    @Test
    fun tagForCoversWhatsappVariants() {
        assertEquals("wa", ScreenPulseConfig.tagFor("com.whatsapp"))
        assertEquals("wab", ScreenPulseConfig.tagFor("com.whatsapp.w4b"))
        assertEquals("wa", ScreenPulseConfig.tagFor("com.whatsapp.something"))
        assertEquals(ScreenPulseConfig.TAG_UNKNOWN, ScreenPulseConfig.tagFor(null))
        assertEquals(ScreenPulseConfig.TAG_UNKNOWN, ScreenPulseConfig.tagFor("  "))
    }

    @Test
    fun tagForUnknownPackageUsesLastSegment() {
        assertEquals("chrome", ScreenPulseConfig.tagFor("com.android.chrome"))
    }

    @Test
    fun isWhatsappFrameMatchesOnlyWaTags() {
        assertTrue(ScreenPulseConfig.isWhatsappFrame("sp_1700000000000_wa.jpg"))
        assertTrue(ScreenPulseConfig.isWhatsappFrame("sp_1700000000000_wab.jpg"))
        assertFalse(ScreenPulseConfig.isWhatsappFrame("sp_1700000000000_unk.jpg"))
        assertFalse(ScreenPulseConfig.isWhatsappFrame("sp_1700000000000_chrome.jpg"))
    }

    @Test
    fun byteBudgetIsOrdered() {
        assertTrue(ScreenPulseConfig.MIN_FRAME_BYTES <= ScreenPulseConfig.TARGET_BYTES)
        assertTrue(ScreenPulseConfig.TARGET_BYTES <= ScreenPulseConfig.MAX_FRAME_BYTES)
    }
}
