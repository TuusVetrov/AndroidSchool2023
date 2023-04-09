package com.example.hxh_project.presentation.ui.product.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.hxh_project.R
import com.example.hxh_project.databinding.ImagePagerBinding
import com.example.hxh_project.databinding.ProductPreviewItemBinding

class ImagePagerAdapter(
    private val images: List<String>
): RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
    private lateinit var binding: ImagePagerBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        binding = ImagePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView = binding.ivPagerProductPreview

        fun bind(imageUrl: String) {
            imageView.load(imageUrl) {
                transformations(RoundedCornersTransformation(16f))
            }
        }
    }
}