package com.example.hxh_project.presentation.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentProfileBinding
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment
import com.example.hxh_project.presentation.ui.orders.OrdersFragment
import com.example.hxh_project.presentation.ui.settings.SettingsFragment
import com.example.hxh_project.presentation.ui.utils.SnackbarListener
import com.example.hxh_project.utils.extensions.navigateLogout
import com.example.hxh_project.utils.extensions.navigateTo
import com.example.hxh_project.utils.extensions.setWindowTransparency
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()


    private lateinit var exitDialog: AlertDialog
    private var snackbarListener: SnackbarListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUserProfile()
        snackbarListener = requireActivity() as? SnackbarListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        createExitDialog()
        setListeners()

        setWindowTransparency(binding.root) { statusBarSize, _ ->
            binding.appBarLayout.updatePadding(top = statusBarSize)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun createExitDialog() {
        exitDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.btn_logout_text)
            .setMessage(R.string.exit_dialog_message)
            .setPositiveButton(R.string.btn_logout_text) { _, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        viewModel.logout()
                    }
                    navigateLogout()
                }
            }
            .setNegativeButton(R.string.btn_cancel_text, null)
            .create()
    }

    private fun setListeners() {
        toolBarMenuLister()

        binding.btnLogOut.setOnClickListener { exitDialog.show() }

        binding.btnOrders.setOnClickListener { navigateTo<OrdersFragment>(true) }

        binding.btnSettings.setOnClickListener { navigateTo<SettingsFragment>(true) }
    }

    private fun toolBarMenuLister() {
        binding.toolbarProfile.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
            navigateTo<CatalogFragment>(false)
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    profileStateHandler(it)
                }
            }
        }
    }

    private fun profileStateHandler(state: ProfileState) {
        if (!state.isUserLoggedIn) {
            navigateLogout()
            return
        }

        binding.tvAppVersion.text = state.appVersion

        binding.profileContainer.apply {
            state.userImage?.let { setImage(it) }
            setUsername("${state.profile?.name} ${state.profile?.surname}")
            setJobTitle(state.profile?.occupation ?: "")
        }

        val errorMessage = state.error
        if (errorMessage != null) {
            snackbarListener?.showError(errorMessage)
            viewModel.clearError()
        }
    }
}