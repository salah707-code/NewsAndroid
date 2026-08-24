package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.NewsArticleEntity
import com.example.data.model.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    // --- Articles ---
    @Query("SELECT * FROM news_articles ORDER BY publishedTimestamp DESC")
    fun getAllArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE categoryId = :categoryId ORDER BY publishedTimestamp DESC")
    fun getArticlesByCategory(categoryId: String): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE isBookmarked = 1 ORDER BY publishedTimestamp DESC")
    fun getBookmarkedArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE isBreaking = 1 ORDER BY publishedTimestamp DESC")
    fun getBreakingArticles(): Flow<List<NewsArticleEntity>>

    @Query("SELECT * FROM news_articles WHERE id = :id LIMIT 1")
    suspend fun getArticleById(id: Long): NewsArticleEntity?

    @Query("SELECT COUNT(*) FROM news_articles WHERE categoryId = :categoryId")
    fun getArticleCountByCategory(categoryId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM news_articles")
    fun getTotalArticleCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: NewsArticleEntity): Long

    @Update
    suspend fun updateArticle(article: NewsArticleEntity)

    @Query("UPDATE news_articles SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun setBookmark(id: Long, isBookmarked: Boolean)

    @Query("UPDATE news_articles SET readCount = readCount + 1 WHERE id = :id")
    suspend fun incrementReadCount(id: Long)

    @Query("DELETE FROM news_articles WHERE id = :id")
    suspend fun deleteArticleById(id: Long)

    @Query("DELETE FROM news_articles WHERE isBookmarked = 1")
    suspend fun clearAllBookmarks()

    // --- Sources ---
    @Query("SELECT * FROM news_sources ORDER BY isCustom DESC, name ASC")
    fun getAllSources(): Flow<List<NewsSourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSources(sources: List<NewsSourceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSource(source: NewsSourceEntity): Long

    @Update
    suspend fun updateSource(source: NewsSourceEntity)

    @Query("UPDATE news_sources SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun setSourceEnabled(id: Long, isEnabled: Boolean)

    @Query("DELETE FROM news_sources WHERE id = :id")
    suspend fun deleteSourceById(id: Long)

    @Query("DELETE FROM news_articles WHERE sourceName = :sourceName")
    suspend fun deleteArticlesBySourceName(sourceName: String)
}
