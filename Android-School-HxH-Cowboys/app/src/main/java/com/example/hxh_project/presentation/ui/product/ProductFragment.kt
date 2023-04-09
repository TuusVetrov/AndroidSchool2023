package com.example.hxh_project.presentation.ui.product

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.hxh_project.R
import com.example.hxh_project.databinding.DialogSizeBinding
import com.example.hxh_project.databinding.FragmentProductBinding
import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.product.adapters.ImagePagerAdapter
import com.example.hxh_project.presentation.ui.product.adapters.PeculiaritiesAdapter
import com.example.hxh_project.presentation.ui.product.adapters.PreviewAdapter
import com.example.hxh_project.presentation.ui.product.adapters.ProductSizeAdapter
import com.example.hxh_project.utils.State
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARG_PRODUCT_ID = "productId"
private const val ARG_PRODUCT_NAME = "productName"

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private lateinit var dialogViewBinding: DialogSizeBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val productViewModel: ProductViewModel by viewModels()

    private var productId: String? = null
    private var productName: String? = null

    private lateinit var imagePagerAdapter: ImagePagerAdapter
    private lateinit var previewAdapter: PreviewAdapter
    private lateinit var productSizeAdapter: ProductSizeAdapter
    private lateinit var peculiaritiesAdapter: PeculiaritiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            productName = it.getString(ARG_PRODUCT_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarMenuLister()
        productName?.let { binding.toolbarProductLayout.title = it }
        productId?.let { productViewModel.getProduct(it) }
        viewModelObserver()

        val selectedItemDecoration =
            SelectedItemDecoration(requireContext(), binding.productScroll.vpProduct.currentItem)

        binding.productScroll.rvPreviewProduct.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(selectedItemDecoration)
        }

        binding.productScroll.rvPeculiarities.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        syncPreview(selectedItemDecoration)

        bottomSheetDialog()

        binding.productScroll.tiedProductSize.apply {
            isFocusable = false
            isClickable = true

            setOnClickListener {
                bottomSheetDialog.show()
            }
        }
    }

    private fun syncPreview(previewAdapter: SelectedItemDecoration) {
        binding.productScroll.vpProduct.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                previewAdapter.selectedPosition = position
                binding.productScroll.rvPreviewProduct.scrollToPosition(position)
            }
        })
    }

    private fun viewModelObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productViewModel.uiState.collect() {
                    when(it){
                        is State.Init -> {}
                        is State.Loading -> {
                            binding.progressContainer.state = ProgressContainer.State.Loading
                        }
                        is State.Success -> {
                            onSuccess(it.data.product)
                        }
                        is State.Error -> {
                            onError()
                        }
                    }
                }
            }
        }
    }

    private fun bottomSheetDialog() {
        dialogViewBinding = DialogSizeBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        dialogViewBinding.rvSize.layoutManager = LinearLayoutManager(requireContext())
        bottomSheetDialog.setContentView(dialogViewBinding.root)
    }

    @SuppressLint("SetTextI18n")
    private fun onSuccess(data: Product) {
        binding.productScroll.tvProductPrice.text =
            String.format("%,d ₽", data.price).replace(",", " ")
        binding.productScroll.tvProductTitle.text = data.title
        binding.productScroll.tvProductDepartment.text = data.department
        binding.productScroll.tiedProductSize.setText(data.sizes[0].value)
        binding.productScroll.tvProductDescription.text = data.description

        imagePagerAdapter = ImagePagerAdapter(data.images)
        previewAdapter = PreviewAdapter(data.images, binding.productScroll.vpProduct)
        productSizeAdapter = ProductSizeAdapter(requireContext(), data.sizes) { size ->
            bottomSheetDialog.dismiss()
            binding.productScroll.tiedProductSize.setText(size)
        }

        peculiaritiesAdapter = PeculiaritiesAdapter(data.details)

        initAdapters()
        binding.progressContainer.state = ProgressContainer.State.Success
    }

    private fun onError() {
        binding.progressContainer.state = ProgressContainer.State.Notice(
            R.drawable.img_logo,
            R.string.error_loading_title,
            R.string.error_loading_description,
        ){
            productId?.let { it1 -> productViewModel.getProduct(it1) }
        }
    }

    private fun initAdapters() {
        binding.productScroll.vpProduct.adapter = imagePagerAdapter
        binding.productScroll.rvPreviewProduct.adapter = previewAdapter
        dialogViewBinding.rvSize.adapter = productSizeAdapter
        binding.productScroll.rvPeculiarities.adapter = peculiaritiesAdapter
    }

    private fun toolBarMenuLister() {
        binding.toolbarProduct.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: String, name: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_ID, id)
                    putString(ARG_PRODUCT_NAME, name)
                }
            }
    }
}