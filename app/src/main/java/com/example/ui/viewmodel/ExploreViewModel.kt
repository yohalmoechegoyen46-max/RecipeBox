package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Recipe
import com.example.data.repository.RecipeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExploreUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "Todas",
    val recipes: List<Recipe> = emptyList(),
    val filteredRecipes: List<Recipe> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ExploreViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState(isLoading = true))
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadRecipes()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            recipeRepository.favoriteRecipes.collectLatest { favorites ->
                _uiState.update { state ->
                    state.copy(favoriteIds = favorites.map { it.id }.toSet())
                }
            }
        }
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = recipeRepository.getRecipes()
            result.onSuccess { list ->
                _uiState.update { state ->
                    state.copy(
                        recipes = list,
                        filteredRecipes = applyFilters(list, state.selectedCategory, state.searchQuery),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = err.localizedMessage ?: "No se pudieron cargar las recetas"
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            if (newQuery.isBlank()) {
                _uiState.update { state ->
                    state.copy(
                        filteredRecipes = applyFilters(state.recipes, state.selectedCategory, "")
                    )
                }
            } else {
                val searchResult = recipeRepository.searchRecipes(newQuery)
                searchResult.onSuccess { searchList ->
                    _uiState.update { state ->
                        state.copy(
                            filteredRecipes = applyFilters(searchList, state.selectedCategory, newQuery)
                        )
                    }
                }
            }
        }
    }

    fun onCategorySelect(category: String) {
        _uiState.update { state ->
            val updated = state.copy(selectedCategory = category)
            updated.copy(
                filteredRecipes = applyFilters(state.recipes, category, state.searchQuery)
            )
        }
    }

    private fun applyFilters(
        baseList: List<Recipe>,
        category: String,
        query: String
    ): List<Recipe> {
        return baseList.filter { recipe ->
            val matchesCategory = when (category) {
                "Todas" -> true
                "Rápidas" -> recipe.totalTimeMinutes <= 30 || recipe.tags.any { it.equals("Rápidas", true) }
                "Vegetarianas" -> recipe.tags.any { it.contains("vegetar", true) || it.contains("ensalada", true) } ||
                        recipe.cuisine.contains("vegetar", true)
                "Saludable" -> recipe.tags.any { it.contains("saludable", true) } || recipe.caloriesPerServing <= 400
                "Cena" -> recipe.mealType.any { it.contains("cena", true) || it.contains("dinner", true) } || recipe.tags.any { it.contains("cena", true) }
                "Desayuno" -> recipe.mealType.any { it.contains("desayuno", true) || it.contains("breakfast", true) } || recipe.tags.any { it.contains("desayuno", true) }
                else -> recipe.tags.any { it.equals(category, true) } || recipe.cuisine.contains(category, true)
            }

            val matchesQuery = if (query.isBlank()) true else {
                recipe.name.contains(query, ignoreCase = true) ||
                        recipe.cuisine.contains(query, ignoreCase = true) ||
                        recipe.ingredients.any { it.contains(query, ignoreCase = true) }
            }

            matchesCategory && matchesQuery
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
                    return ExploreViewModel(repository) as T
                }
            }
    }
}
