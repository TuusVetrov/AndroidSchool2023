package com.example.hxh_project.presentation.ui.product.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hxh_project.R
import com.example.hxh_project.databinding.SizeItemBinding
import com.example.hxh_project.domain.model.ProductSize

class ProductSizeAdapter(
    private val onItemClick: (String) -> Unit
): RecyclerView.Adapter<ProductSizeAdapter.SizeViewHolder>() {
    private lateinit var binding: SizeItemBinding
    private lateinit var context: Context

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        context = parent.context
        binding = SizeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeViewHolder(binding.root)
    }

    fun submitList(products: List<ProductSize>) {
        differ.submitList(products)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        holder.bind(differ.currentList[position].value, differ.currentList[position].isAvailable)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class SizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(size: String, isAvailable: Boolean) {
            binding.tvSize.text = size

            if (isAvailable) {
                val primaryColor = ContextCompat.getColor(context, R.color.text_primary)
                binding.tvSize.setTextColor(primaryColor)
                itemView.setOnClickListener {
                    onItemClick(size)
                }
            } else {
                val secondaryColor = ContextCompat.getColor(context, R.color.text_secondary)
                binding.tvSize.setTextColor(secondaryColor)
            }
        }
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductSize>() {
    override fun areItemsTheSame(oldItem: ProductSize, newItem: ProductSize): Boolean =
        oldItem.value == newItem.value
    override fun areContentsTheSame(oldItem: ProductSize, newItem: ProductSize): Boolean =
        oldItem == newItem
}