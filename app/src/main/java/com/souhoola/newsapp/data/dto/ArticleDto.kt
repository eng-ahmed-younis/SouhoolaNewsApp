package com.souhoola.newsapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("source")
    val source: SourceDto,
    @SerialName("author")
    val author: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("url")
    val url: String,
    @SerialName("urlToImage")
    val urlToImage: String? = null,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("content")
    val content: String? = null
)