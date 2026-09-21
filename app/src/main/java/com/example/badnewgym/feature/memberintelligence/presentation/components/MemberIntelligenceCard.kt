package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.*

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
    val payment = snapshot.payment
    val attendance = snapshot.attendance
    val membership = snapshot.membership
    val trainer = snapshot.trainer
    val workout = snapshot.workout

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(cardBrush(theme))
            .border(
                width = if (theme == ThemeId.PREMIUM_3D) 1.5.dp else 1.dp,
                brush = borderBrush(theme),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Row(Modifier.fillMaxSize()) {
            IntelligenceRail(
                menus = menus,
                activeMenu = activeMenu,
                onMenuSelected = onMenuSelected,
                modifier = Modifier.width(54.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                CardHeader(theme = theme, photoUrl = snapshot.identity.photoUrl)
                EventHeader(
                    event = snapshot.recentEvents.firstOrNull()
                        ?: MemberEvent(
                            id = "ui_event",
                            memberId = snapshot.id,
                            gymId = snapshot.gymId,
                            eventType = EventType.CHECK_IN,
                            occurredAt = System.currentTimeMillis()
                        ),
                    theme = theme
                )

                MemberHeroCompact(snapshot, theme)
                MembershipTierStatus(membership, theme)

                when (activeMenu) {
                    MenuType.HOME -> {
                        CardMetricsGrid(
                            attendance = attendance,
                            payment = payment,
                            workoutsCount = 12,
                            theme = theme
                        )
                        DetailStrip(theme, trainer, workout, snapshot)
                        ThemedCtaButton(
                            theme = theme,
                            onClick = onCtaClick,
                            modifier = Modifier.padding(top = 1.dp)
                        )
                        CardFooterSection(theme = theme)
                    }
                    MenuType.ATTENDANCE -> AttendancePanel(snapshot, theme)
                    MenuType.PLAN -> PlanPanel(snapshot, theme)
                    MenuType.PAYMENT -> PaymentPanel(snapshot, theme, onCtaClick)
                    MenuType.TRAINER -> TrainerPanel(snapshot, theme)
                    MenuType.WORKOUT -> WorkoutPanel(snapshot, theme)
                    else -> ServicesPanel(snapshot, theme)
                }
            }
        }

        if (theme != ThemeId.MINIMAL_DARK) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(92.dp)
                    .clip(RoundedCornerShape(46.dp))
                    .background(colors.cardGlow.copy(alpha = 0.10f))
            )
        }
    }
}

@Composable
private fun MemberHeroCompact(snapshot: MemberSnapshot, theme: ThemeId) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MemberPhoto(
            photoUrl = snapshot.identity.photoUrl,
            tier = snapshot.identity.tier,
            size = 78.dp,
            showVerified = snapshot.identity.isVerified
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    snapshot.identity.name,
                    color = colors.textPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1
                )
                if (snapshot.identity.isVerified) {
                    Spacer(Modifier.width(3.dp))
                    Icon(
                        Icons.Rounded.Verified,
                        contentDescription = "Verified",
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Text(snapshot.identity.code ?: "MEMBER", color = colors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(
                snapshot.membership?.let { it.planName + " • " + it.planType } ?: "Membership",
                color = colors.textMuted,
                fontSize = 9.sp
            )
            Text(
                theme.motto,
                color = colors.mottoColor,
                fontSize = 10.5.sp,
                fontWeight = if (theme.isDark) FontWeight.Black else FontWeight.Bold,
                lineHeight = 12.sp,
                maxLines = 3
            )
        }
    }
}

@Composable
private fun DetailStrip(
    theme: ThemeId,
    trainer: TrainerSummary?,
    workout: WorkoutSummary?,
    snapshot: MemberSnapshot
) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        DetailTile(
            Modifier.weight(1f), Icons.Rounded.Person, "Trainer",
            trainer?.trainerName ?: "Assigned", colors.accent, theme
        )
        DetailTile(
            Modifier.weight(1f), Icons.Rounded.FitnessCenter, "Workout",
            workout?.currentRoutine ?: "Not set", themeDetailAccent(theme), theme
        )
        DetailTile(
            Modifier.weight(1f), Icons.Rounded.Settings, "Services",
            (snapshot.services?.count { it.isActive } ?: 0).toString() + " Active",
            colors.success, theme
        )
    }
}

