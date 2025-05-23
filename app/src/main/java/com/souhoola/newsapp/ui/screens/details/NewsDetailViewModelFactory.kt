package com.souhoola.newsapp.ui.screens.details

import com.souhoola.newsapp.domain.model.Article
import dagger.assisted.AssistedFactory


@AssistedFactory
interface NewsDetailViewModelFactory {
    fun create(article: Article): NewsDetailViewModel
}