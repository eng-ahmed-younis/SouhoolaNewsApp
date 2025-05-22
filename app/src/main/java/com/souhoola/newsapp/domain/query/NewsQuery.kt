package com.souhoola.newsapp.domain.query


sealed class NewsQuery {
    data class TopHeadlines(
        val country: String = "us",
        val category: String? = null,
        val query: String? = null
    ) : NewsQuery()

    data class Everything(
        val query: String,
        val sortBy: String? = null,
        val from: String? = null,
        val to: String? = null
    ) : NewsQuery()
}