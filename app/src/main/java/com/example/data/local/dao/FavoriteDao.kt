package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FavoriteRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_recipes ORDER BY savedAtTimestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :recipeId)")
    fun isFavorite(recipeId: Int): Flow<Boolean>

    @Query("SELECT * FROM favorite_recipes WHERE id = :recipeId LIMIT 1")
    suspend fun getFavoriteById(recipeId: Int): FavoriteRecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite_recipes WHERE id = :recipeId")
    suspend fun deleteFavoriteById(recipeId: Int)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteRecipeEntity)
}
