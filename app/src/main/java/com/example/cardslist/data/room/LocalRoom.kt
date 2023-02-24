package com.example.cardslist.data.room

import android.content.Context
import androidx.room.*
import com.example.cardslist.data.model.Card

@Database(entities = [Card::class], version = 2)
abstract class LocalRoom : RoomDatabase() {

    abstract fun cardDao(): CardDao

    companion object {

        private const val DB_NAME = "card-db"

        // For Singleton instantiation
        @Volatile
        private var instance: LocalRoom? = null

        fun getInstance(context: Context): LocalRoom {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): LocalRoom {
            return Room.databaseBuilder(
                context,
                LocalRoom::class.java, DB_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }

}
