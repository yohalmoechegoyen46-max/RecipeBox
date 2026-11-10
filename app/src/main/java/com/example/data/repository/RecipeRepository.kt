package com.example.data.repository

import com.example.data.local.dao.FavoriteDao
import com.example.data.local.entity.FavoriteRecipeEntity
import com.example.data.model.Recipe
import com.example.data.model.RecipeSeedData
import com.example.data.remote.RecipeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RecipeRepository(
    private val apiService: RecipeApiService,
    private val favoriteDao: FavoriteDao
) {
    val favoriteRecipes: Flow<List<Recipe>> = favoriteDao.getAllFavorites().map { list ->
        list.map { it.toRecipe() }
    }

    fun isFavorite(recipeId: Int): Flow<Boolean> = favoriteDao.isFavorite(recipeId)

    suspend fun getRecipes(): Result<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getRecipes(limit = 40)
            val translatedApiRecipes = response.recipes.map { RecipeSeedData.translateApiRecipe(it) }

            // Combine featured seed recipes (exact match from PDF) at the top, then API recipes
            val all = (RecipeSeedData.featuredRecipes + translatedApiRecipes).distinctBy { it.id }
            Result.success(all)
        } catch (e: Exception) {
            // Offline fallback: check favorites or seed data so the user never has a blank screen
            val localFavorites = favoriteDao.getAllFavorites().firstOrNull()?.map { it.toRecipe() } ?: emptyList()
            val fallback = (RecipeSeedData.featuredRecipes + localFavorites).distinctBy { it.id }
            if (fallback.isNotEmpty()) {
                Result.success(fallback)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun searchRecipes(query: String): Result<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            val trimmed = query.trim()
            if (trimmed.isEmpty()) {
                return@withContext getRecipes()
            }

            // Search local seed/featured first
            val localMatches = RecipeSeedData.featuredRecipes.filter {
                it.name.contains(trimmed, ignoreCase = true) ||
                        it.cuisine.contains(trimmed, ignoreCase = true) ||
                        it.tags.any { tag -> tag.contains(trimmed, ignoreCase = true) }
            }

            val apiResponse = try {
                apiService.searchRecipes(trimmed).recipes.map { RecipeSeedData.translateApiRecipe(it) }
            } catch (_: Exception) {
                emptyList()
            }

            val combined = (localMatches + apiResponse).distinctBy { it.id }
            Result.success(combined)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipeById(id: Int): Recipe? = withContext(Dispatchers.IO) {
        // 1. Check local seed
        RecipeSeedData.featuredRecipes.find { it.id == id }?.let { return@withContext it }

        // 2. Check local database favorites
        favoriteDao.getFavoriteById(id)?.let { return@withContext it.toRecipe() }

        // 3. Fetch from API
        try {
            val remote = apiService.getRecipeById(id)
            RecipeSeedData.translateApiRecipe(remote)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun toggleFavorite(recipe: Recipe): Boolean = withContext(Dispatchers.IO) {
        val existing = favoriteDao.getFavoriteById(recipe.id)
        if (existing != null) {
            favoriteDao.deleteFavoriteById(recipe.id)
            false
        } else {
            favoriteDao.insertFavorite(FavoriteRecipeEntity.fromRecipe(recipe))
            true
        }
    }

    suspend fun removeFavorite(recipeId: Int) = withContext(Dispatchers.IO) {
        favoriteDao.deleteFavoriteById(recipeId)
    }
}
