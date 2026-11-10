package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Recipe(
    val id: Int,
    val name: String,
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val prepTimeMinutes: Int = 15,
    val cookTimeMinutes: Int = 15,
    val servings: Int = 2,
    val difficulty: String = "Easy",
    val cuisine: String = "Internacional",
    val caloriesPerServing: Int = 400,
    val tags: List<String> = emptyList(),
    val image: String = "",
    val rating: Double = 4.7,
    val reviewCount: Int = 95,
    val mealType: List<String> = emptyList()
) {
    val totalTimeMinutes: Int
        get() = prepTimeMinutes + cookTimeMinutes

    val localizedDifficulty: String
        get() = when (difficulty.lowercase()) {
            "easy", "fácil", "facil" -> "FÁCIL"
            "medium", "media" -> "MEDIA"
            "hard", "difícil", "dificil" -> "DIFÍCIL"
            else -> difficulty.uppercase()
        }

    val localizedCuisine: String
        get() = when (cuisine.lowercase()) {
            "italian" -> "COCINA ITALIANA"
            "mediterranean" -> "COCINA MEDITERRÁNEA"
            "mexican" -> "COCINA MEXICANA"
            "asian" -> "COCINA ASIÁTICA"
            "american" -> "COCINA AMERICANA"
            "greek" -> "COCINA GRIEGA"
            "indian" -> "COCINA INDIA"
            "japanese" -> "COCINA JAPONESA"
            "healthy" -> "COCINA SALUDABLE"
            else -> if (cuisine.startsWith("Cocina", ignoreCase = true)) cuisine.uppercase() else "COCINA ${cuisine.uppercase()}"
        }
}

@JsonClass(generateAdapter = true)
data class RecipeResponse(
    val recipes: List<Recipe> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0
)
