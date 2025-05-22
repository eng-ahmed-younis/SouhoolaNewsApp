package com.souhoola.newsapp.domain.use_case

import androidx.paging.PagingData
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    operator fun invoke(
        country: String = "us",
        category: String? = null,
        query: String? = null,
        pageSize: Int = 20
    ): Flow<PagingData<Article>> {
        return repository.getTopHeadlinesStream(
            country = country,
            category = category,
            query = query,
            pageSize = pageSize
        )
    }
}
