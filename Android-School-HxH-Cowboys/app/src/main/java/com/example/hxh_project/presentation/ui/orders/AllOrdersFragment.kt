package com.example.hxh_project.presentation.ui.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentActiveOrdersBinding
import com.example.hxh_project.databinding.FragmentAllOrdersBinding
import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.catalog.CatalogAdapter
import com.example.hxh_project.presentation.ui.catalog.item_decoration.DividerDecoration
import com.example.hxh_project.presentation.ui.catalog.item_decoration.MarginItemDecoration
import com.example.hxh_project.presentation.ui.product.ProductState
import com.example.hxh_project.utils.OrderStatusType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllOrdersFragment : Fragment() {
    private lateinit var binding: FragmentAllOrdersBinding
    private val ordersViewModel: OrdersViewModel by viewModels()

    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ordersViewModel.getOrders(OrderStatusType.All)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserver()

        ordersAdapter = OrdersAdapter(requireContext()) {

        }

        binding.rvAllOrders.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = ordersAdapter
            addItemDecoration(DividerDecoration(requireContext()))
            addItemDecoration(MarginItemDecoration(requireContext()))
        }
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ordersViewModel.uiState.collect() {
                    ordersStateHandler(it)
                }
            }
        }
    }

    private fun ordersStateHandler(state: OrderState) {
        if (state.isLoading) {
            binding.progressContainer.state = ProgressContainer.State.Loading
        }

        if (state.isEmpty) {
            binding.progressContainer.state = ProgressContainer.State.Notice(
                R.drawable.img_logo,
                R.string.empty_state_title,
                R.string.empty_state_description
            ) {
                ordersViewModel.getOrders(OrderStatusType.All)
            }
        } else {
            binding.progressContainer.state = ProgressContainer.State.Success
            ordersAdapter.submitList(state.orders)
        }

        val errorMessage = state.error
        if (errorMessage != null) {
            binding.progressContainer.state = ProgressContainer.State.Notice(
                R.drawable.img_logo,
                R.string.error_loading_title,
                R.string.error_loading_description,
            ){
                ordersViewModel.getOrders(OrderStatusType.All)
            }
        }
    }
}