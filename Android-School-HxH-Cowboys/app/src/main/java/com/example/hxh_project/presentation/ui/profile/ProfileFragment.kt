package com.example.hxh_project.presentation.ui.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentProfileBinding
import com.example.hxh_project.domain.model.Profile
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment
import com.example.hxh_project.presentation.ui.sign_in.SignInFragment
import com.example.hxh_project.presentation.ui.sign_in.SignInState
import com.example.hxh_project.utils.State
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel.getUserProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        toolBarMenuLister()

        val exitDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.btn_logout_text)
            .setMessage(R.string.exit_dialog_message)
            .setPositiveButton(R.string.btn_logout_text) { _, _ ->
                profileViewModel.logout()
                navToLogIn()
            }
            .setNegativeButton(R.string.btn_cancel_text, null)
            .create()

        binding.btnLogOut.setOnClickListener {
            exitDialog.show()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.uiState.collect {
                    profileStateHandler(it)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun profileStateHandler(state: ProfileState) {
        if (!state.isUserLoggedIn) {
            navToLogIn()
        }

        val appText = getText(R.string.app_version)
        binding.tvAppVersion.text = "$appText ${state.appVersion}"

        binding.profileContainer.apply {
            state.profile?.let { setImage(it.avatarId ) }
            setUsername("${state.profile?.name} ${state.profile?.surname}")
            setJobTitle(state.profile?.occupation ?: "")
        }

        val errorMessage = state.error
        if (errorMessage != null) {
            view?.let {
                Snackbar.make(it, errorMessage, Snackbar.LENGTH_SHORT).apply {
                    setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
                    animationMode = Snackbar.ANIMATION_MODE_FADE
                    show()
                }
            }
        }
    }

    private fun toolBarMenuLister() {
        binding.toolbarProfile.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun navToLogIn() {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        parentFragmentManager.commit {
            replace<SignInFragment>(R.id.main_activity_container)
        }
    }
}