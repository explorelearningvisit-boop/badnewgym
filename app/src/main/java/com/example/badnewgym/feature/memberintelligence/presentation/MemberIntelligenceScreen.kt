package com.example.badnewgym.feature.memberintelligence.presentation

import android.content.IntentFilter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugBridge
import com.example.badnewgym.feature.memberintelligence.debug.VariantDebugReceiver
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.design.colors.AdaptiveThemeEngine
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberDashboard

@Composable
fun MemberIntelligenceScreen(viewModel: MemberIntelligenceViewModel) {
    val state by viewModel.state.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val themes = remember { ThemeId.entries.toList() }

    DisposableEffect(viewModel) {
        VariantDebugBridge.onCommand = { variant, menu ->
            if (variant != null) viewModel.selectTheme(variant)
            if (menu != null) viewModel.selectMenu(menu)
        }
        val receiver = VariantDebugReceiver()
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter("com.example.badnewgym.DEBUG_VARIANT"),
            ContextCompat.RECEIVER_EXPORTED
        )
        onDispose {
            VariantDebugBridge.onCommand = null
            runCatching { context.unregisterReceiver(receiver) }
        }
    }

    LaunchedEffect(Unit) {
        if (state !is MemberIntelligenceUiState.Success) {
            viewModel.loadMemberData("BG204", "gym1")
        }
    }

    val success = state as? MemberIntelligenceUiState.Success
    val theme = success?.themeId ?: ThemeId.NATURAL_FRESH
    val adaptiveContext = success?.let {
        AdaptiveThemeEngine.resolve(
            themeId = it.themeId,
            snapshot = it.snapshot,
            currentEventType = it.currentEvent.eventType,
            highestPrioritySignal = it.signals.firstOrNull()
        )
    }

    BADGymTheme(
        colors = theme.colors(),
        shapes = theme.shapes(),
        motion = theme.motion(),
        elevation = theme.elevation(),
        adaptiveContext = adaptiveContext
    ) {
        MemberIntelligenceBackdrop(theme) {
            when (state) {
                MemberIntelligenceUiState.Loading -> LoadingState(theme)
                is MemberIntelligenceUiState.Error -> ErrorState(
                    theme = theme,
                    message = (state as MemberIntelligenceUiState.Error).message,
                    onRetry = { viewModel.loadMemberData("BG204", "gym1") }
                )
                is MemberIntelligenceUiState.Success -> {
                    val s = state as MemberIntelligenceUiState.Success
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                    ) {
                        ThemeSwitcher(
                            themes = themes,
                            selected = s.themeId,
                            onSelected = viewModel::selectTheme
                        )
                        ThemeContextRow(s.themeId)

                        AnimatedContent(
                            targetState = s.themeId,
                            transitionSpec = {
                                fadeIn(androidx.compose.animation.core.tween(220)) togetherWith
                                    fadeOut(androidx.compose.animation.core.tween(150))
                            },
                            label = "theme-transition",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) { selectedTheme ->
                            MemberDashboard(
                                snapshot = s.snapshot,
                                currentEvent = s.currentEvent,
                                signals = s.signals,
                                menus = s.menus,
                                activeMenu = s.activeMenu,
                                onMenuSelected = viewModel::selectMenu,
                                primarySignal = s.primarySignal,
                                secondarySignals = s.secondarySignals,
                                cta = s.cta,
                                onCta = viewModel::executeCta,
                                theme = selectedTheme,
                                gymLive = s.gymLive,
                                gymEvents = s.gymEvents,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Text(
                            text = "BAD GYM  •  MEMBER INTELLIGENCE",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 7.dp, bottom = 4.dp),
                            color = BADGymTheme.colors.textMuted,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                is MemberIntelligenceUiState.Locked -> {
                    val locked = state as MemberIntelligenceUiState.Locked
                    ErrorState(theme, "Feature locked: " + locked.featureName, onRetry = {})
                }
            }
        }
    }
}

@Composable
private fun ThemeSwitcher(themes: List<ThemeId>, selected: ThemeId, onSelected: (ThemeId) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    ) {
        items(themes, key = { it.name }) { theme ->
            val active = theme == selected
            Surface(
                onClick = { onSelected(theme) },
                shape = RoundedCornerShape(20.dp),
                color = if (active) theme.colors().accent else BADGymTheme.colors.surface.copy(alpha = 0.84f),
                border = BorderStroke(1.dp, if (active) theme.colors().accent else BADGymTheme.colors.border),
                tonalElevation = if (active) 2.dp else 0.dp
            ) {
                Text(
                    text = theme.title,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 9.dp),
                    color = if (active) theme.colors().textOnAccent else BADGymTheme.colors.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = if (active) FontWeight.ExtraBold else FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ThemeContextRow(theme: ThemeId) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(theme.title.uppercase(), color = BADGymTheme.colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 1.2.sp)
        Text(theme.category, color = BADGymTheme.colors.textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun MemberIntelligenceBackdrop(theme: ThemeId, content: @Composable BoxScope.() -> Unit) {
    val colors = theme.colors()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(colors.background, colors.background.copy(alpha = 0.97f), colors.surface)
                )
            ),
        content = content
    )
}

@Composable
private fun LoadingState(theme: ThemeId) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CircularProgressIndicator(color = theme.colors().accent, strokeWidth = 3.dp)
            Text("Preparing member intelligence…", color = theme.colors().textSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ErrorState(theme: ThemeId, message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = theme.colors().surface,
            border = BorderStroke(1.dp, theme.colors().border)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Rounded.Bolt, null, tint = theme.colors().accent, modifier = Modifier.size(28.dp))
                Text("Member intelligence unavailable", color = theme.colors().textPrimary, fontWeight = FontWeight.Bold)
                Text(message, color = theme.colors().textSecondary, fontSize = 11.sp)
                Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = theme.colors().accent)) {
                    Icon(Icons.Rounded.Refresh, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Retry")
                }
            }
        }
    }
}
