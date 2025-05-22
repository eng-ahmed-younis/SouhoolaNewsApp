package com.souhoola.newsapp.utils.dispatchers

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineDispatchersProvider @Inject constructor() : DispatchersProvider {
    override val main by lazy { Dispatchers.Main }
    override val io by lazy { Dispatchers.IO }
    override val default by lazy { Dispatchers.Default }
}