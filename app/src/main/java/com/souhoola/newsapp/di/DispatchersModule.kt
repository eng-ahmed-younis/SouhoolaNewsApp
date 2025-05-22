package com.souhoola.newsapp.di

import com.souhoola.newsapp.utils.dispatchers.CoroutineDispatchersProvider
import com.souhoola.newsapp.utils.dispatchers.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatchersModule {
    @Singleton
    @Binds
    abstract fun provideDispatchers(coroutineDispatchersProvider: CoroutineDispatchersProvider): DispatchersProvider
}