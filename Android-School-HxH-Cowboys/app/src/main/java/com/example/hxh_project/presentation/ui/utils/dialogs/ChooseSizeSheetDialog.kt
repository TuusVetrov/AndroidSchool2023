package com.example.hxh_project.presentation.ui.utils.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hxh_project.R
import com.example.hxh_project.databinding.DialogSizeBinding
import com.example.hxh_project.domain.model.ProductSize
import com.example.hxh_project.presentation.ui.product.adapters.ProductSizeAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseSizeSheetDialog(
    onItemClick: (String) -> Unit
): BottomSheetDialogFragment() {
    private lateinit var binding: DialogSizeBinding
    private val productSizeAdapter = ProductSizeAdapter(onItemClick)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                initRecyclerView()
            }
        }
    }

    fun submitList(products: List<ProductSize>) {
        productSizeAdapter.submitList(products)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSizeBinding.bind(
            inflater.inflate(R.layout.dialog_size, container)
        )
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvSize.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSize.adapter = productSizeAdapter
    }

    companion object {
        const val TAG = "ChooseSizeBottomDialog"
    }
}