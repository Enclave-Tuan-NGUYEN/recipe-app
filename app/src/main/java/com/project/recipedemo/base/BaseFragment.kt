package com.project.recipedemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.recipedemo.vmproviderfactory.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<T: ViewDataBinding, V: BaseViewModel> : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private var bindingVariable : Int = BR.viewModel

    @get: LayoutRes
    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<V>

    protected val compositeDisposable = CompositeDisposable()

    protected val binding: T
        get() = _binding!!
    private var _binding: T? = null

    protected lateinit var mViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this, factory).get(viewModelClass.java)
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.setVariable(bindingVariable, mViewModel)
        binding.lifecycleOwner = this
        initViews()

        return binding.root
    }

    protected fun changeFragment(fragment: Fragment) {
        (activity as BaseActivity<*,*>).changeFragment(fragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    open fun initViews() { }
}
