package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AutoRefreshInterval
import com.example.data.model.BreakingNotification
import com.example.data.model.ColorPalette
import com.example.data.model.DarkModeOption
import com.example.data.model.NewsArticleEntity
import com.example.data.model.NewsCategory
import com.example.data.model.NewsSourceEntity
import com.example.data.repository.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class MainNavigationTab(val titleArabic: String, val iconName: String) {
    FEED("الأخبار", "Feed"),
    BOOKMARKS("المفضلة", "Bookmark"),
    SOURCES("المصادر", "Language"),
    SETTINGS("الإعدادات", "Tune")
}

enum class FeedLayoutMode {
    BIG_CARD,
    COMPACT_LIST,
    GRID_2
}

data class NewsUiState(
    val currentTab: MainNavigationTab = MainNavigationTab.FEED,
    val selectedCategory: NewsCategory = NewsCategory.ALL,
    val selectedSourceFilterId: Long? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val layoutMode: FeedLayoutMode = FeedLayoutMode.BIG_CARD,
    val colorPalette: ColorPalette = ColorPalette.EMERALD,
    val darkModeOption: DarkModeOption = DarkModeOption.SYSTEM,
    val autoRefreshInterval: AutoRefreshInterval = AutoRefreshInterval.SEC_30,
    val notificationsEnabled: Boolean = true,
    val readerFontSizeMultiplier: Float = 1.0f,
    val isRefreshing: Boolean = false,
    val activeReaderArticle: NewsArticleEntity? = null,
    val isAddSourceDialogOpen: Boolean = false,
    val isNotificationsSheetOpen: Boolean = false,
    val isThemeSheetOpen: Boolean = false,
    val userFeedbackMessage: String? = null,
    val lastRefreshTime: Long = System.currentTimeMillis()
)

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = NewsRepository(database.newsDao(), viewModelScope)

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    val allArticles = repository.allArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedArticles = repository.bookmarkedArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSources = repository.allSources
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoryCounts = repository.categoryCounts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val notifications = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered articles based on tab, category, search query, and enabled sources
    val filteredArticles: StateFlow<List<NewsArticleEntity>> = combine(
        allArticles,
        bookmarkedArticles,
        allSources,
        _uiState
    ) { articles, bookmarks, sources, state ->
        val baseList = if (state.currentTab == MainNavigationTab.BOOKMARKS) bookmarks else articles

        val enabledSourceNames = sources.filter { it.isEnabled }.map { it.name }.toSet()
        var result = if (enabledSourceNames.isEmpty()) baseList else baseList.filter { it.sourceName in enabledSourceNames }

        // Category filter
        if (state.selectedCategory != NewsCategory.ALL) {
            result = result.filter { it.categoryId == state.selectedCategory.id }
        }

        // Source filter
        if (state.selectedSourceFilterId != null) {
            val targetSource = sources.find { it.id == state.selectedSourceFilterId }
            if (targetSource != null) {
                result = result.filter { it.sourceName == targetSource.name }
            }
        }

        // Search filter
        if (state.searchQuery.isNotBlank()) {
            val query = state.searchQuery.trim().lowercase()
            result = result.filter {
                it.title.lowercase().contains(query) ||
                        it.summary.lowercase().contains(query) ||
                        it.sourceName.lowercase().contains(query) ||
                        it.author.lowercase().contains(query)
            }
        }

        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var autoRefreshJob: Job? = null

    init {
        startAutoRefreshLoop()
    }

    private fun startAutoRefreshLoop() {
        autoRefreshJob?.cancel()
        val interval = _uiState.value.autoRefreshInterval
        if (interval == AutoRefreshInterval.OFF) return

        autoRefreshJob = viewModelScope.launch {
            while (isActive) {
                delay(interval.intervalSeconds * 1000L)
                if (_uiState.value.autoRefreshInterval != AutoRefreshInterval.OFF) {
                    repository.simulateLiveRefresh()
                    _uiState.value = _uiState.value.copy(lastRefreshTime = System.currentTimeMillis())
                }
            }
        }
    }

    fun selectTab(tab: MainNavigationTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun selectCategory(category: NewsCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectSourceFilter(sourceId: Long?) {
        _uiState.value = _uiState.value.copy(selectedSourceFilterId = sourceId)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setSearchActive(active: Boolean) {
        _uiState.value = _uiState.value.copy(
            isSearchActive = active,
            searchQuery = if (!active) "" else _uiState.value.searchQuery
        )
    }

    fun setLayoutMode(mode: FeedLayoutMode) {
        _uiState.value = _uiState.value.copy(layoutMode = mode)
    }

    fun setColorPalette(palette: ColorPalette) {
        _uiState.value = _uiState.value.copy(colorPalette = palette)
    }

    fun setDarkMode(option: DarkModeOption) {
        _uiState.value = _uiState.value.copy(darkModeOption = option)
    }

    fun setAutoRefreshInterval(interval: AutoRefreshInterval) {
        _uiState.value = _uiState.value.copy(autoRefreshInterval = interval)
        startAutoRefreshLoop()
    }

    fun toggleNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }

    fun setReaderFontSizeMultiplier(multiplier: Float) {
        _uiState.value = _uiState.value.copy(
            readerFontSizeMultiplier = multiplier.coerceIn(0.8f, 1.6f)
        )
    }

    fun openArticleReader(article: NewsArticleEntity) {
        viewModelScope.launch {
            repository.incrementReadCount(article.id)
        }
        _uiState.value = _uiState.value.copy(activeReaderArticle = article)
    }

    fun closeArticleReader() {
        _uiState.value = _uiState.value.copy(activeReaderArticle = null)
    }

    fun toggleBookmark(article: NewsArticleEntity) {
        viewModelScope.launch {
            repository.toggleBookmark(article.id, article.isBookmarked)
            // Update active reader article if open
            if (_uiState.value.activeReaderArticle?.id == article.id) {
                _uiState.value = _uiState.value.copy(
                    activeReaderArticle = article.copy(isBookmarked = !article.isBookmarked)
                )
            }
        }
    }

    fun deleteArticle(articleId: Long) {
        viewModelScope.launch {
            repository.deleteArticle(articleId)
            if (_uiState.value.activeReaderArticle?.id == articleId) {
                _uiState.value = _uiState.value.copy(activeReaderArticle = null)
            }
            showFeedback("تم حذف الخبر بنجاح")
        }
    }

    fun clearAllBookmarks() {
        viewModelScope.launch {
            repository.clearAllBookmarks()
            showFeedback("تم مسح كافة الأخبار المحفوظة")
        }
    }

    fun triggerManualRefresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            val msg = repository.simulateLiveRefresh()
            delay(600)
            _uiState.value = _uiState.value.copy(
                isRefreshing = false,
                lastRefreshTime = System.currentTimeMillis()
            )
            showFeedback(msg)
        }
    }

    // Sources management
    fun openAddSourceDialog(open: Boolean) {
        _uiState.value = _uiState.value.copy(isAddSourceDialogOpen = open)
    }

    fun addCustomSource(name: String, url: String, categoryId: String, colorHex: Long) {
        viewModelScope.launch {
            val success = repository.addCustomSource(name, url, categoryId, colorHex)
            if (success) {
                _uiState.value = _uiState.value.copy(isAddSourceDialogOpen = false)
                showFeedback("تمت إضافة موقع \"$name\" بنجاح!")
            } else {
                showFeedback("يرجى إدخال اسم ورابط موقع صالح")
            }
        }
    }

    fun deleteSource(source: NewsSourceEntity) {
        viewModelScope.launch {
            repository.deleteSource(source)
            showFeedback("تم حذف موقع \"${source.name}\" وإزالة أخباره")
        }
    }

    fun toggleSourceEnabled(source: NewsSourceEntity) {
        viewModelScope.launch {
            repository.toggleSourceEnabled(source.id, source.isEnabled)
            val status = if (!source.isEnabled) "تفعيل" else "تعطيل"
            showFeedback("تم $status مصدر \"${source.name}\"")
        }
    }

    // Notifications & Sheets
    fun openNotificationsSheet(open: Boolean) {
        if (open) {
            repository.markAllNotificationsRead()
        }
        _uiState.value = _uiState.value.copy(isNotificationsSheetOpen = open)
    }

    fun openThemeSheet(open: Boolean) {
        _uiState.value = _uiState.value.copy(isThemeSheetOpen = open)
    }

    fun clearNotifications() {
        repository.clearNotifications()
    }

    private fun showFeedback(msg: String) {
        _uiState.value = _uiState.value.copy(userFeedbackMessage = msg)
    }

    fun clearFeedback() {
        _uiState.value = _uiState.value.copy(userFeedbackMessage = null)
    }
}
