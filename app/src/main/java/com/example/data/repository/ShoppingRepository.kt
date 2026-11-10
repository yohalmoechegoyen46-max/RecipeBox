package com.example.data.repository

import com.example.data.local.dao.ShoppingDao
import com.example.data.local.entity.ShoppingItemEntity
import com.example.data.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ShoppingRepository(
    private val shoppingDao: ShoppingDao
) {
    val allItems: Flow<List<ShoppingItemEntity>> = shoppingDao.getAllItems()

    suspend fun addItem(rawText: String, recipeSource: String = ""): Long = withContext(Dispatchers.IO) {
        val (parsedName, parsedQuantity) = parseIngredient(rawText)
        val entity = ShoppingItemEntity(
            name = parsedName,
            quantity = parsedQuantity,
            isCompleted = false,
            recipeSource = recipeSource
        )
        shoppingDao.insertItem(entity)
    }

    suspend fun addIngredientsFromRecipe(recipe: Recipe): Int = withContext(Dispatchers.IO) {
        val items = recipe.ingredients.map { raw ->
            val (name, quantity) = parseIngredient(raw)
            ShoppingItemEntity(
                name = name,
                quantity = quantity,
                isCompleted = false,
                recipeSource = recipe.name
            )
        }
        shoppingDao.insertItems(items)
        items.size
    }

    suspend fun toggleItem(item: ShoppingItemEntity) = withContext(Dispatchers.IO) {
        shoppingDao.updateCompletionStatus(item.id, !item.isCompleted)
    }

    suspend fun deleteItem(id: Long) = withContext(Dispatchers.IO) {
        shoppingDao.deleteItemById(id)
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        shoppingDao.clearAll()
    }

    suspend fun clearCompleted() = withContext(Dispatchers.IO) {
        shoppingDao.clearCompleted()
    }

    companion object {
        fun parseIngredient(text: String): Pair<String, String> {
            val clean = text.removePrefix("•").trim()

            // Check common patterns like "200 g de pasta", "1 cda. de aceite", "Al gusto sal y pimienta"
            if (clean.startsWith("Al gusto", ignoreCase = true)) {
                val rest = clean.removePrefix("Al gusto").removePrefix(":").trim()
                return Pair(
                    if (rest.isEmpty()) clean else rest.replaceFirstChar { it.uppercase() },
                    "Al gusto"
                )
            }

            val regex = Regex("""^(\d+(?:[.,]\d+)?\s*(?:g|kg|ml|l|taza[s]?|cda[.]?|cdta[.]?|diente[s]?|hoja[s]?|lomo[s]?|rebanada[s]?|ramillete[s]?|pizca|chorrito)?)\s*(?:de\s+)?(.+)$""", RegexOption.IGNORE_CASE)
            val match = regex.find(clean)

            return if (match != null) {
                val quantity = match.groupValues[1].trim()
                val name = match.groupValues[2].trim().replaceFirstChar { it.uppercase() }
                Pair(name, quantity)
            } else {
                // If it contains a dash, e.g. "Pasta rigatoni - 200g"
                if (clean.contains(" - ")) {
                    val parts = clean.split(" - ", limit = 2)
                    Pair(parts[0].trim().replaceFirstChar { it.uppercase() }, parts[1].trim())
                } else {
                    Pair(clean.replaceFirstChar { it.uppercase() }, "")
                }
            }
        }
    }
}
