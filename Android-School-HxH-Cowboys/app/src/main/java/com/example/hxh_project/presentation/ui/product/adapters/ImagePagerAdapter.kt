package com.example.hxh_project.presentation.ui.product.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.hxh_project.R
import com.example.hxh_project.databinding.ImagePagerBinding

class ImagePagerAdapter: RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
    private lateinit var binding: ImagePagerBinding
    private lateinit var context: Context

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        binding = ImagePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ImageViewHolder(binding.root)
    }

    fun submitList(images: List<String>) {
        differ.submitList(images)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageUrl: String) {
            val corners = context.resources.getDimension(R.dimen.preview_corner_radius)
            binding.ivPagerProductPreview.load(imageUrl) {
                transformations(RoundedCornersTransformation(corners))
            }
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