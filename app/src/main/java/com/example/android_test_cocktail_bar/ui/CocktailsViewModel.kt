package com.example.android_test_cocktail_bar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.android_test_cocktail_bar.data.CocktailsDao
import com.example.android_test_cocktail_bar.model.Cocktail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CocktailsViewModel(
    private val dao: CocktailsDao
): ViewModel() {

    private val _cocktails: MutableStateFlow<CocktailsEvent> = MutableStateFlow(CocktailsEvent.Loading)
    val cocktails: StateFlow<CocktailsEvent> = _cocktails.asStateFlow()

    init {
        getCocktails()
    }

    fun getCocktails() {
        viewModelScope.launch(Dispatchers.IO) {
            _cocktails.emit(CocktailsEvent.Loading)
            val cocktails = dao.getCocktails()
            _cocktails.emit(CocktailsEvent.DataReady(cocktails))
        }
    }

    fun saveCocktails(vararg cocktail: Cocktail) {
        viewModelScope.launch(Dispatchers.IO) {
            _cocktails.emit(CocktailsEvent.Saving)
            dao.insertCocktails(*cocktail)
            _cocktails.emit(CocktailsEvent.Saved)
        }
    }
}

class CocktailsViewModelFactory(private val dao: CocktailsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CocktailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CocktailsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}