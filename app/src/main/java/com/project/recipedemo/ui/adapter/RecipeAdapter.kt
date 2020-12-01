package com.project.recipedemo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.project.recipedemo.database.entities.Recipe
import com.project.recipedemo.databinding.ItemRecipeBinding

class RecipeAdapter(
    lifecycleOwner: LifecycleOwner,
    private val data: LiveData<List<Recipe>>,
    private val onItemClicked: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    init {
        data.observe(lifecycleOwner, {
          notifyDataSetChanged()
        })
    }

    private val listItems: List<Recipe>
        get() = data.value.orEmpty()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = listItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = listItems[position]
        holder.bind(recipe)

        with(holder.itemView) {
            tag = recipe
            setOnClickListener(mOnClickListener)
        }
    }

    private val mOnClickListener = View.OnClickListener {
        val item = it.tag as Recipe
        onItemClicked(item)
    }

    class ViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Recipe) {
            binding.apply {
                recipe = item
            }
        }
    }
}
