package com.example.hxh_project.presentation.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentProductBinding
import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.presentation.components.ProgressContainer
import com.example.hxh_project.presentation.ui.checkout.CheckOutFragment
import com.example.hxh_project.presentation.ui.utils.dialogs.ChooseSizeSheetDialog
import com.example.hxh_project.presentation.ui.utils.item_decorations.SelectedItemDecoration
import com.example.hxh_project.presentation.ui.product.adapters.ImagePagerAdapter
import com.example.hxh_project.presentation.ui.product.adapters.PeculiaritiesAdapter
import com.example.hxh_project.presentation.ui.product.adapters.PreviewAdapter
import com.example.hxh_project.utils.FormatUtils.priceFormat
import com.example.hxh_project.utils.extensions.navigateLogout
import com.example.hxh_project.utils.extensions.setWindowTransparency
import com.example.hxh_project.utils.extensions.updateMargin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private val viewModel: ProductViewModel by viewModels()

    private var productId: String? = null
    private var productName: String? = null

    private lateinit var imagePagerAdapter: ImagePagerAdapter
    private lateinit var previewAdapter: PreviewAdapter
    private lateinit var peculiaritiesAdapter: PeculiaritiesAdapter

    private lateinit var sizeDialog: ChooseSizeSheetDialog

    private lateinit var viewTreeObserver: ViewTreeObserver.OnGlobalLayoutListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            productName = it.getString(ARG_PRODUCT_NAME)
        }

        productId?.let { viewModel.setProductId(it) }
        viewModel.getProduct()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)

        initUiComponents()
        setListeners()

        setWindowTransparency(binding.root) { statusBarSize, navigationBarSize ->
            val afterRecyclerView = resources.getDimension(R.dimen.normal_400).toInt()
            val afterButton = resources.getDimension(R.dimen.normal_200).toInt()

            binding.toolbarProduct.updateMargin(top = statusBarSize)
            binding.btnBuyNow.updateMargin(bottom = navigationBarSize + afterButton)

            viewTreeObserver = ViewTreeObserver.OnGlobalLayoutListener {
                val buttonSize = binding.btnBuyNow.height
                binding.productScroll.rvPeculiarities.updatePadding(
                    bottom = navigationBarSize + afterRecyclerView + buttonSize + afterButton
                )
            }

            binding.btnBuyNow.viewTreeObserver.addOnGlobalLayoutListener(viewTreeObserver)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserver()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun previewAdapterOnClick(position: Int) {
        binding.productScroll.vpProduct.setCurrentItem(position, true)
    }

    private fun initUiComponents() {
        productName?.let { binding.toolbarProduct.title = it }

        sizeDialog = ChooseSizeSheetDialog(::onSizeClick)
        imagePagerAdapter = ImagePagerAdapter()
        previewAdapter = PreviewAdapter(::previewAdapterOnClick)
        peculiaritiesAdapter = PeculiaritiesAdapter()

        binding.productScroll.vpProduct.adapter = imagePagerAdapter

        val selectedItemDecoration =
            SelectedItemDecoration(requireContext(), binding.productScroll.vpProduct.currentItem)

        binding.productScroll.rvPreviewProduct.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = previewAdapter
            addItemDecoration(selectedItemDecoration)
        }

        binding.productScroll.rvPeculiarities.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = peculiaritiesAdapter
        }

        syncPreview(selectedItemDecoration)
    }

    private fun setListeners() {
        toolBarMenuLister()

        binding.btnBuyNow.setOnClickListener {
            onButtonBuyClick()
        }

        binding.productScroll.tilProductSize.editText?.apply {
            isFocusable = false
            isClickable = true

            setOnClickListener {
                sizeDialog.show(parentFragmentManager, ChooseSizeSheetDialog.TAG)
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
                viewModel.uiState.collect() {
                    productStateHandler(it)
                }
            }
        }
    }

    private fun productStateHandler(state: ProductState) {
        if (state.isUserLoggedIn == false) {
            navigateLogout()
            return
        }

        if (state.isLoading) {
            binding.progressContainer.state = ProgressContainer.State.Loading
        }

        if (binding.btnBuyNow.visibility == View.VISIBLE) {
            binding.btnBuyNow.viewTreeObserver.removeOnGlobalLayoutListener(viewTreeObserver)
        }

        state.product?.let { sizeDialog.submitList(it.sizes) }

        binding.productScroll.tilProductSize.editText?.setText(state.size)

        state.product?.let { onSuccess(it) }

        val errorMessage = state.error
        if (errorMessage != null) {
            binding.progressContainer.state = ProgressContainer.State.Notice(
                R.drawable.img_logo,
                getString(R.string.error_progress_container_title),
                errorMessage,
            ){
                viewModel.getProduct()
            }
        }
    }

    private fun onSuccess(data: Product) {
        binding.progressContainer.state = ProgressContainer.State.Success

        binding.btnBuyNow.visibility = View.VISIBLE

        binding.productScroll.tvProductPrice.text = priceFormat(data.price)
        binding.productScroll.tvProductTitle.text = data.title
        binding.productScroll.tvProductDepartment.text = data.department
        binding.productScroll.tvProductDescription.text = data.description

        imagePagerAdapter.submitList(data.images)
        previewAdapter.submitList(data.images)
        peculiaritiesAdapter.submitList(data.details)

        binding.productScroll.badge.setBackgroundColor(data.badge[0].color)
        binding.productScroll.badge.setText(data.badge[0].value)
    }

    private fun toolBarMenuLister() {
        binding.toolbarProduct.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun onButtonBuyClick() {
        val id = viewModel.uiState.value.productId
        val size = viewModel.uiState.value.size

        val destination = CheckOutFragment.newInstance(id, size)
        parentFragmentManager.commit {
            replace(R.id.main_activity_container, destination)
            addToBackStack(null)
        }
    }

    private fun onSizeClick(size: String) {
        viewModel.setSize(size)
        binding.productScroll.tilProductSize.editText?.setText(size)
        sizeDialog.dismiss()
    }

    companion object {
        private const val ARG_PRODUCT_ID = "productId"
        private const val ARG_PRODUCT_NAME = "productName"
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