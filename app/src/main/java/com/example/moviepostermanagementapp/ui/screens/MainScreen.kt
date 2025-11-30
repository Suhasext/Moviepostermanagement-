package com.example.moviepostermanagementapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviepostermanagementapp.data.model.ContentItem
import com.example.moviepostermanagementapp.ui.components.ContentItemCard
import com.example.moviepostermanagementapp.ui.theme.BackgroundDark
import com.example.moviepostermanagementapp.ui.theme.BackgroundSecondary
import com.example.moviepostermanagementapp.ui.theme.PrimaryPurple
import com.example.moviepostermanagementapp.ui.theme.TextPrimary
import com.example.moviepostermanagementapp.ui.theme.TextSecondary
import com.example.moviepostermanagementapp.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToContentDetail: (String) -> Unit,
    onNavigateToAddContent: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(Screen.Home) }
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedWatchlistTab by remember { mutableStateOf(ContentTypeTab.Movies) }
    var selectedWatchedTab by remember { mutableStateOf(ContentTypeTab.Movies) }

    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    if (isSearchVisible) {
        BackHandler {
            isSearchVisible = false
            searchQuery = ""
        }
    }

    LaunchedEffect(selectedTab) {
        if (selectedTab == Screen.Home) {
            viewModel.shuffleItems()
        }
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(visible = isSearchVisible) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onBackClick = {
                        isSearchVisible = false
                        searchQuery = ""
                    }
                )
            }
        },
        bottomBar = {
            AppBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onAddClick = onNavigateToAddContent,
                onSearchClick = { isSearchVisible = true }
            )
        },
        floatingActionButton = {
            if (selectedTab == Screen.Home) {
                CompletionFab(
                    completedMovies = uiState.completedMovies,
                    completedShows = uiState.completedShows,
                    onClick = { scope.launch { bottomSheetState.show() } }
                )
            }
        },
        containerColor = Color.Transparent,
        modifier = Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(BackgroundDark, BackgroundSecondary)
            )
        )
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                val baseItems = when (selectedTab) {
                    Screen.Home -> uiState.shuffledItems
                    Screen.Watchlist -> if (selectedWatchlistTab == ContentTypeTab.Movies) uiState.watchlistMovies else uiState.watchlistShows
                    Screen.Watched -> if (selectedWatchedTab == ContentTypeTab.Movies) uiState.watchedMovies else uiState.watchedShows
                }

                val itemsToShow = remember(searchQuery, baseItems) {
                    if (searchQuery.isBlank()) {
                        baseItems
                    } else {
                        baseItems.filter {
                            it.title.contains(searchQuery, ignoreCase = true) ||
                                    it.description?.contains(searchQuery, ignoreCase = true) == true
                        }
                    }
                }

                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    itemsToShow.isEmpty() -> {
                        EmptyStateView(
                            screen = selectedTab,
                            watchlistTab = selectedWatchlistTab,
                            watchedTab = selectedWatchedTab,
                            searchQuery = searchQuery,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        ContentStaggeredGrid(
                            contentItems = itemsToShow,
                            onContentClick = onNavigateToContentDetail,
                            onStatusToggle = { viewModel.toggleContentStatus(it) }
                        )
                    }
                }
            }

            if (selectedTab == Screen.Watchlist || selectedTab == Screen.Watched) {
                val tabs = ContentTypeTab.values()
                val selectedContentType = if (selectedTab == Screen.Watchlist) selectedWatchlistTab else selectedWatchedTab
                val onTabSelected: (ContentTypeTab) -> Unit = {
                    if (selectedTab == Screen.Watchlist) {
                        selectedWatchlistTab = it
                    } else {
                        selectedWatchedTab = it
                    }
                }
                ContentTypeFilter(
                    tabs = tabs,
                    selectedTab = selectedContentType,
                    onTabSelected = onTabSelected
                )
            }
        }

        if (bottomSheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = { scope.launch { bottomSheetState.hide() } },
                sheetState = bottomSheetState
            ) {
                CompletionBreakdown(
                    completedMovies = uiState.completedMovies,
                    completedShows = uiState.completedShows
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentTypeFilter(
    tabs: Array<ContentTypeTab>,
    selectedTab: ContentTypeTab,
    onTabSelected: (ContentTypeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = tabs.size),
                onClick = { onTabSelected(tab) },
                selected = tab == selectedTab
            ) {
                Text(tab.name)
            }
        }
    }
}


