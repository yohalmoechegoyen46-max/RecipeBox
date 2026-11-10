package com.example.data.remote

import com.example.data.model.Recipe
import com.example.data.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipes")
    suspend fun getRecipes(
        @Query("limit") limit: Int = 50,
        @Query("skip") skip: Int = 0
    ): RecipeResponse

    @GET("recipes/search")
    suspend fun searchRecipes(
        @Query("q") query: String,
        @Query("limit") limit: Int = 50
    ): RecipeResponse

    @GET("recipes/tag/{tag}")
    suspend fun getRecipesByTag(
        @Path("tag") tag: String,
        @Query("limit") limit: Int = 50
    ): RecipeResponse

    @GET("recipes/meal-type/{mealType}")
    suspend fun getRecipesByMealType(
        @Path("mealType") mealType: String,
        @Query("limit") limit: Int = 50
    ): RecipeResponse

    @GET("recipes/{id}")
    suspend fun getRecipeById(
        @Path("id") id: Int
    ): Recipe
}
