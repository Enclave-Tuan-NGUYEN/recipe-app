package com.project.recipedemo.base

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.project.recipedemo.R
import com.project.recipedemo.vmproviderfactory.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseActivity<TBinding: ViewDataBinding, TViewModel: BaseViewModel> :
    DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProviderFactory

    lateinit var viewModel: TViewModel
    protected val binding get() = _binding!!

    private var _binding: TBinding? = null

    @get:LayoutRes
    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<TViewModel>

    private val bindingVariable: Int = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, factory).get(viewModelClass.java)

        _binding = DataBindingUtil.setContentView(this, layoutId)
        _binding!!.setVariable(bindingVariable, viewModel)
        _binding!!.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setRootFragment(newFragment: Fragment) {
        val currentFragmentTag = newFragment.javaClass.simpleName
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, newFragment, currentFragmentTag)
            .addToBackStack(currentFragmentTag)
            .commit()
    }

    fun changeFragment(newFragment: Fragment) {
        val currentFragmentTag = newFragment.javaClass.simpleName
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in_right, // Enter
            R.anim.slide_out_left, // Exit
            R.anim.slide_in_left, // Pop Enter
            R.anim.slide_out_right // Pop Exit
        )
        transaction.replace(R.id.fragment_container, newFragment, currentFragmentTag)
            .addToBackStack(currentFragmentTag)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
