package com.example.android_practice.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun BreweryTypeIconPlaceholder(
    type: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp
) {
    val (circleBg, iconTint, iconVector) = when (type.lowercase(Locale.ROOT)) {
        "micro" -> Triple(
            Color(0xFFFFF3E0),
            Color(0xFFC9791C),
            Icons.Default.SportsBar
        )
        "brewpub" -> Triple(
            Color(0xFFE8F5E9),
            Color(0xFF6B8E4E),
            Icons.Default.Restaurant
        )
        "regional" -> Triple(
            Color(0xFFE3F2FD),
            Color(0xFF1565C0),
            Icons.Default.Business
        )
        "large" -> Triple(
            Color(0xFFF3E5F5),
            Color(0xFF7B1FA2),
            Icons.Default.Domain
        )
        "closed" -> Triple(
            Color(0xFFFFEBEE),
            Color(0xFFC62828),
            Icons.Default.Block
        )
        else -> Triple(
            Color(0xFFEFEBE9),
            Color(0xFF4E342E),
            Icons.Default.Storefront
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .background(color = circleBg, shape = CircleShape)
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = type,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}
