package com.example.android_test_cocktail_bar.ui

import com.example.android_test_cocktail_bar.model.Cocktail

sealed class CocktailsEvent {
    object Loading : CocktailsEvent()
    object Saving : CocktailsEvent()
    object Saved : CocktailsEvent()
    class DataReady(val value: List<Cocktail>) : CocktailsEvent()
}