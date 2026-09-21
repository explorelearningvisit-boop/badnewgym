package com.example.badnewgym.feature.memberintelligence.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.design.colors.AdaptiveThemeEngine
import com.example.badnewgym.feature.memberintelligence.design.elevation
import com.example.badnewgym.feature.memberintelligence.design.motion
import com.example.badnewgym.feature.memberintelligence.design.shapes
import com.example.badnewgym.feature.memberintelligence.domain.engine.MemberIntelligenceEngine
import com.example.badnewgym.feature.memberintelligence.domain.engine.MenuAvailabilityResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.MenuType
import com.example.badnewgym.feature.memberintelligence.presentation.components.MemberDashboard
import com.example.badnewgym.feature.memberintelligence.preview.scenarios.MemberScenarios

@Composable
fun ThemeGalleryScreen() {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(scrollState)
            .padding(vertical = 32.dp)
    ) {
        Text(
            text = "Theme Gallery Preview",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "Validating rendering for Glassmorphism, 3D Gradients, and Neon glows.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // List of all themes to showcase
        val themes = listOf(
            ThemeId.VIBRANT_GRADIENT,
            ThemeId.FUTURISTIC_NEON,
            ThemeId.NATURAL_FRESH,
            ThemeId.PREMIUM_3D,
            ThemeId.GLASSMORPHISM
        )
        
        val (snapshot, event) = MemberScenarios.overdueActiveMember()
        val result = MemberIntelligenceEngine().evaluate(snapshot, event, event.occurredAt)
        
        themes.forEach { themeId ->
            Text(
                text = themeId.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Constrain width to 360dp to test layout proportion as requested
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .width(360.dp) // Strictly constrain width to 360dp
                    .height(640.dp) // Standard height
            ) {
                // Manually inject the theme and adaptive context
                val adaptiveContext = AdaptiveThemeEngine.resolve(
                    themeId = themeId,
                    snapshot = snapshot,
                    currentEventType = event.eventType,
                    highestPrioritySignal = result.primary
                )
                
                BADGymTheme(
                    colors = themeId.colors(),
                    shapes = themeId.shapes(),
                    motion = themeId.motion(),
                    elevation = themeId.elevation(),
                    adaptiveContext = adaptiveContext
                ) {
                    MemberDashboard(
                        snapshot = snapshot,
                        currentEvent = event,
                        signals = result.signals,
                        menus = MenuAvailabilityResolver.resolve(snapshot, result.signals),
                        activeMenu = MenuType.HOME,
                        onMenuSelected = {},
                        primarySignal = result.primary,
                        secondarySignals = result.secondary,
                        cta = result.cta
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
