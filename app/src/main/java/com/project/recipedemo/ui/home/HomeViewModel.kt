package com.project.recipedemo.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.project.recipedemo.base.BaseViewModel
import com.project.recipedemo.database.entities.Recipe
import com.project.recipedemo.database.repository.LocalRepository
import com.project.recipedemo.model.RecipeType
import com.project.recipedemo.usecase.GetRecipeTypesExecutor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: LocalRepository,
    private var getRecipeTypesExecutor: GetRecipeTypesExecutor
) : BaseViewModel() {

    sealed class Event {
        data class ShowDetailRecipe(val recipe: Recipe) : Event()
    }

    var listRecipes = MutableLiveData<List<Recipe>>()

    private val eventAction = PublishSubject.create<Event>()
    val observableAction: Observable<Event> = eventAction.hide()

    fun getAllRecipes() {
        disposable.add(
            repository.getAllRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listRecipes.value = it!!
                }, {
                    Log.e("HomeViewModel", it.stackTrace.toString())
                })
        )
    }

    fun getRecipesByType(typeSelected: String) {
        disposable.add(
            repository.getRecipesByType(typeSelected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listRecipes.value = it!!
                }, {
                    Log.e("HomeViewModel", it.stackTrace.toString())
                })
        )
    }

    fun getListRecipeTypes(): List<RecipeType> {
        var listRecipeType = listOf<RecipeType>()
        disposable.add(
            getRecipeTypesExecutor.execute()
                .subscribe({
                    listRecipeType = it
                }, {
                    it.printStackTrace()
                })
        )
        return listRecipeType
    }

    fun onRecipeSelected(recipe: Recipe) {
        eventAction.onNext(Event.ShowDetailRecipe(recipe))
    }
}
