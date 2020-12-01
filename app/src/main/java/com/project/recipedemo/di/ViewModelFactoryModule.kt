package com.project.recipedemo.di

import androidx.lifecycle.ViewModelProvider
import com.project.recipedemo.vmproviderfactory.ViewModelProviderFactory
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}