@Composable
private fun CompletionFab(completedMovies: Int, completedShows: Int, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = PrimaryPurple
    ) {
        Text(
            text = "${completedMovies + completedShows}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun CompletionBreakdown(completedMovies: Int, completedShows: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Completed Movies: $completedMovies", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Completed TV Shows: $completedShows", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(BackgroundSecondary, RoundedCornerShape(24.dp))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary
            )
        }
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search...", color = TextSecondary) },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = PrimaryPurple,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
        if (query.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Clear Search",
                    tint = TextPrimary
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun ContentStaggeredGrid(
    contentItems: List<ContentItem>,
    onContentClick: (String) -> Unit,
    onStatusToggle: (ContentItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = contentItems,
            key = { it.id }
        ) { contentItem ->
            ContentItemCard(
                modifier = Modifier.animateItem(fadeInSpec = tween(300), placementSpec = tween(300)),
                contentItem = contentItem,
                onClick = { onContentClick(contentItem.id) },
                onStatusToggle = { onStatusToggle(contentItem) }
            )
        }
    }
}

@Composable
private fun AppBottomNavigation(
    selectedTab: Screen,
    onTabSelected: (Screen) -> Unit,
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    BottomAppBar(
        containerColor = BackgroundSecondary.copy(alpha = 0.9f),
        contentColor = TextPrimary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBarItem(
                selected = selectedTab == Screen.Home,
                onClick = { onTabSelected(Screen.Home) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    unselectedIconColor = TextSecondary,
                    selectedTextColor = PrimaryPurple,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedTab == Screen.Watchlist,
                onClick = { onTabSelected(Screen.Watchlist) },
                icon = { Icon(Icons.Default.List, contentDescription = "Watchlist") },
                label = { Text("Watchlist") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    unselectedIconColor = TextSecondary,
                    selectedTextColor = PrimaryPurple,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                selected = selectedTab == Screen.Watched,
                onClick = { onTabSelected(Screen.Watched) },
                icon = { Icon(Icons.Default.Check, contentDescription = "Watched") },
                label = { Text("Watched") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    unselectedIconColor = TextSecondary,
                    selectedTextColor = PrimaryPurple,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PrimaryPurple,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Content", tint = Color.White)
            }
            NavigationBarItem(
                selected = false,
                onClick = onSearchClick,
                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("Search") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    unselectedIconColor = TextSecondary,
                    selectedTextColor = PrimaryPurple,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun EmptyStateView(
    screen: Screen,
    watchlistTab: ContentTypeTab,
    watchedTab: ContentTypeTab,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val (icon, text, subtext) = when {
            searchQuery.isNotEmpty() -> Triple(Icons.Default.Search, "No Results", "Try a different search term")
            screen == Screen.Home -> Triple(Icons.Default.Home, "No Items", "Add some items to get started.")
            screen == Screen.Watchlist && watchlistTab == ContentTypeTab.Movies -> Triple(Icons.Default.Movie, "No Movies in Watchlist", "Add movies to your watchlist.")
            screen == Screen.Watchlist && watchlistTab == ContentTypeTab.Shows -> Triple(Icons.Default.Tv, "No Shows in Watchlist", "Add TV shows to your watchlist.")
            screen == Screen.Watched && watchedTab == ContentTypeTab.Movies -> Triple(Icons.Default.Movie, "No Watched Movies", "Mark movies as watched to see them here.")
            screen == Screen.Watched && watchedTab == ContentTypeTab.Shows -> Triple(Icons.Default.Tv, "No Watched Shows", "Mark TV shows as watched to see them here.")
            else -> Triple(Icons.Default.List, "Nothing to show", "Add and watch some content!")
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = TextSecondary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtext,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
        )
    }
}

private enum class Screen {
    Home,
    Watchlist,
    Watched
}

private enum class ContentTypeTab {
    Movies,
    Shows
}
