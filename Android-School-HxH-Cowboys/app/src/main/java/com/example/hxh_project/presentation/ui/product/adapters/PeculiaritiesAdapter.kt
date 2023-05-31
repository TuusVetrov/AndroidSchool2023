package com.example.hxh_project.presentation.ui.product.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hxh_project.R
import com.example.hxh_project.databinding.PeculiaritiesItemBinding

class PeculiaritiesAdapter(): RecyclerView.Adapter<PeculiaritiesAdapter.PeculiaritiesViewHolder>() {
    private lateinit var binding: PeculiaritiesItemBinding
    private lateinit var context: Context

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeculiaritiesViewHolder {
        binding = PeculiaritiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return PeculiaritiesViewHolder(binding.root)
    }

    fun submitList(peculiarities: List<String>) {
        differ.submitList(peculiarities)
    }

    override fun getItemCount(): Int = differ.currentList.size


    override fun onBindViewHolder(holder: PeculiaritiesViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class PeculiaritiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(text: String) {
            val gapWidth = context.resources.getDimension(R.dimen.span_gap_width).toInt()
            val builder = SpannableStringBuilder(text)
            builder.setSpan(BulletSpan(gapWidth), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.tvPeculiarities.text = builder
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