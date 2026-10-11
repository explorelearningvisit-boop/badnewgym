package com.example.badnewgym.feature.memberintelligence.presentation.v4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.badnewgym.feature.memberintelligence.presentation.home.HomeViewModel
import com.example.badnewgym.feature.memberintelligence.presentation.home.MemberIntelligenceHome

/**
 * Member Intelligence host activity.
 *
 * Single Home implementation lives in presentation.home; this activity is a
 * thin host so existing launch intents keep working.
 */
class MemberIntelligenceV4Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val memberId = intent?.getStringExtra(EXTRA_MEMBER_ID) ?: HomeViewModel.DEFAULT_MEMBER_ID

        setContent {
            MemberIntelligenceHome(memberId = memberId)
        }
    }

    companion object {
        const val EXTRA_MEMBER_ID = "member_id"
        const val EXTRA_PROGRESS_TAB = "start_progress_tab"
        const val DEFAULT_MEMBER_ID = "BG305"
    }
}
