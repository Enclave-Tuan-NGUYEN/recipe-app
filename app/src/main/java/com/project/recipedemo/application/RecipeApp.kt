package com.project.recipedemo.application

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class RecipeApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}
