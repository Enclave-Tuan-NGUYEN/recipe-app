package com.project.recipedemo.ui.home

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.recipedemo.R
import com.project.recipedemo.base.BaseFragment
import com.project.recipedemo.database.entities.Recipe
import com.project.recipedemo.databinding.FragmentHomeBinding
import com.project.recipedemo.ui.adapter.RecipeAdapter
import com.project.recipedemo.ui.addnewrecipe.AddNewRecipeFragment
import com.project.recipedemo.ui.main.MainActivity
import com.project.recipedemo.ui.recipedetails.RecipeDetailsFragment
import kotlin.reflect.KClass

class HomeFragment: BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_home
    override val viewModelClass: KClass<HomeViewModel>
        get() = HomeViewModel::class

    private lateinit var recipeAdapter: RecipeAdapter

    override fun initViews() {
        initSpinner()
        initRecyclerView()

        binding.btnAdd.setOnClickListener {
            moveToAddNewRecipeScreen()
        }

        compositeDisposable.add(mViewModel.observableAction.subscribe {
            when (it) {
                is HomeViewModel.Event.ShowDetailRecipe -> {
                    moveToRecipeDetailsScreen(it.recipe)
                }
            }
        })
    }

    private fun initSpinner() {
        ArrayAdapter(context!!, android.R.layout.simple_spinner_item, mViewModel.getListRecipeTypes()).let {
            binding.recipesSpinner.adapter = it
        }
        binding.recipesSpinner.onItemSelectedListener = this
    }

    private fun initRecyclerView() {
        recipeAdapter = RecipeAdapter(
            this, mViewModel.listRecipes,
            mViewModel::onRecipeSelected
        )
        binding.rcvRecipes.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = recipeAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        binding.recipesSpinner.setSelection(0)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Recipes App"
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    private fun moveToAddNewRecipeScreen() {
        changeFragment(AddNewRecipeFragment.newInstance())
    }

    private fun moveToRecipeDetailsScreen(recipe: Recipe) {
        changeFragment(RecipeDetailsFragment.newInstance(recipe))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
        when (position) {
            0 -> mViewModel.getAllRecipes()
            else -> {
                mViewModel.getRecipesByType(parent!!.getItemAtPosition(position).toString())
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}
