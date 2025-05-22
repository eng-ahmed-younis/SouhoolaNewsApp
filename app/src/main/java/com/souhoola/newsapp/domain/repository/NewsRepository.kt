package com.souhoola.newsapp.domain.repository

import androidx.paging.PagingData
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.query.NewsQuery
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for news data operations
 */
interface NewsRepository {

    /**
     * Get a stream of paginated news articles based on the provided query
     */
    fun getNewsStream(
        query: NewsQuery,
        pageSize: Int = 20
    ): Flow<PagingData<Article>>

    /**
     * Get top headlines as a paginated stream
     */
    fun getTopHeadlinesStream(
        country: String = "us",
        category: String? = null,
        query: String? = null,
        pageSize: Int = 20
    ): Flow<PagingData<Article>>

    /**
     * Search for articles as a paginated stream
     */
    fun searchArticlesStream(
        query: String,
        sortBy: String? = null,
        from: String? = null,
        to: String? = null,
        pageSize: Int = 20
    ): Flow<PagingData<Article>>
}