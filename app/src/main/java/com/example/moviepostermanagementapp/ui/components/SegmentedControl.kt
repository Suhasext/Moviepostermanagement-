package com.example.moviepostermanagementapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.example.moviepostermanagementapp.ui.theme.*

data class SegmentItem<T>(
    val value: T,
    val label: String,
    val icon: ImageVector? = null
)

@Composable
fun <T> SegmentedControl(
    items: List<SegmentItem<T>>,
    selectedValue: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    indicatorColor: Color = PrimaryPurple
) {
    val selectedIndex = items.indexOfFirst { it.value == selectedValue }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = CardBackground,
                shape = RoundedCornerShape(28.dp)
            )
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onValueChange(item.value) }
                        .then(
                            if (index == selectedIndex) {
                                Modifier
                                    .background(
                                        brush = if (indicatorColor == PrimaryPurple) {
                                            Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryPink))
                                        } else {
                                            Brush.horizontalGradient(listOf(SuccessGreen, Color(0xFF059669)))
                                        },
                                        shape = if (index == 0) {
                                            RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp, topEnd = 4.dp, bottomEnd = 4.dp)
                                        } else if (index == items.size - 1) {
                                            RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp, topStart = 4.dp, bottomStart = 4.dp)
                                        } else {
                                            RoundedCornerShape(4.dp)
                                        }
                                    )
                                    .padding(4.dp)
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.icon != null) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(20.dp),
                                tint = if (index == selectedIndex) TextPrimary else TextSecondary
                            )
                        }
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal,
                            color = if (index == selectedIndex) TextPrimary else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

