package com.example.android_test_cocktail_bar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android_test_cocktail_bar.model.Cocktail

@Database(entities = [
    Cocktail::class
], version = 1)
@TypeConverters(Converters::class)
abstract class CocktailsDatabase: RoomDatabase() {
    abstract fun appDao(): CocktailsDao

    companion object {
        @Volatile
        private var INSTANCE: CocktailsDatabase? = null

        fun getDatabase(context: Context): CocktailsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    CocktailsDatabase::class.java,
                    "app_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}