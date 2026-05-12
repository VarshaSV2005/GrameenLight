package com.grameenlight.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.grameenlight.app.navigation.AppNavigation
import com.grameenlight.app.navigation.Screen
import com.grameenlight.app.ui.GrameenLightTheme
import com.grameenlight.app.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDark = remember { mutableStateOf(true) }

            GrameenLightTheme(darkTheme = isDark.value) {
                val locationPerm = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

                LaunchedEffect(locationPerm.status.isGranted) {
                    if (locationPerm.status.isGranted) {
                        viewModel.startLocationTracking()
                    } else {
                        locationPerm.launchPermissionRequest()
                    }
                }

                val navController = rememberNavController()
                val backStack by navController.currentBackStackEntryAsState()
                val currentRoute = backStack?.destination?.route

                val navItems = listOf(
                    Triple(Screen.Map,     "Map",     Icons.Default.Place),
                    Triple(Screen.Poles,   "Poles",   Icons.Default.List),
                    Triple(Screen.Report,  "Report",  Icons.Default.Home),
                    Triple(Screen.Tracker, "Tracker", Icons.Default.Build)
                )

                Scaffold(
                    topBar = {
                        Surface(color = if (isDark.value) Color(0xFF0A1520) else Color(0xFF1565C0)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .statusBarsPadding()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("⚡ Grameen-Light", color = Color.White, fontSize = 16.sp)
                                    Text("Citizen Streetlight Audit", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                                }
                                TextButton(onClick = { isDark.value = !isDark.value }) {
                                    Text(if (isDark.value) "☀ Day" else "🌙 Night", color = Color.White, fontSize = 12.sp)
                                }
                            }
                        }
                    },
                    bottomBar = {
                        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                            navItems.forEach { (screen, label, icon) ->
                                NavigationBarItem(
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label, fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                        AppNavigation(navController, viewModel)
                    }
                }
            }
        }
    }
}
