package com.project.recipedemo.ui.recipedetails

import androidx.lifecycle.ViewModel
import com.project.recipedemo.di.ViewModelKey
import com.project.recipedemo.di.scope.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RecipeDetailsInjector {

    @FragmentScope
    @ContributesAndroidInjector(modules = [RecipeDetailsModule::class])
    abstract fun bindRecipeDetailsFragment(): RecipeDetailsFragment
}

@Module
abstract class RecipeDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailsViewModel::class)
    abstract fun bindRecipeDetailsViewModel(viewModel: RecipeDetailsViewModel): ViewModel
}
