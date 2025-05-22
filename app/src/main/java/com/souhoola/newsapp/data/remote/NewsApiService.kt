package com.souhoola.newsapp.data.remote


import com.souhoola.newsapp.data.dto.NewsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsApiService @Inject constructor(
    private val client: HttpClient
) {

    suspend fun getTopHeadlines(
        page: Int = 1,
        pageSize: Int = 20,
        country: String = "us",
        category: String? = null,
        query: String? = null
    ): NewsResponseDto {
        return client.get(urlString = "top-headlines") {
            parameter("page", page)
            parameter("pageSize", pageSize)
            parameter("country", country)
            if (!category.isNullOrBlank()) parameter("category", category)
            if (!query.isNullOrBlank()) parameter("q", query)
        }.body()
    }


    suspend fun getEverything(
        page: Int = 1,
        pageSize: Int = 20,
        query: String,
        sortBy: String? = null,
        from: String? = null,
        to: String? = null
    ): NewsResponseDto {
        return client.get("everything") {
            parameter("page", page)
            parameter("pageSize", pageSize)
            parameter("q", query)
            if (!sortBy.isNullOrBlank()) parameter("sortBy", sortBy)
            if (!from.isNullOrBlank()) parameter("from", from)
            if (!to.isNullOrBlank()) parameter("to", to)
        }.body()
    }
}