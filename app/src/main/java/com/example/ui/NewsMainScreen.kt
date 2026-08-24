package com.example.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LayersClear
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.NewsArticleEntity
import com.example.data.model.NewsCategory
import com.example.ui.components.AddSourceDialog
import com.example.ui.components.ArticleReaderSheet
import com.example.ui.components.BreakingNewsTicker
import com.example.ui.components.CategoryTabsBar
import com.example.ui.components.NewsArticleCard
import com.example.ui.components.NotificationsSheet
import com.example.ui.components.SmartSearchBar
import com.example.ui.components.SourcesView
import com.example.ui.components.ThemeSettingsContent
import com.example.ui.components.ThemeSettingsSheet
import com.example.ui.components.TopNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsMainScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredArticles by viewModel.filteredArticles.collectAsStateWithLifecycle()
    val allSources by viewModel.allSources.collectAsStateWithLifecycle()
    val categoryCounts by viewModel.categoryCounts.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val unreadNotifCount = notifications.count { !it.isRead }

    LaunchedEffect(uiState.userFeedbackMessage) {
        uiState.userFeedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedback()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopNavBar(
                uiState = uiState,
                unreadNotificationCount = unreadNotifCount,
                onSearchClick = { viewModel.setSearchActive(!uiState.isSearchActive) },
                onNotificationsClick = { viewModel.openNotificationsSheet(true) },
                onThemeClick = { viewModel.openThemeSheet(true) },
                onSourcesClick = { viewModel.selectTab(MainNavigationTab.SOURCES) },
                onRefreshClick = { viewModel.triggerManualRefresh() }
            )
        },
        bottomBar = {
            NewsBottomNav(
                currentTab = uiState.currentTab,
                bookmarksCount = bookmarkedArticles.size,
                onTabSelected = { viewModel.selectTab(it) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar Overlay
            AnimatedVisibility(visible = uiState.isSearchActive) {
                SmartSearchBar(
                    query = uiState.searchQuery,
                    onQueryChanged = { viewModel.updateSearchQuery(it) },
                    onCloseSearch = { viewModel.setSearchActive(false) }
                )
            }

            // Tab Content
            when (uiState.currentTab) {
                MainNavigationTab.FEED -> {
                    FeedTabContent(
                        uiState = uiState,
                        articles = filteredArticles,
                        categoryCounts = categoryCounts,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        onLayoutModeChanged = { viewModel.setLayoutMode(it) },
                        onArticleClick = { viewModel.openArticleReader(it) },
                        onBookmarkToggle = { viewModel.toggleBookmark(it) },
                        onDeleteArticle = { viewModel.deleteArticle(it.id) },
                        onRefresh = { viewModel.triggerManualRefresh() }
                    )
                }

                MainNavigationTab.BOOKMARKS -> {
                    BookmarksTabContent(
                        bookmarkedArticles = filteredArticles,
                        layoutMode = uiState.layoutMode,
                        onArticleClick = { viewModel.openArticleReader(it) },
                        onBookmarkToggle = { viewModel.toggleBookmark(it) },
                        onDeleteArticle = { viewModel.deleteArticle(it.id) },
                        onClearAllBookmarks = { viewModel.clearAllBookmarks() }
                    )
                }

                MainNavigationTab.SOURCES -> {
                    SourcesView(
                        sources = allSources,
                        onToggleSource = { viewModel.toggleSourceEnabled(it) },
                        onDeleteSource = { viewModel.deleteSource(it) },
                        onAddNewSourceClick = { viewModel.openAddSourceDialog(true) }
                    )
                }

                MainNavigationTab.SETTINGS -> {
                    SettingsInlineView(
                        uiState = uiState,
                        onPaletteSelected = { viewModel.setColorPalette(it) },
                        onDarkModeSelected = { viewModel.setDarkMode(it) },
                        onAutoRefreshSelected = { viewModel.setAutoRefreshInterval(it) },
                        onNotificationsToggle = { viewModel.toggleNotifications(it) }
                    )
                }
            }
        }
    }

    // Article Reader Bottom Sheet / Full screen
    if (uiState.activeReaderArticle != null) {
        ArticleReaderSheet(
            article = uiState.activeReaderArticle,
            fontSizeMultiplier = uiState.readerFontSizeMultiplier,
            onFontSizeChanged = { viewModel.setReaderFontSizeMultiplier(it) },
            onBookmarkToggle = { viewModel.toggleBookmark(it) },
            onClose = { viewModel.closeArticleReader() }
        )
    }

    // Add Source Dialog
    AddSourceDialog(
        isOpen = uiState.isAddSourceDialogOpen,
        onDismiss = { viewModel.openAddSourceDialog(false) },
        onConfirm = { name, url, cat, color ->
            viewModel.addCustomSource(name, url, cat, color)
        }
    )

    // Notifications Center Sheet
    NotificationsSheet(
        isOpen = uiState.isNotificationsSheetOpen,
        notifications = notifications,
        onClearAll = { viewModel.clearNotifications() },
        onDismiss = { viewModel.openNotificationsSheet(false) }
    )

    // Theme & Color Settings Sheet
    ThemeSettingsSheet(
        isOpen = uiState.isThemeSheetOpen,
        currentPalette = uiState.colorPalette,
        currentDarkMode = uiState.darkModeOption,
        currentAutoRefresh = uiState.autoRefreshInterval,
        notificationsEnabled = uiState.notificationsEnabled,
        onPaletteSelected = { viewModel.setColorPalette(it) },
        onDarkModeSelected = { viewModel.setDarkMode(it) },
        onAutoRefreshSelected = { viewModel.setAutoRefreshInterval(it) },
        onNotificationsToggle = { viewModel.toggleNotifications(it) },
        onDismiss = { viewModel.openThemeSheet(false) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTabContent(
    uiState: NewsUiState,
    articles: List<NewsArticleEntity>,
    categoryCounts: Map<NewsCategory, Int>,
    onCategorySelected: (NewsCategory) -> Unit,
    onLayoutModeChanged: (FeedLayoutMode) -> Unit,
    onArticleClick: (NewsArticleEntity) -> Unit,
    onBookmarkToggle: (NewsArticleEntity) -> Unit,
    onDeleteArticle: (NewsArticleEntity) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val breakingArticles = articles.filter { it.isBreaking }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Breaking news ticker
            if (breakingArticles.isNotEmpty() && uiState.searchQuery.isEmpty()) {
                BreakingNewsTicker(
                    breakingArticles = breakingArticles,
                    onArticleClick = onArticleClick
                )
            }

            // Categories tabs with dynamic counter badges
            CategoryTabsBar(
                selectedCategory = uiState.selectedCategory,
                categoryCounts = categoryCounts,
                layoutMode = uiState.layoutMode,
                onCategorySelected = onCategorySelected,
                onLayoutModeChanged = onLayoutModeChanged
            )

            // Articles Grid or List
            if (articles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (uiState.searchQuery.isNotEmpty()) "لا توجد نتائج مطابقة لـ \"${uiState.searchQuery}\"" else "لا توجد أخبار في هذا التصنيف حالياً",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (uiState.layoutMode == FeedLayoutMode.GRID_2) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(articles, key = { it.id }) { article ->
                        NewsArticleCard(
                            article = article,
                            layoutMode = FeedLayoutMode.GRID_2,
                            onClick = { onArticleClick(article) },
                            onBookmarkToggle = { onBookmarkToggle(article) },
                            onDeleteClick = { onDeleteArticle(article) }
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(articles, key = { it.id }) { article ->
                        NewsArticleCard(
                            article = article,
                            layoutMode = uiState.layoutMode,
                            onClick = { onArticleClick(article) },
                            onBookmarkToggle = { onBookmarkToggle(article) },
                            onDeleteClick = { onDeleteArticle(article) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookmarksTabContent(
    bookmarkedArticles: List<NewsArticleEntity>,
    layoutMode: FeedLayoutMode,
    onArticleClick: (NewsArticleEntity) -> Unit,
    onBookmarkToggle: (NewsArticleEntity) -> Unit,
    onDeleteArticle: (NewsArticleEntity) -> Unit,
    onClearAllBookmarks: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "الأخبار المحفوظة (المفضلة)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${bookmarkedArticles.size} مقال محفوظ للقراءة لاحقاً",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (bookmarkedArticles.isNotEmpty()) {
                OutlinedButton(
                    onClick = onClearAllBookmarks,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("clear_all_bookmarks_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.LayersClear,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "مسح الكل",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (bookmarkedArticles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.BookmarkRemove,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "لم تقم بحفظ أي أخبار في المفضلة بعد.\nاضغط على أيقونة الإشارة المرجعية لحفظ الأخبار وقراءتها في أي وقت.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(bookmarkedArticles, key = { it.id }) { article ->
                    NewsArticleCard(
                        article = article,
                        layoutMode = layoutMode,
                        onClick = { onArticleClick(article) },
                        onBookmarkToggle = { onBookmarkToggle(article) },
                        onDeleteClick = { onDeleteArticle(article) }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsInlineView(
    uiState: NewsUiState,
    onPaletteSelected: (com.example.data.model.ColorPalette) -> Unit,
    onDarkModeSelected: (com.example.data.model.DarkModeOption) -> Unit,
    onAutoRefreshSelected: (com.example.data.model.AutoRefreshInterval) -> Unit,
    onNotificationsToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "إعدادات التطبيق والتخصيص",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        ThemeSettingsContent(
            currentPalette = uiState.colorPalette,
            currentDarkMode = uiState.darkModeOption,
            currentAutoRefresh = uiState.autoRefreshInterval,
            notificationsEnabled = uiState.notificationsEnabled,
            onPaletteSelected = onPaletteSelected,
            onDarkModeSelected = onDarkModeSelected,
            onAutoRefreshSelected = onAutoRefreshSelected,
            onNotificationsToggle = onNotificationsToggle
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun NewsBottomNav(
    currentTab: MainNavigationTab,
    bookmarksCount: Int,
    onTabSelected: (MainNavigationTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        MainNavigationTab.entries.forEach { tab ->
            val isSelected = tab == currentTab
            val icon: ImageVector = when (tab) {
                MainNavigationTab.FEED -> Icons.Default.Feed
                MainNavigationTab.BOOKMARKS -> Icons.Default.Bookmark
                MainNavigationTab.SOURCES -> Icons.Default.Language
                MainNavigationTab.SETTINGS -> Icons.Default.Tune
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    if (tab == MainNavigationTab.BOOKMARKS && bookmarksCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                ) {
                                    Text(bookmarksCount.toString())
                                }
                            }
                        ) {
                            Icon(imageVector = icon, contentDescription = tab.titleArabic)
                        }
                    } else {
                        Icon(imageVector = icon, contentDescription = tab.titleArabic)
                    }
                },
                label = {
                    Text(
                        text = tab.titleArabic,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
            )
        }
    }
}
