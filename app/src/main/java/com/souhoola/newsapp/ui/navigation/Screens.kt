package com.souhoola.newsapp.ui.navigation

import com.souhoola.newsapp.domain.model.Article
import com.souhoola.newsapp.domain.model.Source
import kotlinx.serialization.Serializable


@Serializable
sealed class Screens() {
    @Serializable
    data object NewsList : Screens()

    @Serializable
    data class NewsDetail (
        val id: String? = null,
        val name: String = "",
        val author: String? = null,
        val title: String = "",
        val description: String? = null,
        val url: String = "",
        val urlToImage: String? = null,
        val publishedAt: String = "",
        val content: String? = null

    ): Screens()
}
