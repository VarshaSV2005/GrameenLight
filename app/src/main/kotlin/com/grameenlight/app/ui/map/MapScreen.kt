package com.grameenlight.app.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grameenlight.app.model.Pole
import com.grameenlight.app.model.PoleStatus
import com.grameenlight.app.utils.MapUtils
import com.grameenlight.app.utils.toRelativeTime
import com.grameenlight.app.viewmodel.MainViewModel

@Composable
fun MapScreen(viewModel: MainViewModel, onReportClick: () -> Unit) {
    val poles       by viewModel.poles.collectAsStateWithLifecycle()
    val userLoc     by viewModel.userLocation.collectAsStateWithLifecycle()
    val selectedPole by viewModel.selectedPole.collectAsStateWithLifecycle()
    val energySaved by viewModel.energySavedKwh.collectAsStateWithLifecycle()

    val working     = poles.count { it.status == PoleStatus.WORKING }
    val fused       = poles.count { it.status == PoleStatus.FUSED }
    val burningDay  = poles.count { it.status == PoleStatus.BURNING_IN_DAY }

    Column(modifier = Modifier.fillMaxSize()) {

        // Stat row
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard("Working",    working.toString(),   Color(0xFF43A047), Modifier.weight(1f))
            StatCard("Fused",      fused.toString(),     Color(0xFFE53935), Modifier.weight(1f))
            StatCard("On in Day",  burningDay.toString(),Color(0xFFFB8C00), Modifier.weight(1f))
        }

        // Map canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1520))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Grid lines
                    listOf(0.25f, 0.5f, 0.75f).forEach { f ->
                        drawLine(Color.White.copy(alpha = 0.05f), Offset(size.width * f, 0f), Offset(size.width * f, size.height))
                        drawLine(Color.White.copy(alpha = 0.05f), Offset(0f, size.height * f), Offset(size.width, size.height * f))
                    }
                    // Poles
                    poles.forEach { pole ->
                        val cx = pole.gridX * size.width
                        val cy = pole.gridY * size.height
                        val isSelected = selectedPole?.id == pole.id
                        val dotColor = Color(pole.status.dotColor)
                        if (isSelected) {
                            drawCircle(dotColor.copy(alpha = 0.25f), radius = 22f, center = Offset(cx, cy))
                        }
                        drawCircle(Color(0xFF0A1520), radius = if (isSelected) 13f else 9f, center = Offset(cx, cy))
                        drawCircle(dotColor, radius = if (isSelected) 11f else 8f, center = Offset(cx, cy))
                    }
                    // User location blue dot
                    userLoc?.let { loc ->
                        val (gx, gy) = MapUtils.latLngToGrid(loc.latitude, loc.longitude)
                        val ux = gx * size.width
                        val uy = gy * size.height
                        drawCircle(Color(0xFF1565C0).copy(alpha = 0.25f), radius = 26f, center = Offset(ux, uy))
                        drawCircle(Color(0xFF1565C0), radius = 11f, center = Offset(ux, uy))
                        drawCircle(Color.White, radius = 4f, center = Offset(ux, uy))
                    }
                }
                // Pole tap targets
                poles.forEach { pole ->
                    Box(
                        modifier = Modifier
                            .offset(
                                x = (pole.gridX * 320 - 18).dp,
                                y = (pole.gridY * 230 - 18).dp
                            )
                            .size(36.dp)
                            .clickable { viewModel.selectPole(if (selectedPole?.id == pole.id) null else pole) }
                    )
                }
                // Map label
                Text(
                    "VILLAGE MAP — SIMULATED",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 9.sp,
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                )
            }
        }

        // Legend
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PoleStatus.entries.forEach { s ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Box(Modifier.size(8.dp).background(Color(s.dotColor), shape = RoundedCornerShape(4.dp)))
                    Text(s.label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }

        // Selected pole popup
        selectedPole?.let { pole ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(pole.id, style = MaterialTheme.typography.titleSmall)
                        Text(pole.streetName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Spacer(Modifier.height(4.dp))
                        StatusBadge(pole.status)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Last report", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        Text(
                            pole.lastReportedMillis?.toRelativeTime() ?: "Unknown",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Button(
                            onClick = {
                                viewModel.setSelectedStreet(pole.streetName)
                                viewModel.setSelectedPoleId(pole.id)
                                onReportClick()
                            },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) { Text("Report →", fontSize = 12.sp) }
                    }
                }
            }
        }

        // Energy saved card
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("⚡ Energy Saved This Month", style = MaterialTheme.typography.titleSmall)
                Text("$energySaved kWh", fontSize = 22.sp, color = Color(0xFFF59E0B))
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { (energySaved / 500f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFFF59E0B),
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "From $burningDay poles burning during daytime — reported by citizens",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 22.sp, color = color)
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun StatusBadge(status: PoleStatus) {
    val color = Color(status.dotColor)
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(status.label, fontSize = 10.sp, color = color)
    }
}
