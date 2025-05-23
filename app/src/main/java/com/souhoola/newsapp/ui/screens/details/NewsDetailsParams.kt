package com.souhoola.newsapp.ui.screens.details

import com.souhoola.newsapp.domain.model.Article
import kotlinx.serialization.Serializable

@Serializable
data class NewsDetailsParams(
     val article: Article = Article()
)
