package com.example.hxh_project.presentation.ui.sign_in
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
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

        viewModelObserver(view)
        
        binding.buttonSignIn.setOnClickListener {
            onLoginClicked()
        }

        binding.textPassword.setOnEditorActionListener {  _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ){
                onLoginClicked()
                true
            } else {
                false
            }
        }
    }

    private fun viewModelObserver(view: View) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.uiState.collect() {
                    when(it){
                        is State.Init -> {}
                        is State.Loading -> {
                            binding.buttonSignIn.isLoading = true
                        }
                        is State.Success -> {
                            onAuthSuccess()
                        }
                        is State.Error -> {
                            onAuthError(view)
                        }
                    }
                }
            }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        return with(binding) {
            when {
                !AuthValidator.isEmailValid(email) -> {
                    layoutLogin.error = getString(R.string.field_text_error)
                    false
                }
                !AuthValidator.isPasswordValid(password) -> {
                    layoutPassword.error = getString(R.string.field_text_error)
                    false
                }

                else -> true
            }
        }
    }

    private fun onLoginClicked() {
        val email = binding.textLogin.text.toString()
        val password = binding.textPassword.text.toString()

        if (validate(email, password)) {
            signInViewModel.login(email, password)
        }
    }

    private fun onAuthError(view: View) {
        val message = getString(R.string.error_loading_title) +
                "\n" + getString(R.string.error_loading_description)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
            show()
        }
    }

    private fun onAuthSuccess() {
        parentFragmentManager.commit {
            replace<CatalogFragment>(R.id.main_activity_container)
            addToBackStack(null)
        }
    }
}