package com.example.hxh_project.presentation.ui.catalog

import android.os.Bundle
import android.view.*
import androidx.core.view.updatePadding
import androidx.fragment.app.*
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentCatalogBinding
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.checkout.CheckOutFragment
import com.example.hxh_project.presentation.ui.utils.item_decorations.DividerDecoration
import com.example.hxh_project.presentation.ui.utils.item_decorations.MarginItemDecoration
import com.example.hxh_project.presentation.ui.product.ProductFragment
import com.example.hxh_project.presentation.ui.profile.ProfileFragment
import com.example.hxh_project.presentation.ui.sign_in.SignInFragment
import com.example.hxh_project.presentation.ui.utils.PagerLoadingStateAdapter
import com.example.hxh_project.utils.extensions.navigateTo
import com.example.hxh_project.utils.extensions.setWindowTransparency
import com.example.hxh_project.utils.extensions.updateMargin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CatalogFragment : Fragment() {
    private lateinit var binding: FragmentCatalogBinding

    private val viewModel: CatalogViewModel by viewModels()
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catalogAdapter = CatalogAdapter(requireContext(), ::onItemClick, ::onButtonBuyClick)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)

        setWindowTransparency(binding.root) { statusBarSize, navigationBarSize ->
            binding.appBarLayout.updatePadding(top = statusBarSize)
            binding.rvCatalog.updatePadding(bottom = navigationBarSize)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarCatalog.inflateMenu(R.menu.menu_layout)

        setupAdapter()
        setupListeners()
    }

    private fun setupAdapter() {
        binding.rvCatalog.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            val errorOnLoading = getString(R.string.error_on_loading_catalog)
            adapter = catalogAdapter.withLoadStateFooter(
                footer = PagerLoadingStateAdapter(errorOnLoading) { onRetryClick() }
            )
            addItemDecoration(MarginItemDecoration(requireContext()))
            addItemDecoration(DividerDecoration(requireContext()))
        }

        viewModel.currentResult
            .flowWithLifecycle(lifecycle)
            .onEach {
                catalogAdapter.submitData(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }

    private fun setupListeners() {
        binding.toolbarCatalog.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profileIcon -> {
                    navigateTo<ProfileFragment>(true)
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            catalogAdapter.refresh()
        }

        catalogAdapter.addLoadStateListener { loadState ->
            catalogStateHandler(loadState)
        }
    }

    private fun catalogStateHandler(loadStates: CombinedLoadStates) {
        when(val state = loadStates.refresh) {
            is LoadState.Error -> {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressContainer.state = ProgressContainer.State.Notice(
                    R.drawable.img_logo,
                    getString(R.string.error_progress_container_title),
                    state.error.message.orEmpty()
                ) {
                    catalogAdapter.refresh()
                }
            }
            is LoadState.Loading ->{
                binding.progressContainer.state = ProgressContainer.State.Loading
            }
            is LoadState.NotLoading -> {
                binding.swipeRefreshLayout.isRefreshing = false
                if (catalogAdapter.itemCount == 0) {
                    binding.progressContainer.state = ProgressContainer.State.Notice(
                        R.drawable.img_logo,
                        context?.getString(R.string.empty_state_title) ?: "",
                        context?.getString(R.string.empty_state_description) ?: ""
                    ) {
                        catalogAdapter.refresh()
                    }
                }else {
                    binding.progressContainer.state = ProgressContainer.State.Success
                }
            }
        }
    }

    private fun onRetryClick() {
        catalogAdapter.retry()
    }

    private fun onButtonBuyClick(productId: String) {
        val destination = CheckOutFragment.newInstance(productId, null)
        parentFragmentManager.commit {
            replace(R.id.main_activity_container, destination)
            addToBackStack(null)
        }
    }

    private fun onItemClick(productId: String, productName: String) {
        val destination = ProductFragment.newInstance(productId, productName)
        parentFragmentManager.commit {
            replace(R.id.main_activity_container, destination)
            addToBackStack(null)
        }
    }

    private fun navToLogIn() {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        parentFragmentManager.commit {
            replace<SignInFragment>(R.id.main_activity_container)
        }
    }

    private fun onProfileClick() {

        parentFragmentManager.commit {
            replace<ProfileFragment>(R.id.main_activity_container)
            addToBackStack(null)
        }
    }
}