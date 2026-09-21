package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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
    showVerified: Boolean = true
) {
    val resolvedSize = size ?: BADGymTheme.dimensions.memberPhotoSize
    val tierColor = ThemeResolver.resolveMemberTierColor(tier, BADGymTheme.colors)

    Box(modifier = modifier.size(resolvedSize)) {
        AvatarImage(
            model = photoUrl,
            size = resolvedSize,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
            placeholderTint = BADGymTheme.colors.surfaceMuted,
            modifier = Modifier
                .clip(BADGymTheme.shapes.avatar)
                .border(2.dp, tierColor, BADGymTheme.shapes.avatar)
        )
        if (showVerified) {
            Icon(
                imageVector = Icons.Rounded.Verified,
                contentDescription = "Verified",
                tint = Color(0xFF3B82F6),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(18.dp)
                    .background(Color.White, androidx.compose.foundation.shape.CircleShape)
            )
        }
    }
}
