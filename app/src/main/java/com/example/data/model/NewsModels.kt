package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.R

enum class NewsCategory(val id: String, val titleArabic: String, val iconName: String) {
    ALL("all", "الكل", "AllInclusive"),
    POLITICS("politics", "سياسة", "AccountBalance"),
    SPORTS("sports", "رياضة", "SportsSoccer"),
    TECH("tech", "تكنولوجيا", "Memory"),
    ECONOMY("economy", "اقتصاد", "TrendingUp"),
    HEALTH("health", "صحة وعلوم", "HealthAndSafety"),
    CULTURE("culture", "ثقافة ومنوعات", "AutoAwesome");

    companion object {
        fun fromId(id: String): NewsCategory {
            return entries.find { it.id == id } ?: ALL
        }
    }
}

enum class ColorPalette(val id: String, val titleArabic: String, val primaryHex: Long, val secondaryHex: Long) {
    EMERALD("emerald", "زمردي أخضر", 0xFF059669, 0xFF10B981),
    SAPPHIRE("sapphire", "ياقوت أزرق", 0xFF1D4ED8, 0xFF3B82F6),
    RUBY("ruby", "عقيق أحمر", 0xFFDC2626, 0xFFEF4444),
    AMETHYST("amethyst", "بنفسجي ملكي", 0xFF7C3AED, 0xFF8B5CF6),
    AMBER("amber", "عنبر ذهبي", 0xFFD97706, 0xFFF59E0B),
    TEAL("teal", "فيروزي عصري", 0xFF0D9488, 0xFF14B8A6)
}

enum class DarkModeOption(val id: String, val titleArabic: String) {
    SYSTEM("system", "تلقائي حسب النظام"),
    LIGHT("light", "الوضع الفاتح"),
    DARK("dark", "الوضع الداكن المريح"),
    AMOLED("amoled", "سواد تام (AMOLED)")
}

enum class AutoRefreshInterval(val id: String, val titleArabic: String, val intervalSeconds: Long) {
    OFF("off", "إيقاف", 0),
    SEC_30("30s", "كل 30 ثانية", 30),
    MIN_1("1m", "كل دقيقة", 60),
    MIN_5("5m", "كل 5 دقائق", 300)
}

@Entity(tableName = "news_articles")
data class NewsArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val summary: String,
    val content: String,
    val categoryId: String,
    val sourceName: String,
    val sourceUrl: String,
    val imageUrl: String,
    val localDrawableResId: Int = R.drawable.news_world_banner_1787575074316,
    val publishedTimestamp: Long,
    val isBookmarked: Boolean = false,
    val isBreaking: Boolean = false,
    val readCount: Int = 120,
    val readingTimeMinutes: Int = 3,
    val author: String = "هيئة التحرير"
)

@Entity(tableName = "news_sources")
data class NewsSourceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val websiteUrl: String,
    val categoryId: String = "all",
    val colorHex: Long = 0xFF1D4ED8,
    val isEnabled: Boolean = true,
    val isCustom: Boolean = false,
    val articlesCount: Int = 0
)

data class BreakingNotification(
    val id: String,
    val title: String,
    val categoryName: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val articleId: Long? = null
)
