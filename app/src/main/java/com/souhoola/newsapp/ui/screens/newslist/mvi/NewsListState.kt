package com.souhoola.newsapp.ui.screens.newslist.mvi

import androidx.paging.PagingData
import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.model.SortOption

// State to hold UI data and navigation events
data class NewsListState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedSortOption: SortOption = SortOption.PUBLISHED_AT,
    val articles: kotlinx.coroutines.flow.Flow<PagingData<Article>> = kotlinx.coroutines.flow.emptyFlow(),
    val isConnected: Boolean = true,
    // Navigation state
    val articleToNavigateTo: Article? = null,
    val showErrorSnackbar: String? = null
)