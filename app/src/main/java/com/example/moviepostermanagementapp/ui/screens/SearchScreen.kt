package com.example.moviepostermanagementapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviepostermanagementapp.ui.components.ContentItemCard
import com.example.moviepostermanagementapp.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToContentDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // This is the core fix for the back button behavior
    BackHandler(enabled = active) {
        active = false
        keyboardController?.hide()
    }

    // Clear search results when the screen is first shown or search becomes inactive
    LaunchedEffect(active) {
        if (!active) {
            viewModel.clearSearch()
        }
    }

    Scaffold { padding ->
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(horizontal = 16.dp),
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                keyboardController?.hide()
                viewModel.searchContent(it)
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text("Search your collection") },
            leadingIcon = {
                if (active) {
                    IconButton(onClick = {
                        active = false
                        keyboardController?.hide()
                        query = ""
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                } else {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                }
            },
            trailingIcon = {
                if (active && query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear query")
                    }
                }
            }
        ) {
            // Display search results
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.searchResults.isEmpty() && query.isNotEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results found for \'''$query\'''")
                }
            } else {
                 LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.searchResults,
                        key = { it.id }
                    ) { contentItem ->
                        ContentItemCard(
                            contentItem = contentItem,
                            onClick = { onNavigateToContentDetail(contentItem.id) },
                            onStatusToggle = { viewModel.toggleContentStatus(contentItem) }
                        )
                    }
                }
            }
        }
    }
}
