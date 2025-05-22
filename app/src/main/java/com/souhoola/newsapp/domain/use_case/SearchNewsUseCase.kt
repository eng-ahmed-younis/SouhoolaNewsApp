package com.souhoola.newsapp.domain.use_case

import androidx.paging.PagingData
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.model.SortOption
import com.souhoola.newsapp.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    operator fun invoke(
        query: String,
        sortOption: SortOption = SortOption.PUBLISHED_AT,
        from: String? = null,
        to: String? = null,
        pageSize: Int = 20
    ): Flow<PagingData<Article>> {
        return repository.searchArticlesStream(
            query = query,
            sortBy = sortOption.apiValue,
            from = from,
            to = to,
            pageSize = pageSize
        )
    }
}