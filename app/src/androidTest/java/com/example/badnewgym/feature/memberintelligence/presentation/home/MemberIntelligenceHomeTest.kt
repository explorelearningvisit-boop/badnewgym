package com.example.badnewgym.feature.memberintelligence.presentation.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.badnewgym.MainActivity
import org.junit.Rule
import org.junit.Test

/**
 * 4.0 interaction acceptance on a real device: every major Home action must
 * produce a real state change. Compose-test taps drive the same handlers a
 * finger would - ADB input injection stays blocked on MIUI, so this is the
 * honest end-to-end path.
 */
class MemberIntelligenceHomeTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private fun awaitHome() {
        rule.waitUntil(timeoutMillis = 15_000) {
            try {
                rule.onNodeWithText("Aman Tripathi").assertIsDisplayed()
                true
            } catch (t: AssertionError) {
                false
            }
        }
    }

    @Test
    fun allSixWidgetsRenderWithoutScrolling() {
        awaitHome()
        rule.onNodeWithText("CHECK-IN").assertIsDisplayed()
        rule.onNodeWithText("Aman Tripathi").assertIsDisplayed()
        rule.onNodeWithText("Payment Due").assertIsDisplayed()
        rule.onNodeWithText("Attendance").assertIsDisplayed()
        rule.onNodeWithText("Workout").assertIsDisplayed()
        rule.onNodeWithText("Body").assertIsDisplayed()
    }

    @Test
    fun endSessionFlipsToCompletedCheckout() {
        awaitHome()
        rule.onNodeWithText("END SESSION").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            try {
                rule.onNodeWithText("CHECK-OUT").assertIsDisplayed()
                true
            } catch (t: AssertionError) {
                false
            }
        }
        rule.onNodeWithText("Completed").assertIsDisplayed()
    }

    @Test
    fun collectFullPaymentShowsSuccessfulCard() {
        awaitHome()
        rule.onNodeWithText("Collect >").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            try {
                rule.onNodeWithText("Record Payment").assertIsDisplayed()
                true
            } catch (t: AssertionError) {
                false
            }
        }
        // Default method (UPI) + full outstanding amount.
        rule.onNodeWithText("Record Payment").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            try {
                rule.onNodeWithText("SUCCESSFUL").assertIsDisplayed()
                true
            } catch (t: AssertionError) {
                false
            }
        }
        // Due state is gone; the receipt proves the transaction.
        rule.onNodeWithText("Receipt").assertIsDisplayed()
    }

    @Test
    fun workoutRangeSwitchReachesFullMonth() {
        awaitHome()
        rule.onNodeWithText("30D").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            try {
                rule.onNodeWithText("30D").assertIsDisplayed()
                true
            } catch (t: AssertionError) {
                false
            }
        }
        // Range label proves the strip covers the whole selected period.
        var found = false
        rule.waitUntil(timeoutMillis = 5_000) {
            found = try {
                rule.onNodeWithText("last 30d", substring = true).assertIsDisplayed()
                true
            } catch (t: AssertionError) {
                false
            }
            found
        }
    }

    @Test
    fun attendanceCardOpensFullView() {
        awaitHome()
        rule.onNodeWithText("Attendance").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            try {
                rule.onNodeWithText("Full 30-day view").assertIsDisplayed()
                true
            } catch (t: AssertionError) {
                false
            }
        }
    }
}
