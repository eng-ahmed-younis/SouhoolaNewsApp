package com.souhoola.newsapp.domain.model

enum class SortOption(val apiValue: String, val displayName: String) {
    PUBLISHED_AT("publishedAt", "Newest First"),
    RELEVANCY("relevancy", "Most Relevant"),
    POPULARITY("popularity", "Most Popular");

    companion object {
        fun fromApiValue(value: String?): SortOption {
            return SortOption.entries.find { it.apiValue == value } ?: PUBLISHED_AT
        }
    }
}