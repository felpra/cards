package com.example.cardslist.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cards")
data class Card(
    @NonNull
    @PrimaryKey
    val health: Int?,
    val cost: Int?,
    val attack: Int?,
    val name: String?,
    val flavor: String?,
    val text: String?,
    val type: String?,
    val cardSet: String?,
    val faction: String?,
    val rarity: String?,
    val img: String?,
): Parcelable
