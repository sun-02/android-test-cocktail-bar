package com.example.android_test_cocktail_bar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cocktails")
data class Cocktail(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String = "",
    val ingredients: List<String>,
    val recipe: String = "",
    val imageUri: Int? = null // для тестов
) : RecyclerViewAdapterEntity