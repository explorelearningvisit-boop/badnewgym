package com.example.badnewgym.feature.membercard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val quickFilters = listOf("All", "Attendance", "Receipts", "Workouts", "Body Stats")
    var selectedFilter by remember { mutableStateOf("All") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        scrimColor = Color(0x66021C17),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        GlassSheetSurface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                GlassSheetHeader(
                    title = "Quick search",
                    subtitle = "Find any record in one place",
                    onClose = onDismissRequest
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Search field — frosted well with a lit rim.
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search logs, receipts, workouts...",
                            color = GlassFaint,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = InfographicTokens.Brand,
                            modifier = Modifier.size(21.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GlassInk
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = InfographicTokens.Brand,
                        unfocusedBorderColor = GlassHairline,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = Color(0xFF9DB2C8), spotColor = Color(0xFFB9C7D6))
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick filter chips
                Text(
                    text = "FILTER BY",
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = GlassFaint,
                    letterSpacing = 0.9.sp,
                    modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    quickFilters.forEach { filter ->
                        GlassChip(
                            label = filter,
                            selected = filter == selectedFilter,
                            accent = InfographicTokens.Brand,
                            onClick = { selectedFilter = filter }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "RECENT SEARCHES",
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = GlassFaint,
                    letterSpacing = 0.9.sp,
                    modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
                )

                RecentSearchRow("Sep 2026 Attendance Summary", InfographicTokens.Brand) { searchQuery = "Sep 2026 Attendance" }
                Spacer(modifier = Modifier.height(8.dp))
                RecentSearchRow("Prime Membership Payment Receipt", InfographicTokens.Orange) { searchQuery = "Receipt 2026" }
                Spacer(modifier = Modifier.height(8.dp))
                RecentSearchRow("Chest & Triceps Hypertrophy Log", InfographicTokens.Purple) { searchQuery = "Chest & Triceps" }

                GlassSheetFooterPad(height = 18)
            }
        }
    }
}

@Composable
private fun RecentSearchRow(
    text: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = Color(0xFF9DB2C8), spotColor = Color(0xFFB9C7D6))
            .clip(RoundedCornerShape(16.dp))
            .background(GlassPaneFill)
            .border(1.dp, Color.White, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Brush.verticalGradient(listOf(tint.copy(alpha = 0.16f), tint.copy(alpha = 0.08f))))
                .border(0.8.dp, tint.copy(alpha = 0.24f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(17.dp)
            )
        }
        Spacer(modifier = Modifier.width(11.dp))
        Text(
            text = text,
            color = GlassInkSoft,
            fontSize = 13.5.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}
