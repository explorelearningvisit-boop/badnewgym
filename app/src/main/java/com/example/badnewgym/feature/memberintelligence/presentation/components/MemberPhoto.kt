package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier

@Composable
fun MemberPhoto(
    photoUrl: String?,
    tier: MembershipTier,
    modifier: Modifier = Modifier,
    size: Dp? = null,
    showVerified: Boolean = true
) {
    val resolvedSize = size ?: BADGymTheme.dimensions.memberPhotoSize
    val tierColor = ThemeResolver.resolveMemberTierColor(tier, BADGymTheme.colors)

    Box(modifier = modifier.size(resolvedSize), contentAlignment = Alignment.Center) {
        AvatarImage(
            model = photoUrl,
            size = resolvedSize,
            shape = CircleShape,
            placeholderTint = BADGymTheme.colors.surfaceMuted,
            modifier = Modifier
                .background(BADGymTheme.colors.surfaceMuted, CircleShape)
                .border(3.dp, tierColor, CircleShape)
        )
        if (showVerified) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Verified,
                    contentDescription = "Verified",
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
