package com.souhoola.newsapp.di
import com.souhoola.newsapp.domain.repository.NewsRepository
import com.souhoola.newsapp.domain.use_case.GetTopHeadlinesUseCase
import com.souhoola.newsapp.domain.use_case.SearchNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetTopHeadlinesUseCase(repository: NewsRepository): GetTopHeadlinesUseCase {
        return GetTopHeadlinesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchNewsUseCase(repository: NewsRepository): SearchNewsUseCase {
        return SearchNewsUseCase(repository)
    }
}