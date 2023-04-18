package com.example.hxh_project.presentation.ui.sign_in
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentSignInBinding
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment
import com.example.hxh_project.utils.AuthValidator
import com.example.hxh_project.utils.State
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()

        binding.textLogin.addTextChangedListener {
            signInViewModel.setEmail(it.toString())
        }

        binding.textPassword.addTextChangedListener {
            signInViewModel.setPassword(it.toString())
        }

        binding.buttonSignIn.setOnClickListener {
            signInViewModel.login()
        }

        binding.textPassword.setOnEditorActionListener {  _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ){
                signInViewModel.login()
                true
            } else {
                false
            }
        }
    }

    private fun signInStateHandler(state: SignInState) {
        binding.layoutLogin.error = getString(R.string.error_message_email_invalid)
        binding.layoutLogin.isErrorEnabled = state.isValidEmail == false

        binding.layoutPassword.error = getString(R.string.error_message_password_invalid)
        binding.layoutPassword.isErrorEnabled = state.isValidPassword == false

        if (state.isLoggedIn) {
            navigateToCatalog()
        }

        val errorMessage = state.error
        if (errorMessage != null) {
            val message = getString(R.string.error_loading_title) + "\n" + errorMessage
            view?.let {
                Snackbar.make(it, message, Snackbar.LENGTH_SHORT).apply {
                    setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
                    animationMode = Snackbar.ANIMATION_MODE_FADE
                    show()
                }
            }
        }
        signInViewModel.clearError()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.uiState.collect {
                    signInStateHandler(it)
                }
            }
        }
    }

    private fun navigateToCatalog() {
        parentFragmentManager.commit {
            replace<CatalogFragment>(R.id.main_activity_container)
            addToBackStack(null)
        }
    }
}