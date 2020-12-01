package com.project.recipedemo.database.dao

import androidx.room.*
import com.project.recipedemo.database.entities.Recipe
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Observable<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE type = :typeSelected")
    fun getRecipesByType(typeSelected: String): Observable<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(vararg recipe: Recipe): Completable

    @Update
    fun updateRecipe(recipe: Recipe): Completable

    @Delete
    fun deleteRecipe(recipe: Recipe): Completable
}
