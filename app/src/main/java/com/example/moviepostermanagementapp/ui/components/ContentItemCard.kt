package com.example.moviepostermanagementapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviepostermanagementapp.data.model.ContentItem
import com.example.moviepostermanagementapp.data.model.ContentStatus
import com.example.moviepostermanagementapp.data.model.ContentType

@Composable
fun ContentItemCard(
    contentItem: ContentItem,
    onClick: () -> Unit,
    onStatusToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = contentItem.posterPath,
                contentDescription = contentItem.title,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            if (contentItem.status == ContentStatus.WATCHED) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Watched",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(50))
                        .padding(4.dp)
                )
            }

            TypeBadge(
                type = contentItem.type,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun TypeBadge(
    type: ContentType,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.Black.copy(alpha = 0.75f),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (type == ContentType.MOVIE) Icons.Default.Movie else Icons.Default.Tv,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.White
            )
            Text(
                text = if (type == ContentType.MOVIE) "Movie" else "TV Show",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
