package com.example.hxh_project.presentation.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentProfileBinding
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment
import com.example.hxh_project.presentation.ui.sign_in.SignInFragment
import com.example.hxh_project.utils.State
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        profileViewModel.getData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserver(view)
        toolBarMenuLister()
        val exitDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.btn_logout_text)
            .setMessage(R.string.exit_dialog_message)
            .setPositiveButton(R.string.btn_logout_text) { _, _ ->
                profileViewModel.logOut()
                navToLogIn()
            }
            .setNegativeButton(R.string.btn_cancel_text, null)
            .create()

        binding.btnLogOut.setOnClickListener {
            exitDialog.show()
        }
    }

    private fun viewModelObserver(view: View) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.uiState.collect() {
                    when(it){
                        is State.Idle -> {}
                        is State.Loading -> {
                        }
                        is State.Success -> {
                            onSuccess(it.data)
                        }
                        is State.Error -> {
                            onError(view)
                        }
                    }
                }
            }
        }
    }

    private fun toolBarMenuLister() {
        binding.toolbarProfile.setNavigationOnClickListener {
            parentFragmentManager.commit {
                replace<CatalogFragment>(R.id.main_activity_container)
                addToBackStack(null)
            }
        }
    }

    private fun navToLogIn() {
        parentFragmentManager.commit {
            replace<SignInFragment>(R.id.main_activity_container)
            addToBackStack(null)
        }
    }

    private fun onError(view: View) {
        val message = getString(R.string.error_loading_title) +
                "\n" + getString(R.string.error_loading_description)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
            show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onSuccess(data: ProfileData) {
        val appText = getText(R.string.app_version)
        binding.tvAppVersion.text = "$appText ${data.version}"

        binding.profileContainer.apply {
            setImage(data.profile.avatarUrl)
            setUsername("${data.profile.name} ${data.profile.surname}")
            setJobTitle(data.profile.occupation)
        }
    }
}