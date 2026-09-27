package com.example.badnewgym.feature.deployment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.badnewgym.feature.memberintelligence.design.BADGymTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DeploymentProgress(
    val phase: String = "",
    val title: String = "",
    val detail: String = "",
    val percent: Int = 0,
    val bytes: Long = -1L,
    val totalBytes: Long = -1L,
    val sha: String = "",
    val isError: Boolean = false,
    val isVisible: Boolean = true
)

object DeploymentProgressBridge {
    private const val ACTION_DEBUG_DEPLOYMENT = "com.example.badnewgym.DEBUG_DEPLOYMENT_PROGRESS"
    
    private val _state = MutableStateFlow<DeploymentProgress?>(null)
    val state: StateFlow<DeploymentProgress?> = _state.asStateFlow()

    fun initialize(context: Context) {
        // Ready for receiving broadcast telemetry
    }

    fun register(context: Context): BroadcastReceiver {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (intent?.action == ACTION_DEBUG_DEPLOYMENT) {
                    val phase = intent.getStringExtra("phase") ?: ""
                    val title = intent.getStringExtra("title") ?: ""
                    val detail = intent.getStringExtra("detail") ?: ""
                    val percent = intent.getIntExtra("percent", 0)
                    val bytes = intent.getLongExtra("bytes", -1L)
                    val totalBytes = intent.getLongExtra("total_bytes", -1L)
                    val sha = intent.getStringExtra("sha") ?: ""
                    val isError = intent.getBooleanExtra("error", false)
                    
                    _state.value = DeploymentProgress(
                        phase = phase,
                        title = title,
                        detail = detail,
                        percent = percent,
                        bytes = bytes,
                        totalBytes = totalBytes,
                        sha = sha,
                        isError = isError,
                        isVisible = phase != "COMPLETE" || percent < 100
                    )
                }
            }
        }
        val filter = IntentFilter(ACTION_DEBUG_DEPLOYMENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }
        return receiver
    }

    fun unregister(context: Context, receiver: BroadcastReceiver) {
        try {
            context.unregisterReceiver(receiver)
        } catch (_: Exception) {}
    }
}

@Composable
fun DeploymentProgressOverlay(
    progress: DeploymentProgress?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = progress != null && progress.isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        if (progress == null) return@AnimatedVisibility
        val colors = BADGymTheme.colors
        val barColor = if (progress.isError) colors.danger else colors.accent

        Card(
            modifier = modifier
                .padding(horizontal = 12.dp, vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surfaceMuted),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = barColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = progress.phase,
                                color = barColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = progress.title,
                            color = colors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (progress.sha.isNotBlank()) {
                        Text(
                            text = progress.sha,
                            color = colors.textMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { (progress.percent.coerceIn(0, 100)) / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = barColor,
                    trackColor = colors.border.copy(alpha = 0.3f),
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = progress.detail,
                        color = colors.textSecondary,
                        fontSize = 10.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${progress.percent}%",
                        color = barColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    }
}
