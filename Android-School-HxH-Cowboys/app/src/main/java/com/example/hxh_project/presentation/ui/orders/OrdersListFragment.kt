package com.example.hxh_project.presentation.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.*
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentOrdersListBinding
import com.example.hxh_project.domain.model.Order
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment
import com.example.hxh_project.presentation.ui.utils.SnackbarListener
import com.example.hxh_project.presentation.ui.utils.item_decorations.DividerDecoration
import com.example.hxh_project.presentation.ui.utils.item_decorations.MarginItemDecoration
import com.example.hxh_project.presentation.ui.orders.adapters.OrdersAdapter
import com.example.hxh_project.presentation.ui.utils.PagerLoadingStateAdapter
import com.example.hxh_project.utils.FormatUtils.formatDate
import com.example.hxh_project.utils.extensions.navigateTo
import com.example.hxh_project.utils.extensions.setWindowTransparency
import com.example.hxh_project.utils.extensions.updateMargin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class OrdersListFragment : Fragment() {
    private lateinit var binding: FragmentOrdersListBinding

    private val viewModel: OrdersViewModel by activityViewModels()
    private lateinit var ordersAdapter: OrdersAdapter

    private var snackbarListener: SnackbarListener? = null

    private var isAllOrders: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ordersAdapter = OrdersAdapter(requireContext(), ::onButtonCancelClickListener)
        snackbarListener = requireActivity() as? SnackbarListener
        isAllOrders = requireArguments().getBoolean(ARG_IS_ALL_ORDERS)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersListBinding.inflate(inflater, container, false)
        initUiElements()
        setListeners()
        return binding.root
    }

    private fun initUiElements() {
        binding.rvActiveOrders.apply {
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
        val ordersFlow = if (isAllOrders == true) viewModel.allOrders else viewModel.activeOrders

        ordersFlow
            .flowWithLifecycle(lifecycle)
            .onEach { ordersAdapter.submitData(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.cancelOrderResultFlow
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                when(state) {
                    is OrderState.Failure ->
                        snackbarListener?.showError(getString(R.string.error_on_cancel_order))
                    is OrderState.Success -> {
                        val date = formatDate(state.order.createdAt)
                        val message = getString(
                            R.string.cancel_order_success,
                            state.order.number,
                            date.first,
                            date.second
                        )
                        snackbarListener?.showSuccess(message)
                    }
                }
                ordersAdapter.setLoadingPosition(null)
                ordersAdapter.refresh()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)


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
        viewModel.cancelOrder(order.id)
    }

    private fun stateHandler(loadStates: CombinedLoadStates) {
        when(val state = loadStates.refresh) {
            is LoadState.Error -> {
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
                if (ordersAdapter.itemCount == 0) {
                    binding.progressContainer.setButtonTitle(getString(R.string.btn_to_catalog_text))
                    binding.progressContainer.state = ProgressContainer.State.Notice(
                        R.drawable.img_logo,
                        getString(R.string.empty_state_orders_progress_container_title),
                        getString(R.string.empty_state_orders_progress_container_description)
                    ) {
                        navigateTo<CatalogFragment>(true)
                    }
                }else {
                    binding.progressContainer.state = ProgressContainer.State.Success
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ordersAdapter.refresh()
        binding.root.requestLayout() // fix for flying layout
    }

    override fun onStart() {
        super.onStart()
        binding.root.requestLayout()
    }

    companion object {
        private const val ARG_IS_ALL_ORDERS = "isAllOrders"

        @JvmStatic
        fun newInstance(isAllOrders: Boolean) =
            OrdersListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_ALL_ORDERS, isAllOrders)
                }
            }
    }
}