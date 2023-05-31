package com.example.hxh_project.presentation.ui.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentOrdersBinding
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment
import com.example.hxh_project.presentation.ui.orders.adapters.OrdersAdapter
import com.example.hxh_project.presentation.ui.orders.adapters.ViewPagerAdapter
import com.example.hxh_project.presentation.ui.sign_in.SignInFragment
import com.example.hxh_project.utils.extensions.navigateTo
import com.example.hxh_project.utils.extensions.setWindowTransparency
import com.example.hxh_project.utils.extensions.updateMargin
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding

    private val viewModel: OrdersViewModel by viewModels()

    private lateinit var adapterAllOrders: OrdersAdapter
    private lateinit var adapterActiveOrders: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        adapterAllOrders = OrdersAdapter(requireContext()) { }
        adapterActiveOrders = OrdersAdapter(requireContext()) { }

        setWindowTransparency(binding.root) { statusBarSize, navigationBarSize ->
            binding.appBarLayout.updatePadding(top = statusBarSize)
            binding.viewPager.updatePadding(bottom = navigationBarSize)
        }

        initUiElements()
        setListeners()

        return binding.root
    }

    private fun initUiElements() {
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle).apply {
            addFragment(AllOrdersFragment())
            addFragment(ActiveOrdersFragment())
        }

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = resources.getText(R.string.all_orders_tab)
                else -> tab.text = resources.getText(R.string.active_orders_tab)
            }
        }.attach()

        changeOrdersTabCount(adapterAllOrders, 0, R.string.all_orders_tab)
        changeOrdersTabCount(adapterActiveOrders, 1, R.string.active_orders_tab)
    }

    private fun changeOrdersTabCount(
        adapter: OrdersAdapter,
        tabNumber: Int,
        stringResId: Int
    ) {
        adapter.loadStateFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                val ordersNumber = when (it.refresh) {
                    is LoadState.NotLoading -> {
                        if (adapter.itemCount == 0) "" else adapter.itemCount.toString()
                    }
                    else -> ""
                }
                binding.tabLayout.getTabAt(tabNumber)?.text = getString(stringResId) + " $ordersNumber"
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setListeners() {
        viewModel.allOrders
            .flowWithLifecycle(lifecycle)
            .onEach {
                adapterAllOrders.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            adapterAllOrders.loadStateFlow.collectLatest { loadStates ->
                stateHandler(loadStates)
            }
        }

        viewModel.activeOrders
            .flowWithLifecycle(lifecycle)
            .onEach {
                adapterActiveOrders.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.toolbarProfile.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun stateHandler(loadStates: CombinedLoadStates) {
        when(val state = loadStates.refresh) {
            is LoadState.Error -> {
                binding.progressContainer.state = ProgressContainer.State.Notice(
                    R.drawable.img_logo,
                    getString(R.string.error_progress_container_title),
                    state.error.message.orEmpty()
                ) {
                    adapterAllOrders.refresh()
                }
            }
            is LoadState.Loading ->{
                binding.progressContainer.state = ProgressContainer.State.Loading
            }
            is LoadState.NotLoading -> {
                binding.progressContainer.setButtonTitle(getString(R.string.btn_to_catalog_text))
                if (adapterAllOrders.itemCount == 0) {
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