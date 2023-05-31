package com.example.hxh_project.presentation.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentCheckOutBinding
import com.example.hxh_project.domain.model.Product
import com.example.hxh_project.data.model.response.CheckOutResponse
import com.example.hxh_project.presentation.ui.utils.SnackbarListener
import com.example.hxh_project.presentation.ui.utils.dialogs.ChooseSizeSheetDialog
import com.example.hxh_project.presentation.ui.orders.OrdersFragment
import com.example.hxh_project.presentation.ui.place_picker.PlacePickerFragment
import com.example.hxh_project.utils.FormatUtils
import com.example.hxh_project.utils.FormatUtils.formatDate
import com.example.hxh_project.utils.FormatUtils.millisToDate
import com.example.hxh_project.utils.FormatUtils.millisToIsoDateTime
import com.example.hxh_project.utils.extensions.navigateTo
import com.example.hxh_project.utils.extensions.setError
import com.example.hxh_project.utils.extensions.setWindowTransparency
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private const val ARG_PRODUCT_ID = "productId"
private const val ARG_SIZE = "productSize"

@AndroidEntryPoint
class CheckOutFragment : Fragment() {

    private var productId: String? = null
    private var productSize: String? = null

    private val viewModel: CheckOutViewModel by viewModels()

    private lateinit var binding: FragmentCheckOutBinding
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var sizeDialog: ChooseSizeSheetDialog

    private var listener: SnackbarListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            productSize = it.getString(ARG_SIZE)
        }

        productId?.let { viewModel.setProductId(it) }
        productSize?.let { viewModel.setProductSize(it) }
        viewModel.getProduct()
        datePicker = setupDatePicker()
        listener = requireActivity() as? SnackbarListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckOutBinding.inflate(inflater, container, false)
        sizeDialog = ChooseSizeSheetDialog(::onSizeClick)
        initElements()
        setupListeners()

        setWindowTransparency(binding.root) { statusBarSize, navigationBarSize ->
            binding.appBarLayout.updatePadding(top = statusBarSize)
            binding.btnBuy.updatePadding(bottom = navigationBarSize)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserver()
    }

    private fun setupListeners() {
        setFragmentResultListener(PlacePickerFragment.REQUEST_KEY) { _, bundle ->
            val result = bundle.getString(PlacePickerFragment.KEY_ADDRESS)
            result?.let { viewModel.setAddress(it) }
        }

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.counter.setOnCountChangedListener { value ->
            viewModel.setQuantity(value)
        }

        binding.textInputProductSize.editText?.setOnClickListener {
            sizeDialog.show(parentFragmentManager, ChooseSizeSheetDialog.TAG)
        }

        binding.textInputAddress.editText?.setOnClickListener {
            parentFragmentManager.commit {
                replace<PlacePickerFragment>(R.id.main_activity_container)
                addToBackStack(null)
            }
        }

        binding.textInputDeliveryDate.editText?.setOnClickListener {
            datePicker.show(parentFragmentManager, datePicker.tag)
        }

        binding.textInputFlatNumber.editText?.addTextChangedListener {
            viewModel.setApartment(it.toString())
        }

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            viewModel.setEtd(millisToIsoDateTime(selectedDateInMillis))
            viewModel.setDateForView(millisToDate(selectedDateInMillis))
        }

        binding.btnBuy.setOnClickListener {
            viewModel.checkOut()
        }
    }

    private fun initElements() {
        binding.textInputProductSize.visibility =
            if (productSize == null) View.VISIBLE else View.GONE

        binding.textInputProductSize.editText?.apply {
            isClickable = false
            isFocusable = false
        }

        binding.textInputDeliveryDate.editText?.apply {
            isClickable = false
            isFocusable = false
        }

        binding.textInputAddress.editText?.apply {
            isClickable = false
            isFocusable = false
        }
    }

    private fun productStateHandler(state: CheckOutState) {
        binding.btnBuy.isLoading = state.isLoading

        binding.textInputAddress.editText?.setText(state.address)
        binding.textInputDeliveryDate.editText?.setText(state.dateForView)
        binding.textInputProductSize.editText?.setText(state.productSize)
        binding.counter


        state.product?.let { onSuccess(it) }
        state.product?.let { sizeDialog.submitList(it.sizes) }
        state.order?.let { navigateToOrders(it) }

        binding.textInputProductSize.setError(state.isValidSize == false) {
            getString(R.string.message_field_size_invalid)
        }

        binding.textInputAddress.setError(state.isValidAddress == false) {
            getString(R.string.message_field_address_invalid)
        }

        binding.textInputDeliveryDate.setError(state.isValidDate == false) {
            getString(R.string.message_field_date_invalid)
        }

        val errorMessage = state.error
        if (errorMessage != null) {
            listener?.showError(errorMessage)
        }
    }

    private fun onSuccess(data: Product) {
        val corners = requireContext().resources.getDimension(R.dimen.preview_corner_radius)
        val price = FormatUtils.priceFormat(data.price)

        binding.ivItemPreview.load(data.preview)
        binding.ivItemPreview.load(data.preview) {
            transformations(RoundedCornersTransformation(corners))
            placeholder(R.drawable.img_logo)
            error(R.drawable.img_logo)
        }

        val size = viewModel.uiState.value.productSize

        val title = if (size.isNotEmpty()) "$size • ${data.title}" else data.title



        binding.tvTitle.text = title
        binding.tvCategory.text = data.department
        binding.counter.maxQuantity = 10 // TODO: maybe need get count items in stock

        binding.btnBuy.setText(requireContext().getString(R.string.btn_checkout_text, price))
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

    private fun setupDatePicker(): MaterialDatePicker<Long> {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        return MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
    }

    private fun onSizeClick(size: String) {
        viewModel.setProductSize(size)
        binding.textInputProductSize.editText?.setText(size)
        sizeDialog.dismiss()
    }

    private fun navigateToOrders(order: CheckOutResponse) {
        val date = formatDate(order.data.createdAt)
        val message = getString(R.string.check_out_success, order.data.number, date.first, date.second)

        listener?.showSuccess(message)

        navigateTo<OrdersFragment>(false)
    }

      companion object {
          @JvmStatic
          fun newInstance(productId: String, productSize: String?) =
              CheckOutFragment().apply {
                  arguments = Bundle().apply {
                      putString(ARG_PRODUCT_ID, productId)
                      putString(ARG_SIZE, productSize)
                  }
              }
      }
}