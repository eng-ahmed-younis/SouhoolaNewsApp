package com.souhoola.newsapp.ui.screens.details.mvi

import com.souhoola.newsapp.domain.model.Article

sealed class NewsDetailIntent {
    data class ReadFullArticle(val article: Article) : NewsDetailIntent()
    data class ShareArticle(val article: Article) : NewsDetailIntent()
    object ClearOpenUrl : NewsDetailIntent()
    object ClearShareArticle : NewsDetailIntent()
    data class LoadArticle(val article: Article) : NewsDetailIntent()
}