package com.example.cardslist.data.room

import androidx.room.*
import com.example.cardslist.data.model.Card

@Dao
interface CardDao {

    @Query("SELECT * FROM cards order by name asc")
    fun getAll(): List<Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cards: Collection<Card>)

    @Query("DELETE FROM cards")
    fun deleteAll()
}