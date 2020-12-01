package com.project.recipedemo.model

data class RecipeType(
    val id: Int,
    val title: String
) {
    override fun toString(): String {
        return title
    }
}
