package com.project.recipedemo.database.repository

import com.project.recipedemo.database.MainDatabase
import com.project.recipedemo.database.entities.Recipe
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val database: MainDatabase
) {
    fun getAllRecipes(): Observable<List<Recipe>> {
        return database.recipeDAO().getAllRecipes()
    }

    fun getRecipesByType(typeSelected: String): Observable<List<Recipe>> {
        return database.recipeDAO().getRecipesByType(typeSelected)
    }

    fun insertRecipe(recipe: Recipe): Completable {
        return database.recipeDAO().insertRecipe(recipe)
    }

    fun updateRecipe(recipe: Recipe): Completable {
        return database.recipeDAO().updateRecipe(recipe)
    }

    fun deleteRecipe(recipe: Recipe): Completable {
        return database.recipeDAO().deleteRecipe(recipe)
    }
}