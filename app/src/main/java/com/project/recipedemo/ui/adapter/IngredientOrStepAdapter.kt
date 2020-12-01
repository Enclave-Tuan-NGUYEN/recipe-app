package com.project.recipedemo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.project.recipedemo.databinding.ItemIngradientOrRecipeBinding
import kotlinx.android.synthetic.main.item_ingradient_or_recipe.view.*

class IngredientOrStepAdapter (
    lifecycleOwner: LifecycleOwner,
    private val data: LiveData<List<String>>,
    private val onEditItem: (Int) -> Unit,
    private val onDeleteItem: (Int) -> Unit
) : RecyclerView.Adapter<IngredientOrStepAdapter.ViewHolder>() {

    init {
        data.observe(lifecycleOwner, {
            notifyDataSetChanged()
        })
    }

    private val listItems: List<String>
        get() = data.value!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemIngradientOrRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = listItems.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemValue = listItems[position]

        holder.tvFoodName.text = (position + 1).toString() + ". " + itemValue

        with(holder.itemView.btn_edit) {
            setOnClickListener {
                onEditItem(position)
            }
        }

        with(holder.itemView.btn_delete) {
            setOnClickListener {
                onDeleteItem(position)
            }
        }
    }

    class ViewHolder(binding: ItemIngradientOrRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvFoodName = binding.tvFoodName
    }
}
