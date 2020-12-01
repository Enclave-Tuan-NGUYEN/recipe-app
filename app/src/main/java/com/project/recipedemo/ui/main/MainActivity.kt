package com.project.recipedemo.ui.main

import android.os.Bundle
import com.project.recipedemo.R
import com.project.recipedemo.base.BaseActivity
import com.project.recipedemo.databinding.ActivityMainBinding
import com.project.recipedemo.ui.home.HomeFragment
import kotlin.reflect.KClass

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModelClass: KClass<MainViewModel>
        get() = MainViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        if(supportFragmentManager.backStackEntryCount == 0) {
            setRootFragment(HomeFragment.newInstance())
        }
    }
}
