package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Recipe
import com.example.data.repository.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    val favorites: StateFlow<List<Recipe>> = recipeRepository.favoriteRecipes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavorite(recipeId: Int) {
        viewModelScope.launch {
            recipeRepository.removeFavorite(recipeId)
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            recipeRepository.toggleFavorite(recipe)
        }
    }

    companion object {
        fun provideFactory(repository: RecipeRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoritesViewModel(repository) as T
                }
            }
    }
}
