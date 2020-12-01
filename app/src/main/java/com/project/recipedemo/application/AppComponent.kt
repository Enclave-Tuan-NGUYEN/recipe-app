package com.project.recipedemo.application

import android.app.Application
import com.project.recipedemo.di.ViewModelFactoryModule
import com.project.recipedemo.ui.main.MainInjector
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        MainInjector::class,
        ViewModelFactoryModule::class,
        AppModule::class
    ]
)

interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: RecipeApp)

    override fun inject(instance: DaggerApplication)
}
