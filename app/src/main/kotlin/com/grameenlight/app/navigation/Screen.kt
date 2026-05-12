package com.grameenlight.app.navigation

sealed class Screen(val route: String) {
    object Map      : Screen("map")
    object Poles    : Screen("poles")
    object Report   : Screen("report")
    object Tracker  : Screen("tracker")
}