@Composable
private fun DetailTile(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    accent: Color,
    theme: ThemeId
) {
    val colors = BADGymTheme.colors
    Column(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(detailSurface(theme))
            .border(1.dp, colors.border.copy(alpha = 0.55f), RoundedCornerShape(10.dp))
            .padding(horizontal = 6.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(3.dp))
            Text(label, color = colors.textSecondary, fontSize = 7.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            value,
            color = colors.textPrimary,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

private fun cardBrush(theme: ThemeId): Brush = when (theme) {
    ThemeId.NATURAL_FRESH -> Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF0FDF4)))
    ThemeId.FUTURISTIC_NEON -> Brush.verticalGradient(listOf(Color(0xFF081426), Color(0xFF020617)))
    ThemeId.MINIMAL_DARK -> Brush.verticalGradient(listOf(Color(0xFF171A20), Color(0xFF0B0D11)))
    ThemeId.GLASSMORPHISM -> Brush.verticalGradient(listOf(Color(0xEFFFFFFF), Color(0xC9EAF6FF)))
    ThemeId.PREMIUM_3D -> Brush.verticalGradient(listOf(Color(0xFF18130A), Color(0xFF080704)))
    ThemeId.VIBRANT_GRADIENT -> Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF8F5FF)))
    ThemeId.BEAST_MODE -> Brush.verticalGradient(listOf(Color(0xFF1A0508), Color(0xFF090102)))
    ThemeId.PURPLE_ROYAL -> Brush.verticalGradient(listOf(Color(0xFF211039), Color(0xFF0D061A)))
}

private fun borderBrush(theme: ThemeId): Brush = when (theme) {
    ThemeId.NATURAL_FRESH -> Brush.linearGradient(listOf(Color(0xFF86EFAC), Color(0xFF22C55E)))
    ThemeId.FUTURISTIC_NEON -> Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF0369A1)))
    ThemeId.MINIMAL_DARK -> Brush.linearGradient(listOf(Color(0xFF303742), Color(0xFF171B22)))
    ThemeId.GLASSMORPHISM -> Brush.linearGradient(listOf(Color(0xCCFFFFFF), Color(0x5538BDF8), Color(0xCCFFFFFF)))
    ThemeId.PREMIUM_3D -> Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFF8B6516), Color(0xFFFFE57F)))
    ThemeId.VIBRANT_GRADIENT -> Brush.linearGradient(listOf(Color(0xFFF472B6), Color(0xFF818CF8), Color(0xFF38BDF8)))
    ThemeId.BEAST_MODE -> Brush.linearGradient(listOf(Color(0xFFFF1E27), Color(0xFF7F1D1D)))
    ThemeId.PURPLE_ROYAL -> Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF581C87), Color(0xFFC084FC)))
}

private fun detailSurface(theme: ThemeId): Color = when (theme) {
    ThemeId.NATURAL_FRESH -> Color(0xFFF6FBF7)
    ThemeId.FUTURISTIC_NEON -> Color(0xFF0A1428)
    ThemeId.MINIMAL_DARK -> Color(0xFF181C22)
    ThemeId.GLASSMORPHISM -> Color(0xAFFFFFFF)
    ThemeId.PREMIUM_3D -> Color(0xFF1F1A10)
    ThemeId.VIBRANT_GRADIENT -> Color(0xFFF8FAFC)
    ThemeId.BEAST_MODE -> Color(0xFF1C0609)
    ThemeId.PURPLE_ROYAL -> Color(0xFF22113B)
}

private fun themeDetailAccent(theme: ThemeId): Color = when (theme) {
    ThemeId.PREMIUM_3D -> Color(0xFFFFD700)
    ThemeId.BEAST_MODE -> Color(0xFFFF1E27)
    ThemeId.PURPLE_ROYAL -> Color(0xFFA855F7)
    ThemeId.FUTURISTIC_NEON -> Color(0xFF00E5FF)
    ThemeId.GLASSMORPHISM -> Color(0xFF0284C7)
    ThemeId.VIBRANT_GRADIENT -> Color(0xFF3B82F6)
    ThemeId.MINIMAL_DARK -> Color(0xFF9CA3AF)
    ThemeId.NATURAL_FRESH -> Color(0xFF16A34A)
}
