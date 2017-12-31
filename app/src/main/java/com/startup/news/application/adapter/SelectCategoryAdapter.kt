package com.startup.news.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.startup.news.application.R
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse
import kotlinx.android.synthetic.main.row_select_category.view.*

/**
 * Created by admin on 11/17/2017.
 */
class SelectCategoryAdapter(private val categories: List<CategoryFirebaseResponse>, val callback: ICategoryCallback)
    : RecyclerView.Adapter<SelectCategoryAdapter.CategoryViewHolder>() {
    private var isViewLoading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder =
            CategoryViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.row_select_category, parent, false))

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        isViewLoading = true
        with(categories[position]) {
            holder.textCategory.text = title
            if (isSelected)
                holder.textCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checked, 0)
            else
                holder.textCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add, 0)
            holder.textCategory.setOnClickListener {
                val category = categories[holder.adapterPosition]
                category.isSelected = !category.isSelected
                callback.isCategorySelected(category.isSelected)
                if (!isViewLoading)
                    notifyItemChanged(holder.adapterPosition)
            }
        }
        isViewLoading = false
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textCategory: TextView = view.text_category
    }

    interface ICategoryCallback {
        fun isCategorySelected(isSelected: Boolean)
    }
}