package com.example.hxh_project.presentation.ui.catalog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.hxh_project.R
import com.example.hxh_project.databinding.ProductItemBinding
import com.example.hxh_project.domain.model.Product
import java.util.*

class CatalogAdapter(
    private val onItemClickListener: (Product) -> Unit
): RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
    fun submitList(products: List<Product>) {
        differ.submitList(products)
    }
    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder =
        CatalogViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(differ.currentList[position], onItemClickListener)
    }

    inner class CatalogViewHolder(private val itemBinding: ProductItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {

                @SuppressLint("SetTextI18n")
                fun bind(product: Product, onItemClickListener: (Product) -> Unit) = with(itemBinding) {
                    tvTitle.text = product.title
                    tvCategory.text = product.department
                    tvPrice.text =
                        String.format("%,d ₽", product.price).replace(",", " ")
                    ivCatalogItemPreview.load(product.preview) {
                        transformations(RoundedCornersTransformation(20f))
                        placeholder(R.drawable.img_logo)
                        error(R.drawable.img_logo)
                    }
                    root.setOnClickListener { onItemClickListener(product) }
                }
            }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
        oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
        oldItem == newItem
}