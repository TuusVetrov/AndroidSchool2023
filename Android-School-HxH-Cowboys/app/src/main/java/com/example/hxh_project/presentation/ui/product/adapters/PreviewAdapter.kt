package com.example.hxh_project.presentation.ui.product.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.hxh_project.databinding.ProductPreviewItemBinding

class PreviewAdapter(
    private val images: List<String>,
    private val viewPager: ViewPager2
): RecyclerView.Adapter<PreviewAdapter.PreviewViewHolder>() {
    private lateinit var binding: ProductPreviewItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        binding = ProductPreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PreviewViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class PreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = binding.ivProductPreview

        fun bind(imageUrl: String) {
            imageView.load(imageUrl) {
                transformations(RoundedCornersTransformation(10f))
            }

            imageView.setOnClickListener {
                viewPager.setCurrentItem(position, true)
            }
        }
    }
}