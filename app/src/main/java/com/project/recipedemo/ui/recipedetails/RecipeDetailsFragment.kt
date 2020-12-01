package com.project.recipedemo.ui.recipedetails

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.recipedemo.R
import com.project.recipedemo.base.BaseFragment
import com.project.recipedemo.database.entities.Recipe
import com.project.recipedemo.databinding.FragmentRecipeDetailsBinding
import com.project.recipedemo.model.RecipeType
import com.project.recipedemo.ui.adapter.IngredientOrStepAdapter
import com.project.recipedemo.ui.main.MainActivity
import kotlin.reflect.KClass

@Suppress("DEPRECATION")
class RecipeDetailsFragment : BaseFragment<FragmentRecipeDetailsBinding, RecipeDetailsViewModel>(),
    AdapterView.OnItemSelectedListener {

    companion object {
        private const val ARG_INPUT_DATA = "ARG_INPUT_DATA"

        private const val PERMISSION_CODE = 100
        private const val IMAGE_PICK_CODE = 101

        fun newInstance(recipe: Recipe) = RecipeDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_INPUT_DATA, recipe)
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_recipe_details
    override val viewModelClass: KClass<RecipeDetailsViewModel>
        get() = RecipeDetailsViewModel::class

    val recipe: Recipe by lazy {
        arguments!!.getParcelable(ARG_INPUT_DATA)!!
    }

    private lateinit var ingredientAdapter: IngredientOrStepAdapter
    private lateinit var stepAdapter: IngredientOrStepAdapter

    override fun initViews() {
        initValueVariable()
        initRecyclerViews()
        initSpinner()
        binding.ivFood.setImageBitmap(BitmapFactory.decodeFile(recipe.image))
    }

    private fun initValueVariable() {
        mViewModel.recipe.value = recipe
        mViewModel.foodName.value = recipe.name
        mViewModel.imagePath = recipe.image
        mViewModel.listIngredients.value = recipe.ingredients
        mViewModel.listSteps.value = recipe.steps
        mViewModel.listIngredientsTemp = recipe.ingredients as ArrayList<String>
        mViewModel.listStepsTemp = recipe.steps as ArrayList<String>
    }

    private fun initRecyclerViews() {
        ingredientAdapter = IngredientOrStepAdapter(
            this, mViewModel.listIngredients,
            mViewModel::onEditIngredient,
            mViewModel::onDeleteIngredient
        )
        binding.rcvIngredients.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = ingredientAdapter
        }

        stepAdapter = IngredientOrStepAdapter(
            this, mViewModel.listSteps,
            mViewModel::onEditStep,
            mViewModel::onDeleteStep
        )
        binding.rcvSteps.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = stepAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "Details"
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.btnAddImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        compositeDisposable.add(mViewModel.observableAction.subscribe {
            when (it) {
                is RecipeDetailsViewModel.Event.FocusIngredientEditText -> {
                    binding.edtIngredient.requestFocus()
                }
                is RecipeDetailsViewModel.Event.FocusStepEditText -> {
                    binding.edtStep.requestFocus()
                }
                is RecipeDetailsViewModel.Event.NotEnoughInfo -> {
                    Toast.makeText(context!!, "Please enter full information!", Toast.LENGTH_LONG)
                        .show()
                }
                RecipeDetailsViewModel.Event.OnBackPressed -> {
                    activity!!.onBackPressed()
                }
            }
        })
    }

    private fun initSpinner() {
        ArrayAdapter(context!!, android.R.layout.simple_spinner_item, mViewModel.getListRecipeTypes()).let {
            binding.typeSpinner.adapter = it
        }

        binding.typeSpinner.onItemSelectedListener = this

        when (recipe.type) {
            (binding.typeSpinner.getItemAtPosition(0) as RecipeType).title -> 0
            (binding.typeSpinner.getItemAtPosition(1) as RecipeType).title -> 1
            (binding.typeSpinner.getItemAtPosition(2) as RecipeType).title -> 2
            (binding.typeSpinner.getItemAtPosition(3) as RecipeType).title -> 3
            else -> 4
        }.apply {
            binding.typeSpinner.setSelection(this)
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(context!!, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val selectedImageUri: Uri = data?.data!!
            val picturePath = getPath(activity!!.applicationContext, selectedImageUri)
            Log.d("Picture Path", picturePath)
//            binding.ivFood.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            Glide.with(context!!).load(picturePath).into(binding.ivFood)
            binding.ivFood.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            mViewModel.imagePath = picturePath
        }
    }

    private fun getPath(context: Context, uri: Uri?): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = uri?.let {
            context.contentResolver.query(it, proj, null, null, null)
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {
        mViewModel.type = parent!!.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}
