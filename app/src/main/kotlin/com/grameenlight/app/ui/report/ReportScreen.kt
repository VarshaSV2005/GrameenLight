package com.grameenlight.app.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grameenlight.app.model.PoleStatus
import com.grameenlight.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: MainViewModel) {
    val streets         = remember { viewModel.getStreets() }
    val selectedStreet  by viewModel.selectedStreet.collectAsStateWithLifecycle()
    val selectedPoleId  by viewModel.selectedPoleId.collectAsStateWithLifecycle()
    val reportStatus    by viewModel.selectedReportStatus.collectAsStateWithLifecycle()
    val reportNote      by viewModel.reportNote.collectAsStateWithLifecycle()
    val lastComplaintId by viewModel.lastComplaintId.collectAsStateWithLifecycle()

    val streetPoles = remember(selectedStreet) { viewModel.getPolesByStreet(selectedStreet) }

    if (lastComplaintId != null) {
        // Success screen
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("✅", fontSize = 56.sp)
            Spacer(Modifier.height(12.dp))
            Text("Report Submitted!", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Complaint ID:", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text(lastComplaintId!!, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("What happens next:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Spacer(Modifier.height(6.dp))
                    listOf(
                        "Panchayat office is notified automatically",
                        "A lineman will be assigned within 24 hrs",
                        "Track status in the Repair Tracker tab"
                    ).forEach { step ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text("›  ", color = Color(0xFFF59E0B))
                            Text(step, fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.clearComplaintId() }, modifier = Modifier.fillMaxWidth()) {
                Text("Report Another Pole")
            }
        }
        return
    }

    // Report form
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("📋 Quick Report a Pole", style = MaterialTheme.typography.titleMedium)

        // Street selector
        var streetExpanded by remember { mutableStateOf(false) }
        Column {
            Text("Street / Area", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(Modifier.height(4.dp))
            ExposedDropdownMenuBox(expanded = streetExpanded, onExpandedChange = { streetExpanded = it }) {
                OutlinedTextField(
                    value = selectedStreet,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = streetExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = streetExpanded, onDismissRequest = { streetExpanded = false }) {
                    streets.forEach { street ->
                        DropdownMenuItem(
                            text = { Text(street) },
                            onClick = { viewModel.setSelectedStreet(street); streetExpanded = false }
                        )
                    }
                }
            }
        }

        // Pole selector
        var poleExpanded by remember { mutableStateOf(false) }
        Column {
            Text("Pole ID", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(Modifier.height(4.dp))
            ExposedDropdownMenuBox(expanded = poleExpanded, onExpandedChange = { poleExpanded = it }) {
                OutlinedTextField(
                    value = selectedPoleId.ifEmpty { "— Select pole —" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = poleExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = poleExpanded, onDismissRequest = { poleExpanded = false }) {
                    streetPoles.forEach { pole ->
                        DropdownMenuItem(
                            text = { Text("${pole.id} (${pole.status.label})") },
                            onClick = { viewModel.setSelectedPoleId(pole.id); poleExpanded = false }
                        )
                    }
                }
            }
        }

        // Status options
        Column {
            Text("Status to Report", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PoleStatus.entries.forEach { s ->
                    val selected = reportStatus == s
                    val dotColor = Color(s.dotColor)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (selected) 2.dp else 1.dp,
                                color = if (selected) dotColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                if (selected) dotColor.copy(alpha = 0.12f) else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.setReportStatus(s) }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val icon = when (s) {
                                PoleStatus.WORKING       -> "✓"
                                PoleStatus.FUSED         -> "✕"
                                PoleStatus.BURNING_IN_DAY -> "☀"
                            }
                            Text(icon, fontSize = 20.sp, color = dotColor)
                            Spacer(Modifier.height(4.dp))
                            Text(s.label, fontSize = 10.sp, color = dotColor, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }

        // Note
        Column {
            Text("Additional Note (optional)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = reportNote,
                onValueChange = { if (it.length <= 150) viewModel.setReportNote(it) },
                placeholder = { Text("e.g. Wire hanging loose, bulb flickering...", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 4
            )
            Text("${reportNote.length}/150", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), modifier = Modifier.align(Alignment.End))
        }

        Button(
            onClick = { viewModel.submitReport() },
            enabled = selectedPoleId.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Submit Report & Get Complaint ID", fontSize = 14.sp)
        }

        Text(
            "A unique Complaint ID will be generated for tracking",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
