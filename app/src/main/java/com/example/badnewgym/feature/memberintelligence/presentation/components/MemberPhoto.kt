package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    width: Dp = size ?: 80.dp,
    height: Dp = size ?: 90.dp,
    showVerified: Boolean = true
) {
    val tierColor = ThemeResolver.resolveMemberTierColor(tier, BADGymTheme.colors)
    val portraitShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)

    Box(modifier = modifier.size(width = width, height = height)) {
        AvatarImage(
            model = photoUrl,
            width = width,
            height = height,
            shape = portraitShape,
            placeholderTint = BADGymTheme.colors.surfaceMuted,
            modifier = Modifier
                .fillMaxSize()
                .clip(portraitShape)
                .border(2.dp, tierColor.copy(alpha = 0.85f), portraitShape)
        )
        if (showVerified) {
            Icon(
                imageVector = Icons.Rounded.Verified,
                contentDescription = "Verified",
                tint = Color(0xFF38BDF8),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(20.dp)
                    .background(Color.White, androidx.compose.foundation.shape.CircleShape)
            )
        }
    }
}
