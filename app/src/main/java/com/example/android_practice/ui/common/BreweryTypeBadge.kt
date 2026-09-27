package com.example.android_practice.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun BreweryTypeBadge(
    type: String,
    modifier: Modifier = Modifier
) {
    val (label, bgColor, textColor) = when (type.lowercase(Locale.ROOT)) {
        "micro" -> Triple(
            "Микропивоварня",
            Color(0xFFFFF3E0),
            Color(0xFFD87815)
        )
        "brewpub" -> Triple(
            "Брюпаб",
            Color(0xFFE8F5E9),
            Color(0xFF2E7D32)
        )
        "regional" -> Triple(
            "Региональная",
            Color(0xFFE3F2FD),
            Color(0xFF1565C0)
        )
        "large" -> Triple(
            "Крупная",
            Color(0xFFF3E5F5),
            Color(0xFF7B1FA2)
        )
        "closed" -> Triple(
            "Закрыта",
            Color(0xFFFFEBEE),
            Color(0xFFC62828)
        )
        "contract" -> Triple(
            "Контрактная",
            Color(0xFFE0F2F1),
            Color(0xFF00695C)
        )
        "proprietor" -> Triple(
            "Частная",
            Color(0xFFEFEBE9),
            Color(0xFF4E342E)
        )
        else -> Triple(
            type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            Color(0xFFF5F5F5),
            Color(0xFF616161)
        )
    }

    Surface(
        modifier = modifier,
        color = bgColor,
        contentColor = textColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
