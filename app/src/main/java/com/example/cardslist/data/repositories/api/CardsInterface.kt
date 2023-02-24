package com.example.cardslist.data.repositories.api

import com.example.cardslist.data.model.Card
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CardsInterface {
    @GET("cards/classes/{class}?collectible=1")
    suspend fun getCardsList(@Path("class") className: String = "Hunter"): Response<List<Card>>
}