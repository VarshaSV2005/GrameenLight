package com.grameenlight.app.utils

/**
 * Converts real GPS coordinates to normalised 0..1 canvas grid coordinates.
 * Update the bounding box values to match your actual village boundary.
 * Tip: right-click the four corners of your village on Google Maps to get lat/lng.
 */
object MapUtils {
    // Village bounding box — replace with your actual coordinates
    private const val MIN_LAT = 13.0550
    private const val MAX_LAT = 13.0640
    private const val MIN_LNG = 77.7290
    private const val MAX_LNG = 77.7380

    fun latLngToGrid(lat: Double, lng: Double): Pair<Float, Float> {
        val x = ((lng - MIN_LNG) / (MAX_LNG - MIN_LNG)).toFloat().coerceIn(0f, 1f)
        val y = (1f - (lat - MIN_LAT) / (MAX_LAT - MIN_LAT)).toFloat().coerceIn(0f, 1f)
        return Pair(x, y)
    }
}

fun Long.toRelativeTime(): String {
    val diff = System.currentTimeMillis() - this
    return when {
        diff < 60_000       -> "Just now"
        diff < 3_600_000    -> "${diff / 60_000} min ago"
        diff < 86_400_000   -> "${diff / 3_600_000} hrs ago"
        else                -> "${diff / 86_400_000} days ago"
    }
}
