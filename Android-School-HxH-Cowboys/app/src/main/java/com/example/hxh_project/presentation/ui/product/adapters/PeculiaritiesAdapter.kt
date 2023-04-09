package com.example.hxh_project.presentation.ui.product.adapters

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hxh_project.databinding.ImagePagerBinding
import com.example.hxh_project.databinding.PeculiaritiesItemBinding

class PeculiaritiesAdapter(
    private val peculiarities: List<String>
): RecyclerView.Adapter<PeculiaritiesAdapter.PeculiaritiesViewHolder>() {
    private lateinit var binding: PeculiaritiesItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeculiaritiesViewHolder {
        binding = PeculiaritiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeculiaritiesViewHolder(binding.root)
    }

    override fun getItemCount() = peculiarities.size


    override fun onBindViewHolder(holder: PeculiaritiesViewHolder, position: Int) {
        holder.bind(peculiarities[position])
    }

    inner class PeculiaritiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(text: String) {
            val builder = SpannableStringBuilder(text)
            builder.setSpan(BulletSpan(16), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.tvPeculiarities.text = builder
        }
    }
}