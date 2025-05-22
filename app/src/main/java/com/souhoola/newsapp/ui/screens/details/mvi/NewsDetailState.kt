package com.souhoola.newsapp.ui.screens.details.mvi

import com.souhoola.newsapp.domain.model.Article

data class NewsDetailState(
    val article: Article? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    // Actions
    val openUrl: String? = null,
    val shareArticle: Article? = null,
)
