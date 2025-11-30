package com.example.moviepostermanagementapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.moviepostermanagementapp.data.model.ContentStatus
import com.example.moviepostermanagementapp.data.model.ContentType
import com.example.moviepostermanagementapp.ui.components.GlassmorphismCard
import com.example.moviepostermanagementapp.ui.components.SegmentItem
import com.example.moviepostermanagementapp.ui.components.SegmentedControl
import com.example.moviepostermanagementapp.ui.theme.*
import com.example.moviepostermanagementapp.ui.viewmodel.AddContentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContentScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddContentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var title by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(ContentType.MOVIE) }
    var selectedStatus by remember { mutableStateOf(ContentStatus.WATCHLIST) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        viewModel.setPosterUri(uri)
    }

    // Navigate back when save is successful
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            viewModel.resetSaveSuccess()
            onNavigateBack()
        }
    }

    // Background gradient
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BackgroundDark, BackgroundSecondary)
                    )
                )
        )

    Scaffold(
        topBar = {
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.height(60.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }
                        Text(
                            "Add Content",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                }
            },
            containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Poster Upload Area
                GlassmorphismCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                    if (selectedImageUri != null) {
                            // Show selected image
                            Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected poster",
                            modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop
                        )
                                // Remove button
                                IconButton(
                                    onClick = { 
                                        selectedImageUri = null
                                        viewModel.setPosterUri(null)
                                    },
                            modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .size(40.dp)
                                        .background(
                                            color = Color.Black.copy(alpha = 0.6f),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = TextPrimary
                                    )
                                }
                            }
                        } else {
                            // Empty state
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.AddAPhoto,
                                    contentDescription = "Add image",
                                    modifier = Modifier.size(64.dp),
                                    tint = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "No poster selected",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Tap to select poster",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                // Select Poster Button
                                Button(
                                    onClick = { imagePickerLauncher.launch("image/*") },
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent
                                    ),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                brush = Brush.horizontalGradient(
                                                    colors = listOf(PrimaryPurple, SecondaryPink)
                                                ),
                                                shape = RoundedCornerShape(24.dp)
                                            ),
                                contentAlignment = Alignment.Center
                            ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                                Icons.Default.AddAPhoto,
                                                contentDescription = null,
                                                tint = TextPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                "Select Poster",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Title Input
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                    Text(
                            "Enter title",
                            color = TextSecondary.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Movie,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground
                    ),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                // Year Input
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Enter year",
                            color = TextSecondary.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Movie,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground
                    ),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                // Type Selector
                SegmentedControl(
                    items = listOf(
                        SegmentItem(ContentType.MOVIE, "Movie", Icons.Default.Movie),
                        SegmentItem(ContentType.SERIES, "Show", Icons.Default.PlayArrow)
                    ),
                    selectedValue = selectedType,
                    onValueChange = { selectedType = it },
                    indicatorColor = PrimaryPurple
                )

                // Status Selector
                SegmentedControl(
                    items = listOf(
                        SegmentItem(ContentStatus.WATCHLIST, "Watchlist", Icons.Default.Queue),
                        SegmentItem(ContentStatus.WATCHED, "Watched", Icons.Default.CheckCircle)
                    ),
                    selectedValue = selectedStatus,
                    onValueChange = { selectedStatus = it },
                    indicatorColor = SuccessGreen
                )

                // Bottom Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Cancel Button
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .border(2.dp, TextSecondary, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextSecondary
                        )
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
            }

            // Save Button
            Button(
                onClick = {
                    if (selectedImageUri != null && title.isNotBlank()) {
                        viewModel.saveContent(
                            title = title,
                            year = year.toIntOrNull(),
                            type = selectedType,
                            status = selectedStatus,
                            posterUri = selectedImageUri!!
                        )
                    }
                },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        enabled = selectedImageUri != null && title.isNotBlank() && !uiState.isSaving,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = TextPrimary,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = TextSecondary.copy(alpha = 0.4f)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(PrimaryPurple, SecondaryPink)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
            ) {
                if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = TextPrimary
                                )
                            } else {
                                Text(
                                    "Save",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                // Error Message
            val currentError = uiState.saveError
            if (currentError != null) {
                Text(
                    text = currentError,
                    color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                )
                }
            }
        }
    }
}
