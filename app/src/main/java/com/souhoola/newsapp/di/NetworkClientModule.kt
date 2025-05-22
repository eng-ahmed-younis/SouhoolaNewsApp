package com.souhoola.newsapp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.souhoola.newsapp.BuildConfig
import com.souhoola.newsapp.data.base.AppClientRequestException
import com.souhoola.newsapp.data.base.AppRedirectResponseException
import com.souhoola.newsapp.data.base.AppServerResponseException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import com.souhoola.newsapp.data.base.Error
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkClientModule {

    @Singleton
    @Provides
    fun chuckerInterceptor(@ApplicationContext context: Context) =
        ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()


    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideJson() = Json {
        isLenient = true
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = false
        prettyPrint = true
        coerceInputValues = true
        allowStructuredMapKeys = true
        explicitNulls = true
    }


    private fun provideHttpClient(
        json: Json,
        chuckerInterceptor: ChuckerInterceptor
    ): HttpClient =
        HttpClient(OkHttp) {
            engine {
                addInterceptor(chuckerInterceptor)
            }

            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HttpClient: $message")
                    }
                }
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildConfig.NEWS_API_BASE_URL
                }
                contentType(ContentType.Application.Json)
                BuildConfig.NEWS_API_KEY.takeIf { it.isNotEmpty() }?.let {
                    header("X-Api-Key", it)
                }
            }
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, request ->

                    val responseException = exception as? ResponseException
                    val statusCode = responseException?.response?.status?.value ?: 500
                    val statusMessage = responseException?.message ?: ""

                    val errorBody = try {
                        responseException?.response?.body<Error>() ?: Error(
                            message = statusMessage,
                            code = statusCode
                        )
                    } catch (e: Exception) {
                        Error(
                            message = statusMessage,
                            code = statusCode
                        )
                    }

                    val errorMessage = errorBody.message
                    val errorCode = errorBody.code

                    val throwable = when (responseException) {
                        is ClientRequestException ->
                            AppClientRequestException(
                                responseException.response,
                                errorMessage
                            )

                        is RedirectResponseException ->
                            AppRedirectResponseException(
                                responseException.response,
                                errorMessage, errorCode
                            )

                        is ServerResponseException ->
                            AppServerResponseException(
                                responseException.response,
                                errorMessage, errorCode
                            )

                        else -> Exception(errorMessage)
                    }
                    throw throwable
                }
            }
        }



    @Provides
    @Singleton
    fun provideApiHttpClient(
        json: Json,
        chuckerInterceptor: ChuckerInterceptor,
    ): HttpClient = provideHttpClient(
        json = json,
        chuckerInterceptor = chuckerInterceptor
    )


}