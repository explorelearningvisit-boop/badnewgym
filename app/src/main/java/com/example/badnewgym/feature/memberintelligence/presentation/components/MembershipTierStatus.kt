package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.design.ThemeId
import com.example.badnewgym.feature.memberintelligence.domain.model.MembershipStatus

@Composable
fun MembershipTierStatus(
    membership: MembershipStatus?,
    theme: ThemeId,
    modifier: Modifier = Modifier
) {
    val colors = BADGymTheme.colors
    val planName = membership?.planName ?: "Premium Plan"
    val planType = membership?.planType ?: "12 Months"
    val daysRemaining = membership?.daysRemaining ?: 48

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Plan Tier Card (e.g. Gold Plan 12 Months)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    when (theme) {
                        ThemeId.PREMIUM_3D -> Brush.horizontalGradient(listOf(Color(0xFF2E2413), Color(0xFF1E170A)))
                        ThemeId.BEAST_MODE -> Brush.horizontalGradient(listOf(Color(0xFF220A0E), Color(0xFF160507)))
                        ThemeId.PURPLE_ROYAL -> Brush.horizontalGradient(listOf(Color(0xFF2A1545), Color(0xFF1B0C30)))
                        ThemeId.FUTURISTIC_NEON -> Brush.horizontalGradient(listOf(Color(0xFF0F1E38), Color(0xFF070F1E)))
                        ThemeId.VIBRANT_GRADIENT -> Brush.horizontalGradient(listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)))
                        ThemeId.GLASSMORPHISM -> Brush.horizontalGradient(listOf(Color(0xD0E0F2FE), Color(0x90BAE6FD)))
                        ThemeId.MINIMAL_DARK -> Brush.horizontalGradient(listOf(Color(0xFF1C222B), Color(0xFF151920)))
                        ThemeId.NATURAL_FRESH -> Brush.horizontalGradient(listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A)))
                    }
                )
                .border(
                    width = 1.dp,
                    color = when (theme) {
                        ThemeId.PREMIUM_3D -> Color(0xFFE5B842)
                        ThemeId.BEAST_MODE -> Color(0x66FF1E27)
                        ThemeId.PURPLE_ROYAL -> Color(0x66A855F7)
                        ThemeId.FUTURISTIC_NEON -> Color(0x4D00E5FF)
                        ThemeId.VIBRANT_GRADIENT -> Color(0xFFCBD5E1)
                        ThemeId.GLASSMORPHISM -> Color(0x80FFFFFF)
                        ThemeId.MINIMAL_DARK -> Color(0xFF2D3748)
                        ThemeId.NATURAL_FRESH -> Color(0xFFFCD34D)
                    },
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.WorkspacePremium,
                    contentDescription = "Tier Crown",
                    tint = when (theme) {
                        ThemeId.PREMIUM_3D -> Color(0xFFFFD700)
                        ThemeId.BEAST_MODE -> Color(0xFFFF4D5E)
                        ThemeId.PURPLE_ROYAL -> Color(0xFFC084FC)
                        ThemeId.FUTURISTIC_NEON -> Color(0xFFFACC15)
                        ThemeId.VIBRANT_GRADIENT -> Color(0xFF3B82F6)
                        ThemeId.GLASSMORPHISM -> Color(0xFF0284C7)
                        ThemeId.MINIMAL_DARK -> Color(0xFFCBD5E1)
                        ThemeId.NATURAL_FRESH -> Color(0xFFD97706)
                    },
                    modifier = Modifier.size(18.dp)
                )

                Column {
                    Text(
                        text = planName,
                        color = when (theme) {
                            ThemeId.NATURAL_FRESH -> Color(0xFF78350F)
                            ThemeId.VIBRANT_GRADIENT -> Color(0xFF1E293B)
                            ThemeId.PREMIUM_3D -> Color(0xFFFFE082)
                            else -> colors.textPrimary
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = planType,
                        color = when (theme) {
                            ThemeId.NATURAL_FRESH -> Color(0xFF92400E)
                            ThemeId.VIBRANT_GRADIENT -> Color(0xFF64748B)
                            ThemeId.PREMIUM_3D -> Color(0xFFD4AF37)
                            else -> colors.textSecondary
                        },
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Active Status or Payment Due Pill
        val isPaymentDue = theme == ThemeId.VIBRANT_GRADIENT
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isPaymentDue) Color(0xFFFFEDD5)
                    else when (theme) {
                        ThemeId.NATURAL_FRESH -> Color(0xFFDCFCE7)
                        ThemeId.FUTURISTIC_NEON -> Color(0xFF042F2E)
                        ThemeId.MINIMAL_DARK -> Color(0xFF064E3B).copy(alpha = 0.6f)
                        ThemeId.GLASSMORPHISM -> Color(0x60D1FAE5)
                        ThemeId.BEAST_MODE -> Color(0xFF064E3B)
                        ThemeId.PURPLE_ROYAL -> Color(0xFF064E3B)
                        else -> colors.surfaceMuted
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isPaymentDue) Color(0xFFF97316)
                    else when (theme) {
                        ThemeId.NATURAL_FRESH -> Color(0xFF86EFAC)
                        ThemeId.FUTURISTIC_NEON -> Color(0xFF14B8A6)
                        ThemeId.MINIMAL_DARK -> Color(0xFF059669)
                        ThemeId.GLASSMORPHISM -> Color(0xFF34D399)
                        ThemeId.BEAST_MODE -> Color(0xFF10B981)
                        ThemeId.PURPLE_ROYAL -> Color(0xFF10B981)
                        else -> colors.border
                    },
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (isPaymentDue) Icons.Rounded.Warning else Icons.Rounded.AddCircle,
                    contentDescription = "Status",
                    tint = if (isPaymentDue) Color(0xFFEA580C) else Color(0xFF16A34A),
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    text = if (isPaymentDue) "! PAYMENT DUE" else "ACTIVE",
                    color = if (isPaymentDue) Color(0xFFC2410C)
                    else if (theme == ThemeId.NATURAL_FRESH || theme == ThemeId.GLASSMORPHISM) Color(0xFF15803D)
                    else Color(0xFF34D399),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (isPaymentDue) "₹2,000 • 2 days" else "$daysRemaining Days Left",
                    color = if (isPaymentDue) Color(0xFF9A3412)
                    else if (theme == ThemeId.NATURAL_FRESH || theme == ThemeId.GLASSMORPHISM) Color(0xFF166534)
                    else Color(0xFF6EE7B7),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
