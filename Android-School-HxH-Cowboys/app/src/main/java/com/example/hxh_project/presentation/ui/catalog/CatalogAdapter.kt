package com.example.hxh_project.presentation.ui.catalog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
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
    context: Context,
    private val onItemClickListener: (productId: String, productName: String) -> Unit,
    private val onButtonClickListener: (productId: String) -> Unit,

): PagingDataAdapter<Product, CatalogAdapter.CatalogViewHolder>(DIFF_CALLBACK) {
    private val corners = context.resources.getDimension(R.dimen.preview_corner_radius)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        return CatalogViewHolder(
             ProductItemBinding.inflate(
                 LayoutInflater.from(parent.context),
                 parent,
                 false
             )
        )
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val data = getItem(position)
        holder.itemView.setOnClickListener {
            data?.let { onItemClickListener(data.id, data.title) }
        }
        holder.bind(data, onButtonClickListener)
    }

    inner class CatalogViewHolder(private val itemBinding: ProductItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
                @SuppressLint("SetTextI18n")
                fun bind(product: Product?, onButtonClickListener: (productId: String) -> Unit) {
                    with(itemBinding) {
                            tvTitle.text = product?.title
                            tvCategory.text = product?.department
                            tvPrice.text =
                                String.format("%,d ₽", product?.price).replace(",", " ")
                            ivCatalogItemPreview.load(product?.preview) {
                                transformations(RoundedCornersTransformation(corners))
                                placeholder(R.drawable.img_logo)
                                error(R.drawable.img_logo)
                            }
                            buyButton.setOnClickListener { product?.id?.let { it1 ->
                                onButtonClickListener(
                                    it1
                                )
                            } }
                    }
                }
            }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
        oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
        oldItem == newItem
}