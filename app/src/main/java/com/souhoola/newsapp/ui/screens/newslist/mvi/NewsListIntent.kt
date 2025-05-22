package com.souhoola.newsapp.ui.screens.newslist.mvi

import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.model.SortOption

sealed class NewsListIntent {
    object LoadArticles : NewsListIntent()
    data class SearchNews(val query: String) : NewsListIntent()
    data class SelectSortOption(val option: SortOption) : NewsListIntent()
    data class ClickArticle(val article: Article) : NewsListIntent()
    data class ConnectionChange(val isConnected: Boolean) : NewsListIntent()
    object ClearNavigation : NewsListIntent()
    object ClearSnackbar : NewsListIntent()
}