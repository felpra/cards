package com.example.cardslist.data.repositories.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class KeyInterceptor (private val apiKey: String,
                      private val apiHost: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val httpUrl = original.url.newBuilder()
            .build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", apiHost)
            .url(httpUrl)

        return chain.proceed(requestBuilder.build())
    }
}