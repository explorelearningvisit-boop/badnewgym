package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.MemberIdentity
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus

@Composable
fun MemberIdentitySection(
    identity: MemberIdentity,
    membership: MembershipStatus? = null,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        MemberPhoto(
            photoUrl = identity.photoUrl,
            tier = identity.tier,
            showVerified = false,
            size = 110.dp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = identity.name,
                    style = BADGymTheme.typography.memberName,
                    color = BADGymTheme.colors.textPrimary
                )
                if (identity.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Rounded.Verified,
                        contentDescription = "Verified member",
                        tint = BADGymTheme.colors.textPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            identity.code?.let {
                Text(
                    text = it,
                    style = BADGymTheme.typography.timestamp,
                    color = BADGymTheme.colors.textSecondary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            val planName = membership?.planName
            val planType = membership?.planType
            if (!planName.isNullOrBlank()) {
                Text(
                    text = planName,
                    color = BADGymTheme.colors.textPrimary,
                    fontSize = 14.sp
                )
            }
            if (!planType.isNullOrBlank()) {
                Text(
                    text = planType,
                    color = BADGymTheme.colors.textPrimary,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status Box
            Row(
                modifier = Modifier
                    .border(1.dp, BADGymTheme.colors.border, androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = BADGymTheme.colors.textPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ACTIVE",
                    color = BADGymTheme.colors.textPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "48 Days Left",
                color = BADGymTheme.colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
