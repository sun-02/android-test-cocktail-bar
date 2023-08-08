package com.example.android_test_cocktail_bar.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_test_cocktail_bar.model.RecyclerViewAdapterEntity

class RecyclerViewListAdapter<T : RecyclerViewAdapterEntity>(
    @LayoutRes private val itemRes: Int,
    private val outerBind: (View, T, Int) -> Unit
) : ListAdapter<T, RecyclerViewListAdapter<T>.BaseViewHolder>(DiffCallback()) {

    inner class BaseViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind(itemData: T, pos: Int) {
            outerBind.invoke(rootView, itemData, pos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemRes, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemData = getItem(position)
        holder.bind(itemData, position)
    }

    class DiffCallback<T : RecyclerViewAdapterEntity> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem.component1() == newItem.component1()

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem
    }
}