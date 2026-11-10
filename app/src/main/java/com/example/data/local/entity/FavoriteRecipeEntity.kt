package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Recipe

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val ingredientsCsv: String,
    val instructionsCsv: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val difficulty: String,
    val cuisine: String,
    val caloriesPerServing: Int,
    val tagsCsv: String,
    val image: String,
    val rating: Double,
    val reviewCount: Int,
    val mealTypeCsv: String,
    val savedAtTimestamp: Long = System.currentTimeMillis()
) {
    fun toRecipe(): Recipe {
        return Recipe(
            id = id,
            name = name,
            ingredients = if (ingredientsCsv.isBlank()) emptyList() else ingredientsCsv.split("|||"),
            instructions = if (instructionsCsv.isBlank()) emptyList() else instructionsCsv.split("|||"),
            prepTimeMinutes = prepTimeMinutes,
            cookTimeMinutes = cookTimeMinutes,
            servings = servings,
            difficulty = difficulty,
            cuisine = cuisine,
            caloriesPerServing = caloriesPerServing,
            tags = if (tagsCsv.isBlank()) emptyList() else tagsCsv.split("|||"),
            image = image,
            rating = rating,
            reviewCount = reviewCount,
            mealType = if (mealTypeCsv.isBlank()) emptyList() else mealTypeCsv.split("|||")
        )
    }

    companion object {
        fun fromRecipe(recipe: Recipe): FavoriteRecipeEntity {
            return FavoriteRecipeEntity(
                id = recipe.id,
                name = recipe.name,
                ingredientsCsv = recipe.ingredients.joinToString("|||"),
                instructionsCsv = recipe.instructions.joinToString("|||"),
                prepTimeMinutes = recipe.prepTimeMinutes,
                cookTimeMinutes = recipe.cookTimeMinutes,
                servings = recipe.servings,
                difficulty = recipe.difficulty,
                cuisine = recipe.cuisine,
                caloriesPerServing = recipe.caloriesPerServing,
                tagsCsv = recipe.tags.joinToString("|||"),
                image = recipe.image,
                rating = recipe.rating,
                reviewCount = recipe.reviewCount,
                mealTypeCsv = recipe.mealType.joinToString("|||")
            )
        }
    }
}
