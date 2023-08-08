package com.example.android_test_cocktail_bar

import android.app.Application
import com.example.android_test_cocktail_bar.data.CocktailsDatabase

class CocktailsApp : Application() {
    val database: CocktailsDatabase by lazy { CocktailsDatabase.getDatabase(this) }
}