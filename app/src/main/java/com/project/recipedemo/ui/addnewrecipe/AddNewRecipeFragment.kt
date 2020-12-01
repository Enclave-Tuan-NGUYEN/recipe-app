package com.project.recipedemo.ui.addnewrecipe

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
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
import com.project.recipedemo.databinding.FragmentAddNewRecipeBinding
import com.project.recipedemo.ui.adapter.IngredientOrStepAdapter
import com.project.recipedemo.ui.main.MainActivity
import kotlin.reflect.KClass

class AddNewRecipeFragment :
    BaseFragment<FragmentAddNewRecipeBinding, AddNewRecipeViewModel>(),
    AdapterView.OnItemSelectedListener {

    companion object {
        private const val PERMISSION_CODE = 1
        private const val IMAGE_PICK_CODE = 2

        fun newInstance() = AddNewRecipeFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_add_new_recipe
    override val viewModelClass: KClass<AddNewRecipeViewModel>
        get() = AddNewRecipeViewModel::class

    private lateinit var ingredientAdapter: IngredientOrStepAdapter
    private lateinit var stepAdapter: IngredientOrStepAdapter

    override fun initViews() {
        initRecyclerViews()
        initSpinner()

        compositeDisposable.add(mViewModel.observableAction.subscribe {
            when (it) {
                is AddNewRecipeViewModel.Event.FocusIngredientEditText -> {
                    binding.edtIngredient.requestFocus()
                }
                is AddNewRecipeViewModel.Event.FocusStepEditText -> {
                    binding.edtStep.requestFocus()
                }
                is AddNewRecipeViewModel.Event.NotEnoughInfo -> {
                    Toast.makeText(context!!, "Please enter full information!", Toast.LENGTH_LONG).show()
                }
                is AddNewRecipeViewModel.Event.OnBackPressed -> {
                    activity!!.onBackPressed()
                }
            }
        })
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

    private fun initSpinner() {
        ArrayAdapter(context!!, android.R.layout.simple_spinner_item, mViewModel.getListRecipeTypes()).let {
            binding.typeSpinner.adapter = it
        }

        binding.typeSpinner.onItemSelectedListener = this
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).supportActionBar?.title = "Add Recipe"
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
            Glide.with(context!!).load(picturePath).into(binding.ivFood)
            binding.ivFood.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            mViewModel.imagePath = picturePath
        }
    }

    @Suppress("DEPRECATION")
    private fun getPath(context: Context, uri: Uri?): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = uri?.let { context.contentResolver.query(it, proj, null, null, null) }
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

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
