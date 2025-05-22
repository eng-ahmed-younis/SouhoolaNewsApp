package com.souhoola.newsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.souhoola.newsapp.data.mappers.toDomain
import com.souhoola.newsapp.data.remote.NewsApiService
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.query.NewsQuery
import jakarta.inject.Inject


class NewsPagingSource @Inject constructor(
    private val newsApiService: NewsApiService,
    private val query: NewsQuery,
    private val pageSize: Int = 10
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1

        return try {
            val response = when (query) {
                is NewsQuery.TopHeadlines -> {
                    newsApiService.getTopHeadlines(
                        page = page,
                        pageSize = pageSize,
                        country = query.country,
                        category = query.category,
                        query = query.query
                    )
                }
                is NewsQuery.Everything -> {
                    newsApiService.getEverything(
                        page = page,
                        pageSize = pageSize,
                        query = query.query,
                        sortBy = query.sortBy,
                        from = query.from,
                        to = query.to
                    )
                }
            }

            LoadResult.Page(
                data = response.articles.map { it.toDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}