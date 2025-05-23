package com.souhoola.newsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val source: Source = Source(),
    val author: String? = null,
    val title: String = "",
    val description: String? = null,
    val url: String = "",
    val urlToImage: String? = null,
    val publishedAt: String = "",
    val content: String? = null
)