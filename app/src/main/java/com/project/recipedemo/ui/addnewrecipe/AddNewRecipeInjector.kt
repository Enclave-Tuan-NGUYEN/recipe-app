package com.project.recipedemo.ui.addnewrecipe

import androidx.lifecycle.ViewModel
import com.project.recipedemo.di.ViewModelKey
import com.project.recipedemo.di.scope.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AddNewRecipeInjector {

    @FragmentScope
    @ContributesAndroidInjector(modules = [AddNewRecipeModule::class])
    abstract fun bindAddNewRecipeFragment(): AddNewRecipeFragment
}

@Module
abstract class AddNewRecipeModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddNewRecipeViewModel::class)
    abstract fun bindAddNewRecipeViewModel(viewModel: AddNewRecipeViewModel): ViewModel
}
