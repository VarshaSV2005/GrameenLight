package com.grameenlight.app.model

import androidx.compose.ui.graphics.Color

enum class PoleStatus(val label: String, val color: Color, val dotColor: Long) {
    WORKING("Working", Color(0xFF43A047), 0xFF43A047),
    FUSED("Fused / Dead", Color(0xFFE53935), 0xFFE53935),
    BURNING_IN_DAY("On During Day", Color(0xFFFB8C00), 0xFFFB8C00)
}
