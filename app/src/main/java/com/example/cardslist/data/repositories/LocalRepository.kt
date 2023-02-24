package com.example.cardslist.data.repositories

import com.example.cardslist.data.model.Card
import com.example.cardslist.data.room.CardDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(private val cardDao: CardDao){
    fun getCardsCache(): List<Card> {
        return cardDao.getAll()
    }

    fun saveCards(cards: List<Card>) {
        cardDao.insertAll(cards)
    }

    fun deleteAll() {
        cardDao.deleteAll()
    }
}