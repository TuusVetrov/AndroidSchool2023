package com.example.hxh_project.presentation.ui.catalog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentCatalogBinding
import com.example.hxh_project.domain.model.Profile
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.catalog.item_decoration.DividerItemDecoration
import com.example.hxh_project.presentation.ui.catalog.item_decoration.MarginItemDecoration
import com.example.hxh_project.presentation.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {
    private lateinit var binding: FragmentCatalogBinding
    private val productsAdapter by lazy { CatalogAdapter() }

    private val catalogViewModel: CatalogViewModel by viewModels()

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
        catalogViewModel.fetchData()

        viewModelObserver()
        toolBarMenuLister()

        val margins = MarginItemDecoration(requireContext()).apply {
            setColor(R.color.white)
            setCornerRadius(16)
            setMargin(16)
        }

        val divider = DividerItemDecoration(requireContext()).apply {
            setColor(R.color.seashell)
            setMarginHorizontal(16)
        }

        binding.rvCatalog.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = productsAdapter
            addItemDecoration(margins)
            addItemDecoration(divider)
        }
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                catalogViewModel.uiState.collect() {
                    when(it){
                        is UiState.Loading -> {
                            binding.progressContainer.state = ProgressContainer.State.Loading
                        }
                        is UiState.Success -> {
                            binding.progressContainer.state = ProgressContainer.State.Success
                            productsAdapter.submitList(it.data)
                        }
                        is UiState.Notice -> {
                            binding.progressContainer.state =
                                ProgressContainer.State.Notice(
                                    it.imageId,
                                    it.message,
                                    it.description) {
                                    catalogViewModel.fetchData()
                                }
                        }
                    }
                }
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

    private fun onProfileClick() {
        parentFragmentManager.commit {
            replace<ProfileFragment>(R.id.main_activity_container)
            addToBackStack(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CatalogFragment()
    }
}