package com.grameenlight.app.ui.poles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grameenlight.app.model.PoleStatus
import com.grameenlight.app.ui.map.StatusBadge
import com.grameenlight.app.utils.toRelativeTime
import com.grameenlight.app.viewmodel.MainViewModel

@Composable
fun PolesScreen(viewModel: MainViewModel) {
    val poles    by viewModel.poles.collectAsStateWithLifecycle()
    val filter   by viewModel.filterStatus.collectAsStateWithLifecycle()

    val filtered = if (filter == null) poles else poles.filter { it.status == filter }

    Column(modifier = Modifier.fillMaxSize()) {
        // Filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(selected = filter == null, onClick = { viewModel.setFilter(null) }, label = { Text("All Poles") })
            }
            items(PoleStatus.entries) { s ->
                FilterChip(
                    selected = filter == s,
                    onClick = { viewModel.setFilter(if (filter == s) null else s) },
                    label = { Text(s.label) }
                )
            }
        }

        // List
        LazyColumn(contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
            items(filtered) { pole ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(pole.status.dotColor), CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(pole.id, style = MaterialTheme.typography.titleSmall)
                            Text(
                                "${pole.streetName} · ${pole.lastReportedMillis?.toRelativeTime() ?: "Unknown"}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        StatusBadge(pole.status)
                    }
                }
            }
        }
    }
}
