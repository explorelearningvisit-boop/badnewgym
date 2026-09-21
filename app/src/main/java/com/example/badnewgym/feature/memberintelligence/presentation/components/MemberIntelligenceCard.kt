package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberMenu
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberSnapshot
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType

@Composable
fun MemberIntelligenceCard(
    snapshot: MemberSnapshot,
    theme: ThemeId,
    menus: List<MemberMenu>,
    activeMenu: MenuType,
    onMenuSelected: (MenuType) -> Unit,
    onCtaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors

    // Card Outer Frame with Theme Specific Borders and Styling
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (theme == ThemeId.PREMIUM_3D || theme == ThemeId.BEAST_MODE || theme == ThemeId.FUTURISTIC_NEON) 16.dp else 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = colors.cardGlow,
                ambientColor = colors.cardGlow
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                when (theme) {
                    ThemeId.PREMIUM_3D -> Brush.verticalGradient(listOf(Color(0xFF14110A), Color(0xFF090704)))
                    ThemeId.BEAST_MODE -> Brush.verticalGradient(listOf(Color(0xFF1A0609), Color(0xFF0A0203)))
                    ThemeId.PURPLE_ROYAL -> Brush.verticalGradient(listOf(Color(0xFF1E0E38), Color(0xFF0D061A)))
                    ThemeId.FUTURISTIC_NEON -> Brush.verticalGradient(listOf(Color(0xFF091224), Color(0xFF030712)))
                    ThemeId.VIBRANT_GRADIENT -> Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF5FF)))
                    ThemeId.GLASSMORPHISM -> Brush.verticalGradient(listOf(Color(0xE6FFFFFF), Color(0xCCF0F9FF)))
                    ThemeId.MINIMAL_DARK -> Brush.verticalGradient(listOf(Color(0xFF171A20), Color(0xFF0F1115)))
                    ThemeId.NATURAL_FRESH -> Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF0FDF4)))
                }
            )
            .border(
                width = when (theme) {
                    ThemeId.PREMIUM_3D -> 2.dp
                    ThemeId.BEAST_MODE -> 1.5.dp
                    ThemeId.FUTURISTIC_NEON -> 1.5.dp
                    ThemeId.PURPLE_ROYAL -> 1.5.dp
                    ThemeId.GLASSMORPHISM -> 1.dp
                    else -> 1.dp
                },
                brush = when (theme) {
                    ThemeId.PREMIUM_3D -> Brush.linearGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFB8860B), Color(0xFFFFE57F), Color(0xFF8B6914))
                    )
                    ThemeId.BEAST_MODE -> Brush.linearGradient(
                        listOf(Color(0xFFFF1E27), Color(0xFF7F1D1D), Color(0xFFFF4D5E))
                    )
                    ThemeId.PURPLE_ROYAL -> Brush.linearGradient(
                        listOf(Color(0xFFA855F7), Color(0xFF581C87), Color(0xFFC084FC))
                    )
                    ThemeId.FUTURISTIC_NEON -> Brush.linearGradient(
                        listOf(Color(0xFF00E5FF), Color(0xFF0369A1), Color(0xFF38BDF8))
                    )
                    ThemeId.VIBRANT_GRADIENT -> Brush.linearGradient(
                        listOf(Color(0xFFF472B6), Color(0xFF818CF8), Color(0xFF38BDF8))
                    )
                    ThemeId.GLASSMORPHISM -> Brush.linearGradient(
                        listOf(Color(0x90FFFFFF), Color(0x4038BDF8), Color(0x90FFFFFF))
                    )
                    ThemeId.MINIMAL_DARK -> Brush.linearGradient(
                        listOf(Color(0xFF2C3440), Color(0xFF1A1F26))
                    )
                    ThemeId.NATURAL_FRESH -> Brush.linearGradient(
                        listOf(Color(0xFF86EFAC), Color(0xFF22C55E), Color(0xFF86EFAC))
                    )
                },
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        // Layout: Left Rail + Right Content Area
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left Rail
            IntelligenceRail(
                menus = menus,
                activeMenu = activeMenu,
                onMenuSelected = onMenuSelected
            )

            // Right Content Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp, bottom = 10.dp, start = 8.dp, end = 10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header (Logo, Subtitle, Bell, Avatar, Time)
                CardHeader(
                    theme = theme,
                    photoUrl = snapshot.identity.photoUrl
                )

                // Hero Member Area (Action Pill, Avatar, Name, Code, Motto)
                HeroMemberSection(
                    identity = snapshot.identity,
                    theme = theme
                )

                // Plan & Active / Due Status
                MembershipTierStatus(
                    membership = snapshot.membership,
                    theme = theme
                )

                // 3-Column Metrics (Attendance, Payment, Workouts)
                val workoutsCount = when (theme) {
                    ThemeId.NATURAL_FRESH -> 12
                    ThemeId.FUTURISTIC_NEON -> 18
                    ThemeId.MINIMAL_DARK -> 14
                    ThemeId.GLASSMORPHISM -> 16
                    ThemeId.PREMIUM_3D -> 28
                    ThemeId.VIBRANT_GRADIENT -> 8
                    ThemeId.BEAST_MODE -> 24
                    ThemeId.PURPLE_ROYAL -> 18
                }
                CardMetricsGrid(
                    attendance = snapshot.attendance,
                    payment = snapshot.payment,
                    workoutsCount = workoutsCount,
                    theme = theme
                )

                // Primary CTA Button
                ThemedCtaButton(
                    theme = theme,
                    onClick = onCtaClick
                )

                // Footer Section
                CardFooterSection(
                    theme = theme
                )
            }
        }
    }
}
