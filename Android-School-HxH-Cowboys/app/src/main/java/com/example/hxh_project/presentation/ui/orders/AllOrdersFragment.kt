package com.example.hxh_project.presentation.ui.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentAllOrdersBinding
import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment
import com.example.hxh_project.presentation.ui.utils.SnackbarListener
import com.example.hxh_project.presentation.ui.utils.item_decorations.DividerDecoration
import com.example.hxh_project.presentation.ui.utils.item_decorations.MarginItemDecoration
import com.example.hxh_project.presentation.ui.orders.adapters.OrdersAdapter
import com.example.hxh_project.presentation.ui.sign_in.SignInFragment
import com.example.hxh_project.presentation.ui.utils.PagerLoadingStateAdapter
import com.example.hxh_project.utils.FormatUtils.formatDate
import com.example.hxh_project.utils.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllOrdersFragment : Fragment() {
    private lateinit var binding: FragmentAllOrdersBinding

    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var ordersAdapter: OrdersAdapter

    private var snackbarListener: SnackbarListener? = null
    private lateinit var message: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        snackbarListener = requireActivity() as? SnackbarListener
        ordersAdapter = OrdersAdapter(requireContext(), ::onButtonCancelClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllOrdersBinding.inflate(inflater, container, false)
        initUiElements()
        setListeners()
        return binding.root
    }

    private fun initUiElements() {
        binding.rvAllOrders.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            val errorMessage = getString(R.string.error_on_loading_orders)
            adapter = ordersAdapter.withLoadStateFooter(
                footer = PagerLoadingStateAdapter(errorMessage) { onRetryClick() }
            )
            addItemDecoration(MarginItemDecoration(requireContext()))
            addItemDecoration(DividerDecoration(requireContext()))
        }
    }

    private fun setListeners() {
        viewModel.allOrders
            .flowWithLifecycle(lifecycle)
            .onEach {
                ordersAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.cancelOrderResultFlow
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                when(state) {
                    is OrderState.Failure ->
                        snackbarListener?.showError(getString(R.string.error_on_cancel_order))
                    is OrderState.Success ->
                        snackbarListener?.showSuccess(message)
                }
                ordersAdapter.setLoadingPosition(null)
                ordersAdapter.refresh()
            }.launchIn(viewLifecycleOwner.lifecycleScope)


        viewLifecycleOwner.lifecycleScope.launch {
            ordersAdapter.loadStateFlow.collectLatest { loadStates ->
                stateHandler(loadStates)
            }
        }
    }

    private fun onRetryClick() {
        ordersAdapter.retry()
    }

    private fun onButtonCancelClickListener(order: Order) {
        val date = formatDate(order.createdAt)
        message = getString(R.string.cancel_order_success, order.number, date.first, date.second)
        viewModel.cancelOrder(order.id)
    }

    private fun stateHandler(loadStates: CombinedLoadStates) {
        when(val state = loadStates.refresh) {
            is LoadState.Error -> {
                binding.progressContainer.setButtonTitle(getString(R.string.btn_to_catalog_text))
                binding.progressContainer.state = ProgressContainer.State.Notice(
                    R.drawable.img_logo,
                    getString(R.string.error_progress_container_title),
                    state.error.message.orEmpty()
                ) {
                    ordersAdapter.refresh()
                }
            }
            is LoadState.Loading ->{
                binding.progressContainer.state = ProgressContainer.State.Loading
            }
            is LoadState.NotLoading -> {
                binding.progressContainer.setButtonTitle(getString(R.string.btn_to_catalog_text))
                if (ordersAdapter.itemCount == 0) {
                    binding.progressContainer.state = ProgressContainer.State.Notice(
                        R.drawable.img_logo,
                        getString(R.string.empty_state_orders_progress_container_title),
                        getString(R.string.empty_state_orders_progress_container_description)
                    ) {
                        navigateTo<CatalogFragment>(false)
                    }
                }else {
                    binding.progressContainer.state = ProgressContainer.State.Success
                }
            }
        }
    }
}