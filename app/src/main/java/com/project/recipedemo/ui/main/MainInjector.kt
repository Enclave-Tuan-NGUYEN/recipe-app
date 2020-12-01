package com.project.recipedemo.ui.main

import androidx.lifecycle.ViewModel
import com.project.recipedemo.di.ViewModelKey
import com.project.recipedemo.di.scope.ActivityScope
import com.project.recipedemo.ui.addnewrecipe.AddNewRecipeInjector
import com.project.recipedemo.ui.home.HomeInjector
import com.project.recipedemo.ui.recipedetails.RecipeDetailsInjector
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainInjector {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            MainModule::class,
            HomeInjector::class,
            AddNewRecipeInjector::class,
            RecipeDetailsInjector::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}

@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}
