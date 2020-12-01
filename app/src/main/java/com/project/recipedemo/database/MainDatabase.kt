package com.project.recipedemo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.recipedemo.database.dao.RecipeDao
import com.project.recipedemo.database.entities.Recipe
import com.project.recipedemo.database.typeconverter.StringTypeConverter


@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(StringTypeConverter::class)
abstract class MainDatabase : RoomDatabase() {

    abstract fun recipeDAO(): RecipeDao
}
