@file:OptIn(ExperimentalLayoutApi::class)

package com.example.moviepostermanagementapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.moviepostermanagementapp.data.model.ContentStatus
import com.example.moviepostermanagementapp.data.model.ContentType
import com.example.moviepostermanagementapp.ui.viewmodel.ContentDetailViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDetailScreen(
    contentId: String,
    onNavigateBack: () -> Unit,
    viewModel: ContentDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showEditSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showFullImage by remember { mutableStateOf(false) }
    var editRating by remember { mutableStateOf(0f) }
    var editNotes by remember { mutableStateOf("") }

    LaunchedEffect(contentId) { viewModel.loadContentItem(contentId) }

    BackHandler(enabled = showEditSheet || showDeleteDialog || showFullImage) {
        when {
            showFullImage -> showFullImage = false
            showEditSheet -> showEditSheet = false
            showDeleteDialog -> showDeleteDialog = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.contentItem?.title ?: "Content details",
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        uiState.contentItem?.let { item ->
                            editRating = item.userRating ?: 0f
                            editNotes = item.userNotes.orEmpty()
                            showEditSheet = true
                        }
                    }) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            uiState.contentItem == null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Content not found", style = MaterialTheme.typography.bodyLarge)
            }
            else -> {
                val contentItem = uiState.contentItem!!
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        HeroSection(
                            item = contentItem,
                            onStatusToggle = { viewModel.toggleStatus() },
                            onImageTap = { showFullImage = true }
                        )
                    }

                    item {
                        FlowRow(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SectionChip(label = "${contentItem.releaseYear ?: "—"}", icon = Icons.Default.CalendarMonth)
                            SectionChip(label = contentItem.genre.joinToString(", ").ifBlank { "Genre TBD" }, icon = Icons.Default.Movie)
                            SectionChip(label = contentItem.runtime?.let { "$it min" } ?: "Runtime N/A", icon = Icons.Default.Timer)
                            SectionChip(label = contentItem.type.friendlyName(), icon = Icons.Default.Info)
                        }
                    }

                    item {
                        InfoCard(header = "Highlights") {
                            HighlightRow(label = "Director", value = contentItem.director)
                            HighlightRow(label = "Cast", value = contentItem.cast.joinToString(", "))
                            HighlightRow(label = "Status", value = contentItem.status.name)
                            HighlightRow(label = "Type", value = contentItem.type.friendlyName())
                        }
                    }

                    item {
                        RatingsSection(
                            imdb = contentItem.imdbRating,
                            rottenTomatoes = contentItem.rottenTomatoesScore,
                            user = contentItem.userRating
                        )
                    }

                    if (!contentItem.description.isNullOrBlank()) {
                        item {
                            InfoCard(header = "Overview") {
                                Text(
                                    text = contentItem.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    item {
                        InfoCard(header = "Personal Notes") {
                            Text(
                                text = contentItem.userNotes
                                    .takeIf { it?.isNotBlank() == true }
                                    ?: "No notes yet. Tap edit to jot down your impressions.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (contentItem.userNotes.isNullOrBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    }

    if (showFullImage && uiState.contentItem != null) {
        FullScreenImageViewer(
            imagePath = uiState.contentItem!!.posterPath,
            title = uiState.contentItem!!.title,
            onDismiss = { showFullImage = false }
        )
    }

    ModalBottomSheetWrapper(
        visible = showEditSheet,
        title = "Edit rating & notes",
        onDismiss = { showEditSheet = false }
    ) {
        var rating by remember { mutableStateOf(editRating) }
        var notes by remember { mutableStateOf(editNotes) }

        LaunchedEffect(editRating, editNotes) {
            rating = editRating
            notes = editNotes
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Your rating", fontWeight = FontWeight.SemiBold)
            StarRatingRow(rating = rating, onRatingSelected = { rating = it })
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Thoughts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp)
            )
            Button(
                onClick = {
                    viewModel.updateUserRatingAndNotes(rating, notes)
                    showEditSheet = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save changes")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete content") },
            text = { Text("Are you sure you want to remove this entry? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteContent()
                        showDeleteDialog = false
                        onNavigateBack()
                    }
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun HeroSection(
    item: com.example.moviepostermanagementapp.data.model.ContentItem,
    onStatusToggle: () -> Unit,
    onImageTap: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(360.dp)) {
        AsyncImage(
            model = item.posterPath,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xCC000000))
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)) {
            Text(item.title, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
            Text(
                text = item.genre.joinToString(" • ").ifBlank { "Genre not available" },
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )
            Spacer(Modifier.height(12.dp))
            AssistChip(
                onClick = onStatusToggle,
                label = {
                    Text(
                        if (item.status == ContentStatus.WATCHED) "Watched" else "In Watchlist"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = if (item.status == ContentStatus.WATCHED) Icons.Default.CheckCircle else Icons.Default.BookmarkBorder,
                        contentDescription = null
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color.White.copy(alpha = 0.15f),
                    labelColor = Color.White
                )
            )
            Spacer(Modifier.height(12.dp))
            TextButton(
                onClick = onImageTap,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
            ) {
                Icon(Icons.Default.ZoomOutMap, contentDescription = "View poster")
                Spacer(Modifier.width(6.dp))
                Text("View poster")
            }
        }
    }
}

@Composable
private fun SectionChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(6.dp))
            Text(label, maxLines = 1, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun InfoCard(header: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        tonalElevation = 3.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(header, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun HighlightRow(label: String, value: String?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Text(
            text = value ?: "N/A",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun RatingsSection(imdb: Float?, rottenTomatoes: Int?, user: Float?) {
    InfoCard("Scores") {
        val cards = mutableListOf<@Composable () -> Unit>()
        imdb?.let {
            cards += {
                RatingTile(
                    title = "IMDb",
                    value = String.format(Locale.getDefault(), "%.1f / 10", it),
                    icon = Icons.Default.Movie
                )
            }
        }
        rottenTomatoes?.let {
            cards += {
                RatingTile(
                    title = "Rotten Tomatoes",
                    value = "$it%",
                    icon = Icons.Default.ThumbUp
                )
            }
        }
        user?.takeIf { it > 0f }?.let {
            cards += {
                RatingTile(
                    title = "You",
                    value = String.format(Locale.getDefault(), "%.1f / 5", it),
                    icon = Icons.Default.Star
                )
            }
        }
        if (cards.isEmpty()) {
            Text(
                "No ratings available yet.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                cards.forEach { it() }
            }
        }
    }
}

@Composable
private fun RatingTile(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .widthIn(min = 140.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(value, fontSize = 13.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalBottomSheetWrapper(
    visible: Boolean,
    title: String,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    if (!visible) return
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Box(
                Modifier
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun FullScreenImageViewer(
    imagePath: String,
    title: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onDismiss() }
    ) {
        AsyncImage(
            model = imagePath,
            contentDescription = title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun StarRatingRow(
    modifier: Modifier = Modifier,
    rating: Float,
    onRatingSelected: (Float) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..5).forEach { star ->
            val isSelected = star <= rating
            IconButton(
                onClick = { onRatingSelected(star.toFloat()) },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = if (isSelected) Color.Yellow else Color.Gray
                )
            }
        }
    }
}

private fun ContentType.friendlyName(): String {
    return when (this) {
        ContentType.MOVIE -> "Movie"
        ContentType.SERIES -> "Series"
    }
}
