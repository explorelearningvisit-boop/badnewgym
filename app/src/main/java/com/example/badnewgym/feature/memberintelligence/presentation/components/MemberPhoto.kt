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
import com.example.badnewgym.R
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.colors.ThemeResolver
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipTier

fun resolveMemberDrawable(photoUrl: String?, memberName: String?): Int {
    val key = ((photoUrl ?: "") + " " + (memberName ?: "")).lowercase()
    return when {
        "yash" in key -> R.drawable.portrait_yash
        "arjun" in key -> R.drawable.portrait_arjun
        "riya" in key -> R.drawable.portrait_riya
        "neha" in key -> R.drawable.portrait_neha
        "kabir" in key -> R.drawable.portrait_kabir
        "aarav" in key -> R.drawable.portrait_aarav
        "rohan" in key -> R.drawable.portrait_rohan
        "simran" in key -> R.drawable.portrait_simran
        "vikram" in key -> R.drawable.portrait_vikram
        else -> R.drawable.portrait_yash
    }
}

@Composable
fun MemberPhoto(
    photoUrl: String?,
    tier: MembershipTier,
    modifier: Modifier = Modifier,
    size: Dp? = null,
    width: Dp = size ?: 80.dp,
    height: Dp = size ?: 90.dp,
    showVerified: Boolean = true,
    memberName: String? = null
) {
    val tierColor = ThemeResolver.resolveMemberTierColor(tier, BADGymTheme.colors)
    val portraitShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    val fallbackDrawable = resolveMemberDrawable(photoUrl, memberName)

    Box(modifier = modifier.size(width = width, height = height)) {
        AvatarImage(
            model = photoUrl ?: fallbackDrawable,
            fallbackModel = fallbackDrawable,
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
                tint = BADGymTheme.colors.info,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(20.dp)
                    .background(BADGymTheme.colors.surfaceElevated, androidx.compose.foundation.shape.CircleShape)
            )
        }
    }
}
