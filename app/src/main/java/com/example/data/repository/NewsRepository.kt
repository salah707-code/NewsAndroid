package com.example.data.repository

import com.example.R
import com.example.data.local.NewsDao
import com.example.data.model.BreakingNotification
import com.example.data.model.NewsArticleEntity
import com.example.data.model.NewsCategory
import com.example.data.model.NewsSourceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class NewsRepository(
    private val newsDao: NewsDao,
    private val scope: CoroutineScope
) {
    val allArticles: Flow<List<NewsArticleEntity>> = newsDao.getAllArticles()
    val bookmarkedArticles: Flow<List<NewsArticleEntity>> = newsDao.getBookmarkedArticles()
    val breakingArticles: Flow<List<NewsArticleEntity>> = newsDao.getBreakingArticles()
    val allSources: Flow<List<NewsSourceEntity>> = newsDao.getAllSources()

    private val _notifications = MutableStateFlow<List<BreakingNotification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    init {
        // Initial in-app notifications
        val now = System.currentTimeMillis()
        _notifications.value = listOf(
            BreakingNotification(
                id = UUID.randomUUID().toString(),
                title = "عاجل: قمة دولية كبرى لبحث استقرار أسواق الطاقة والأمن العالمي",
                categoryName = "سياسة",
                timestamp = now - 15 * 60 * 1000,
                isRead = false,
                articleId = 1L
            ),
            BreakingNotification(
                id = UUID.randomUUID().toString(),
                title = "عاجل: ريال مدريد ومانشستر سيتي في مواجهة نارية لحسم بطاقة العبور",
                categoryName = "رياضة",
                timestamp = now - 35 * 60 * 1000,
                isRead = false,
                articleId = 3L
            ),
            BreakingNotification(
                id = UUID.randomUUID().toString(),
                title = "عاجل: ارتفاع قياسي لأسعار الذهب عالمياً",
                categoryName = "اقتصاد",
                timestamp = now - 60 * 60 * 1000,
                isRead = true,
                articleId = 7L
            )
        )
    }

    // Dynamic category counts flow
    val categoryCounts: Flow<Map<NewsCategory, Int>> = allArticles.combine(allSources) { articles, sources ->
        val enabledSourceNames = sources.filter { it.isEnabled }.map { it.name }.toSet()
        val visibleArticles = if (enabledSourceNames.isEmpty()) articles else articles.filter { it.sourceName in enabledSourceNames }

        val map = mutableMapOf<NewsCategory, Int>()
        map[NewsCategory.ALL] = visibleArticles.size
        NewsCategory.entries.filter { it != NewsCategory.ALL }.forEach { cat ->
            map[cat] = visibleArticles.count { it.categoryId == cat.id }
        }
        map
    }

    suspend fun toggleBookmark(articleId: Long, currentStatus: Boolean) = withContext(Dispatchers.IO) {
        newsDao.setBookmark(articleId, !currentStatus)
    }

    suspend fun deleteArticle(articleId: Long) = withContext(Dispatchers.IO) {
        newsDao.deleteArticleById(articleId)
    }

    suspend fun clearAllBookmarks() = withContext(Dispatchers.IO) {
        newsDao.clearAllBookmarks()
    }

    suspend fun incrementReadCount(articleId: Long) = withContext(Dispatchers.IO) {
        newsDao.incrementReadCount(articleId)
    }

    suspend fun addCustomSource(
        name: String,
        url: String,
        categoryId: String,
        colorHex: Long
    ): Boolean = withContext(Dispatchers.IO) {
        if (name.isBlank() || url.isBlank()) return@withContext false

        val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }

        val source = NewsSourceEntity(
            name = name.trim(),
            websiteUrl = formattedUrl.trim(),
            categoryId = categoryId,
            colorHex = colorHex,
            isEnabled = true,
            isCustom = true
        )
        val insertedId = newsDao.insertSource(source)

        // Generate a welcome / inaugural news article for this newly added source
        val now = System.currentTimeMillis()
        val categoryEnum = NewsCategory.fromId(categoryId)
        val newArticle = NewsArticleEntity(
            title = "تغطية خاصة وحصرية من موقع ${source.name}",
            summary = "متابعة مستمرة لأحدث التطورات والتحليلات الإخبارية المنشورة مباشرة عبر موقع ${source.name}.",
            content = """تمت إضافة موقع "${source.name}" بنجاح إلى شبكة مصادر نبض الأخبار الخاصة بك.

يمكنك الآن متابعة التحديثات المباشرة والأخبار العاجلة والتقارير الميدانية فور نشرها عبر الرابط:
${source.websiteUrl}

يغطي هذا المصدر تصنيف (${categoryEnum.titleArabic}) مع تحديث فوري للأخبار والمقالات الحصرية.""",
            categoryId = if (categoryId == "all") NewsCategory.POLITICS.id else categoryId,
            sourceName = source.name,
            sourceUrl = source.websiteUrl,
            imageUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=800&q=80",
            localDrawableResId = R.drawable.news_world_banner_1787575074316,
            publishedTimestamp = now,
            isBookmarked = false,
            isBreaking = true,
            readCount = 45,
            readingTimeMinutes = 2,
            author = "مراسل ${source.name}"
        )
        newsDao.insertArticle(newArticle)

        // Add in-app breaking notification
        val notification = BreakingNotification(
            id = UUID.randomUUID().toString(),
            title = "تمت إضافة مصدر جديد: ${source.name}",
            categoryName = categoryEnum.titleArabic,
            timestamp = now,
            isRead = false,
            articleId = null
        )
        _notifications.value = listOf(notification) + _notifications.value

        insertedId > 0
    }

    suspend fun deleteSource(source: NewsSourceEntity) = withContext(Dispatchers.IO) {
        newsDao.deleteSourceById(source.id)
        // Also remove articles belonging to this source if user deletes it
        newsDao.deleteArticlesBySourceName(source.name)
    }

    suspend fun toggleSourceEnabled(sourceId: Long, currentEnabled: Boolean) = withContext(Dispatchers.IO) {
        newsDao.setSourceEnabled(sourceId, !currentEnabled)
    }

    suspend fun simulateLiveRefresh(): String = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val randomPool = listOf(
            Triple(
                "اكتشاف فلكي مذهل: تلسكوب الفضاء يرصد كوكباً مائياً في نظام شمسي مجاور",
                "رصد كوكب بحجم الأرض يحتوي غلافه الجوي على مؤشرات بخار ماء معتدل الحرارة تثير اهتمام العلماء.",
                NewsCategory.TECH
            ),
            Triple(
                "نهائي كأس أبطال القارات: التشكيلة الرسمية والخطط التكتيكية للمدربين",
                "استعدادات استثنائية وجماهير غفيرة تحتشد لمتابعة المواجهة الكروية الكبرى.",
                NewsCategory.SPORTS
            ),
            Triple(
                "اتفاقية تجارة حرة تاريخية لخفض الرسوم الجمركية ودعم الصادرات الإقليمية",
                "توقيع حزمة شراكات استراتيجية لدعم التبادل التجاري وتسهيل حركة البضائع بين الدول.",
                NewsCategory.ECONOMY
            ),
            Triple(
                "ابتكار جيل جديد من اللقاحات الذكية ذات الحماية الشاملة وطويلة الأمد",
                "أبحاث علمية رائدة تعتمد تقنية النانو الحيوية للقضاء على مسببات الأمراض المستعصية.",
                NewsCategory.HEALTH
            ),
            Triple(
                "انطلاق المهرجان الثقافي الدولي بمشاركة أدباء ومفكرين من 50 دولة",
                "فعاليات فكرية ومعارض كتب وأمسيات شعرية تسلط الضوء على الإرث الأدبي الإنساني.",
                NewsCategory.CULTURE
            )
        )

        val item = randomPool.random()
        val freshArticle = NewsArticleEntity(
            title = item.first,
            summary = item.second,
            content = """${item.second}

وتشير التقارير الأولية الواردة إلى أهمية هذا الحدث في سياق التطورات الراهنة، مع تفاعل واسع من قبل المتخصصين والمتابعين على مستوى العالم.

وسنوافيكم بكافة التفاصيل والمستجدات والتحليلات الحصرية فور ورودها في نشراتنا القادمة.""",
            categoryId = item.third.id,
            sourceName = "الجزيرة نت",
            sourceUrl = "https://www.aljazeera.net",
            imageUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?w=800&q=80",
            localDrawableResId = when (item.third) {
                NewsCategory.SPORTS -> R.drawable.news_sports_banner_1787575039663
                NewsCategory.TECH -> R.drawable.news_tech_banner_1787575055120
                else -> R.drawable.news_world_banner_1787575074316
            },
            publishedTimestamp = now,
            isBookmarked = false,
            isBreaking = true,
            readCount = 75,
            readingTimeMinutes = 3,
            author = "نشرة الأخبار العاجلة"
        )

        newsDao.insertArticle(freshArticle)

        val notif = BreakingNotification(
            id = UUID.randomUUID().toString(),
            title = "خبر عاجل جديد: ${freshArticle.title}",
            categoryName = item.third.titleArabic,
            timestamp = now,
            isRead = false,
            articleId = null
        )
        _notifications.value = listOf(notif) + _notifications.value

        "تم تحديث الأخبار بنجاح: تم إضافة خبر عاجل جديد"
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }
}
