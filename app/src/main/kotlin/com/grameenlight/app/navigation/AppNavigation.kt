package com.grameenlight.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grameenlight.app.ui.map.MapScreen
import com.grameenlight.app.ui.poles.PolesScreen
import com.grameenlight.app.ui.report.ReportScreen
import com.grameenlight.app.ui.tracker.TrackerScreen
import com.grameenlight.app.viewmodel.MainViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Screen.Map.route) {
        composable(Screen.Map.route)     { MapScreen(viewModel, onReportClick = { navController.navigate(Screen.Report.route) }) }
        composable(Screen.Poles.route)   { PolesScreen(viewModel) }
        composable(Screen.Report.route)  { ReportScreen(viewModel) }
        composable(Screen.Tracker.route) { TrackerScreen(viewModel) }
    }
}
