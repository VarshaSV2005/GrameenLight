package com.grameenlight.app.ui.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grameenlight.app.model.RepairStatus
import com.grameenlight.app.viewmodel.MainViewModel

@Composable
fun TrackerScreen(viewModel: MainViewModel) {
    val complaints by viewModel.complaints.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "🔧 Repair Tracker",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        )
        LazyColumn(contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
            items(complaints) { c ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(c.id, style = MaterialTheme.typography.titleSmall, color = Color(0xFFF59E0B))
                            RepairBadge(c.repairStatus)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("${c.poleId} · ${c.streetName}", fontSize = 12.sp)
                        Text(
                            "Issue: ${c.issue} · ${c.timeLabel}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Assigned to: ", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            Text(c.assignedTo, fontSize = 11.sp)
                        }
                        // Progress bar
                        Spacer(Modifier.height(10.dp))
                        val stages = listOf(RepairStatus.PENDING, RepairStatus.ASSIGNED, RepairStatus.FIXED)
                        val idx = stages.indexOf(c.repairStatus)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            stages.forEachIndexed { i, stage ->
                                val filled = idx >= i
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(5.dp)
                                        .background(
                                            if (filled) (if (stage == RepairStatus.FIXED) Color(0xFF43A047) else Color(0xFFF59E0B))
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                                            RoundedCornerShape(3.dp)
                                        )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("Pending", "Assigned", "Fixed").forEach { s ->
                                Text(s, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RepairBadge(status: RepairStatus) {
    val (bg, fg) = when (status) {
        RepairStatus.FIXED    -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        RepairStatus.ASSIGNED -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        RepairStatus.PENDING  -> Color(0xFFFFF9C4) to Color(0xFFF57F17)
        RepairStatus.NONE     -> Color(0xFFF5F5F5) to Color(0xFF9E9E9E)
    }
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(status.label, fontSize = 10.sp, color = fg)
    }
}
