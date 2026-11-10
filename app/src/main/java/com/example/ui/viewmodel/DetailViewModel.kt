package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Recipe
import com.example.data.repository.RecipeRepository
import com.example.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val recipe: Recipe? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val addedIngredientsCount: Int? = null,
    val errorMessage: String? = null
)

class DetailViewModel(
    private val recipeRepository: RecipeRepository,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(isLoading = true))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val foundRecipe = recipeRepository.getRecipeById(recipeId)
            if (foundRecipe != null) {
                _uiState.update {
                    it.copy(
                        recipe = foundRecipe,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                observeFavoriteStatus(recipeId)
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo encontrar la receta seleccionada."
                    )
                }
            }
        }
    }

    private fun observeFavoriteStatus(recipeId: Int) {
        viewModelScope.launch {
            recipeRepository.isFavorite(recipeId).collectLatest { isFav ->
                _uiState.update { it.copy(isFavorite = isFav) }
            }
        }
    }

    fun toggleFavorite() {
        val currentRecipe = _uiState.value.recipe ?: return
        viewModelScope.launch {
            val isNowFav = recipeRepository.toggleFavorite(currentRecipe)
            _uiState.update { it.copy(isFavorite = isNowFav) }
        }
    }

    fun addIngredientsToShoppingList(onSuccess: (Int) -> Unit) {
        val currentRecipe = _uiState.value.recipe ?: return
        viewModelScope.launch {
            val count = shoppingRepository.addIngredientsFromRecipe(currentRecipe)
            _uiState.update { it.copy(addedIngredientsCount = count) }
            onSuccess(count)
        }
    }

    fun clearAddedNotification() {
        _uiState.update { it.copy(addedIngredientsCount = null) }
    }

    companion object {
        fun provideFactory(
            recipeRepository: RecipeRepository,
            shoppingRepository: ShoppingRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(recipeRepository, shoppingRepository) as T
            }
        }
    }
}
