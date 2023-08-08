package com.example.android_test_cocktail_bar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_test_cocktail_bar.model.Cocktail

@Dao
interface CocktailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktails(vararg cocktails: Cocktail)

    @Query("SELECT * FROM cocktails")
    fun getCocktails() : List<Cocktail>
}