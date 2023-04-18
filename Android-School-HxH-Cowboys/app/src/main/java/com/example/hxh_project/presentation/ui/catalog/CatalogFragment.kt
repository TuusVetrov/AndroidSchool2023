package com.example.hxh_project.presentation.ui.catalog

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hxh_project.R
import com.example.hxh_project.data.remote.utils.json
import com.example.hxh_project.databinding.FragmentCatalogBinding
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.catalog.item_decoration.DividerDecoration
import com.example.hxh_project.presentation.ui.catalog.item_decoration.MarginItemDecoration
import com.example.hxh_project.presentation.ui.product.ProductFragment
import com.example.hxh_project.presentation.ui.profile.ProfileFragment
import com.example.hxh_project.presentation.ui.sign_in.SignInFragment
import com.example.hxh_project.presentation.ui.sign_in.SignInState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {
    private lateinit var binding: FragmentCatalogBinding
    private lateinit var productsAdapter: CatalogAdapter

    private val catalogViewModel: CatalogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catalogViewModel.getProducts()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarCatalog.inflateMenu(R.menu.menu_layout)

        viewModelObserver()
        toolBarMenuLister()

        productsAdapter = CatalogAdapter { product ->
            onItemClick(product.id, product.title)
        }

        binding.rvCatalog.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = productsAdapter
            addItemDecoration(MarginItemDecoration(requireContext()))
            addItemDecoration(DividerDecoration(requireContext()))
        }

    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                catalogViewModel.uiState.collect {
                    catalogStateHandler(it)
                }
            }
        }
    }

    private fun catalogStateHandler(state: CatalogState) {
        if(state.isUserLoggedIn == false) {
            navToLogIn()
        }

        if (state.isLoading) {
            binding.progressContainer.state = ProgressContainer.State.Loading
        }

        if (state.isEmpty) {
            binding.progressContainer.state = ProgressContainer.State.Notice(
                R.drawable.img_logo,
                R.string.empty_state_title,
                R.string.empty_state_description
            ) {
                catalogViewModel.getProducts()
            }
        } else {
            binding.progressContainer.state = ProgressContainer.State.Success
            productsAdapter.submitList(state.products)
        }

        val errorMessage = state.error
        if (errorMessage != null) {
            binding.progressContainer.state = ProgressContainer.State.Notice(
                R.drawable.img_logo,
                R.string.empty_state_title,
                R.string.empty_state_description
            ) {
                catalogViewModel.getProducts()
            }
        }
    }

    private fun toolBarMenuLister() {
        binding.toolbarCatalog.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.profileIcon -> {
                    onProfileClick()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun onItemClick(productId: String, productName: String) {
        val fragment = ProductFragment.newInstance(productId, productName)
        parentFragmentManager.commit {
            replace(R.id.main_activity_container, fragment)
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