package com.project.recipedemo.ui.addnewrecipe

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

class AddNewRecipeViewModel @Inject constructor(
    private val repository: LocalRepository,
    private val getRecipeTypesExecutor: GetRecipeTypesExecutor
) : BaseViewModel() {

    sealed class Event {
        object FocusIngredientEditText : Event()
        object FocusStepEditText : Event()
        object NotEnoughInfo : Event()
        object OnBackPressed : Event()
    }

    private val eventAction = PublishSubject.create<Event>()
    val observableAction: Observable<Event> = eventAction.hide()

    val foodName = MutableLiveData<String>()

    val listIngredients = MutableLiveData<List<String>>()
    private var listIngredientsTemp = arrayListOf<String>()

    val listSteps = MutableLiveData<List<String>>()
    private var listStepsTemp = arrayListOf<String>()

    val newIngredient = MutableLiveData<String>()
    val newStep = MutableLiveData<String>()

    var type: String = ""

    var imagePath: String? = null

    var isEditIngredient = MutableLiveData(false)
    var isEditStep = MutableLiveData(false)

    private var pos: Int = -1

    init {
        listIngredients.value = listOf()
        listSteps.value = listOf()
    }

    fun onAddIngredient() {
        if (!newIngredient.value.isNullOrEmpty()) {
            if (isEditIngredient.value!!) {
                isEditIngredient.value = false
                listIngredientsTemp[pos] = newIngredient.value!!
                listIngredients.value = listIngredientsTemp
                newIngredient.value = ""
            } else {
                listIngredientsTemp.add(newIngredient.value!!)
                listIngredients.value = listIngredientsTemp
                newIngredient.value = ""
            }
        }
    }

    fun onEditIngredient(position: Int) {
        isEditIngredient.value = true
        eventAction.onNext(Event.FocusIngredientEditText)
        newIngredient.value = listIngredients.value?.get(position)
        pos = position
    }

    fun onDeleteIngredient(position: Int) {
        listIngredientsTemp.removeAt(position)
        listIngredients.value = listIngredientsTemp
        newIngredient.value = ""
    }

    fun onAddStep() {
        if (!newStep.value.isNullOrEmpty()) {
            if (isEditStep.value!!) {
                isEditStep.value = false
                listStepsTemp[pos] = newStep.value!!
                listSteps.value = listStepsTemp
                newStep.value = ""
            } else {
                listStepsTemp.add(newStep.value!!)
                listSteps.value = listStepsTemp
                newStep.value = ""
            }
        }
    }

    fun onEditStep(position: Int) {
        isEditStep.value = true
        pos = position
        eventAction.onNext(Event.FocusStepEditText)
        newStep.value = listSteps.value?.get(position)
    }

    fun onDeleteStep(position: Int) {
        listStepsTemp.removeAt(position)
        listSteps.value = listStepsTemp
        newStep.value = ""
    }

    fun createRecipe() {
        if (!foodName.value.isNullOrEmpty() && !imagePath.isNullOrEmpty() && !listIngredients.value.isNullOrEmpty() &&
            !listSteps.value.isNullOrEmpty()) {
            Recipe( foodName.value!!, type, imagePath.toString() , listIngredients.value!!, listSteps.value!!).let {
                disposable.add(
                    repository.insertRecipe(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        eventAction.onNext(Event.OnBackPressed)
                    }, { throwable ->
                        Log.e("AddNewRecipeViewModel", throwable.message.toString())
                    }))
            }
        } else {
            eventAction.onNext(Event.NotEnoughInfo)
        }
    }

    fun getListRecipeTypes(): List<RecipeType> {
        var listRecipeType = listOf<RecipeType>()
        disposable.add(
            getRecipeTypesExecutor.execute(false)
                .subscribe({
                    listRecipeType = it
                }, {
                    it.printStackTrace()
                })
        )
        return listRecipeType
    }
}
