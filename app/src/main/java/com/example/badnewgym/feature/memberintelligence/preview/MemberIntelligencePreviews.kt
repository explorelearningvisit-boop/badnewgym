package com.example.badnewgym.feature.memberintelligence.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberSummaryCard
import com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios

@Preview(name = "Aman compact member dashboard", widthDp = 393, heightDp = 520)
@Composable
fun AmanMemberSummaryPreview() {
    val (snapshot, _) = MemberScenarios.compactSummaryAman()
    val theme = ThemeId.NATURAL_FRESH
    BADGymTheme(
        colors = theme.colors(),
        shapes = theme.shapes(),
        motion = theme.motion(),
        elevation = theme.elevation()
    ) {
        MemberSummaryCard(snapshot = snapshot)
    }
}
