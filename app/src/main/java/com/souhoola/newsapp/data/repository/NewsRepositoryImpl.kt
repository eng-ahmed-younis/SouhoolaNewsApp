package com.souhoola.newsapp.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.souhoola.newsapp.data.paging.NewsPagingSource
import com.souhoola.newsapp.data.remote.NewsApiService
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.query.NewsQuery
import com.souhoola.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of NewsRepository that uses pagination
 */
@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService
) : NewsRepository {

    override fun getNewsStream(
        query: NewsQuery,
        pageSize: Int
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = pageSize,
                initialLoadSize = pageSize * 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    newsApiService = apiService,
                    query = query,
                    pageSize = pageSize
                )
            }
        ).flow
    }

    override fun getTopHeadlinesStream(
        country: String,
        category: String?,
        query: String?,
        pageSize: Int
    ): Flow<PagingData<Article>> {
        val newsQuery = NewsQuery.TopHeadlines(
            country = country,
            category = category,
            query = query
        )
        return getNewsStream(newsQuery, pageSize)
    }

    override fun searchArticlesStream(
        query: String,
        sortBy: String?,
        from: String?,
        to: String?,
        pageSize: Int
    ): Flow<PagingData<Article>> {
        val newsQuery = NewsQuery.Everything(
            query = query,
            sortBy = sortBy,
            from = from,
            to = to
        )
        return getNewsStream(newsQuery, pageSize)
    }
}