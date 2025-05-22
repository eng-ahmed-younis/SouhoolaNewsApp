package com.souhoola.newsapp.data.base

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

class AppClientRequestException(
    response: HttpResponse,
     errorMessage: String,
    val result: Any? = null
) : ResponseException(response, errorMessage) {
    override val message: String =errorMessage
}

class AppRedirectResponseException(
    response: HttpResponse,
     errorMessage: String,
    val errorCode: Int,
    val result: Any? = null
) : ResponseException(response, errorMessage) {
    override val message: String =errorMessage
}

class AppServerResponseException(
    response: HttpResponse,
     errorMessage: String,
    val errorCode: Int,
    val result: Any? = null
) : ResponseException(response, errorMessage) {
    override val message: String =errorMessage
}
