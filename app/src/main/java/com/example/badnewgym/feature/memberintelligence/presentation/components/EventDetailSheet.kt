package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import com.example.badnewgym.feature.memberintelligence.domain.model.EventType
import com.example.badnewgym.feature.memberintelligence.domain.model.TemporalEventRecord
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EventDetailDialog(
    event: TemporalEventRecord,
    onDismiss: () -> Unit
) {
    val colors = BADGymTheme.colors

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(colors.surface)
                .border(1.dp, colors.border.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(colors.accent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (event.eventType == EventType.PAYMENT) Icons.Rounded.ReceiptLong else Icons.Rounded.Verified,
                            contentDescription = null,
                            tint = colors.accent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = event.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Text(
                            text = event.formatTimestamp(withSeconds = true),
                            fontSize = 10.5.sp,
                            color = colors.textMuted
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(colors.surfaceMuted)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close dialog",
                        tint = colors.textPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            HorizontalDivider(color = colors.divider)

            // Subtitle / High-level status
            event.subtitle?.let { sub ->
                Text(
                    text = sub,
                    fontSize = 11.5.sp,
                    color = colors.textSecondary,
                    lineHeight = 15.sp
                )
            }

            // Amount if financial
            if (event.amount != null) {
                val formattedAmt = NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(event.amount)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.success.copy(alpha = 0.1f))
                        .border(1.dp, colors.success.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transaction Amount",
                        fontSize = 11.sp,
                        color = colors.textMuted
                    )
                    Text(
                        text = formattedAmt,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.success
                    )
                }
            }

            // Source Verification details
            if (event.sourceMetadata != null) {
                val meta = event.sourceMetadata
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.surfaceMuted.copy(alpha = 0.5f))
                        .border(1.dp, colors.border.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "AUDIT & HARDWARE TELEMETRY",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.accent
                    )
                    AuditRow("Verification Source", meta.source)
                    meta.gateName?.let { AuditRow("Access Gate", it) }
                    meta.deviceId?.let { AuditRow("Device ID", it) }
                    meta.confidenceScore?.let { AuditRow("Match Confidence", "${(it * 100).toInt()}%") }
                    meta.latencyMs?.let { AuditRow("Device Latency", "$it ms") }
                }
            }

            // Custom key-value detail list
            if (event.details.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.surfaceMuted.copy(alpha = 0.5f))
                        .border(1.dp, colors.border.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "EVENT AUDIT DETAILS",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.accent
                    )
                    event.details.forEach { (k, v) ->
                        AuditRow(k, v)
                    }
                }
            }

            // Close button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.accent)
                    .clickable { onDismiss() }
                    .padding(vertical = 9.dp)
                    .semantics { contentDescription = "Dismiss Event Audit Details" },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Done",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textOnAccent
                )
            }
        }
    }
}

@Composable
private fun AuditRow(label: String, value: String) {
    val colors = BADGymTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 10.5.sp, color = colors.textMuted)
        Text(text = value, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
    }
}
