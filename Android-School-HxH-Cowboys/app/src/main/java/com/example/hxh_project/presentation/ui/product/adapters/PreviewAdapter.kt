package com.example.hxh_project.presentation.ui.product.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.hxh_project.R
import com.example.hxh_project.databinding.ProductPreviewItemBinding

class PreviewAdapter(
    private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<PreviewAdapter.PreviewViewHolder>() {
    private lateinit var binding: ProductPreviewItemBinding
    private lateinit var context: Context

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        binding = ProductPreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return PreviewViewHolder(binding.root)
    }

    fun submitList(images: List<String>) {
        differ.submitList(images)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class PreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageUrl: String) {
            val corners = context.resources.getDimension(R.dimen.preview_corner_radius)
            binding.ivProductPreview.load(imageUrl) {
                transformations(RoundedCornersTransformation(corners))
            }

            binding.ivProductPreview.setOnClickListener { onItemClick(position) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}