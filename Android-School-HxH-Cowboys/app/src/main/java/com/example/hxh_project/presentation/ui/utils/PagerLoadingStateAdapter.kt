package com.example.hxh_project.presentation.ui.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hxh_project.databinding.NetworkStateItemBinding

class PagerLoadingStateAdapter(
    private val errorMessage: String,
    private val retry: () -> Unit
):  LoadStateAdapter<PagerLoadingStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(
        holder: PagerLoadingStateAdapter.LoadStateViewHolder,
        loadState: LoadState
    ) {
        when(loadState) {
            is LoadState.Loading -> {
                holder.binding.progressBar.isVisible = true
                holder.binding.tvError.isVisible = false
                holder.binding.btnRetry.isVisible = false
            }
            is LoadState.Error -> {
                holder.binding.progressBar.isVisible = false
                holder.binding.tvError.isVisible = true
                holder.binding.btnRetry.isVisible = true
                holder.binding.tvError.text = errorMessage
            }
            else -> {
                holder.binding.progressBar.isVisible = false
                holder.binding.tvError.isVisible = false
                holder.binding.btnRetry.isVisible = false
            }
        }

        holder.binding.btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagerLoadingStateAdapter.LoadStateViewHolder {
        return LoadStateViewHolder(
            NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class LoadStateViewHolder(val binding: NetworkStateItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}