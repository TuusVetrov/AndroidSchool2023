package com.example.hxh_project.presentation.ui.sign_in
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hxh_project.R
import com.example.hxh_project.databinding.FragmentSignInBinding
import com.example.hxh_project.presentation.ui.utils.SnackbarListener
import com.example.hxh_project.presentation.ui.catalog.CatalogFragment
import com.example.hxh_project.utils.extensions.navigateTo
import com.example.hxh_project.utils.extensions.setError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    private var snackbarListener: SnackbarListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        snackbarListener = requireActivity() as? SnackbarListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun setListeners() {
        binding.textLogin.addTextChangedListener {
            viewModel.setEmail(it.toString())
        }

        binding.textPassword.addTextChangedListener {
            viewModel.setPassword(it.toString())
        }

        binding.buttonSignIn.setOnClickListener {
            viewModel.login()
        }

        binding.textPassword.setOnEditorActionListener {  _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ){
                viewModel.login()
                true
            } else {
                false
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    signInStateHandler(it)
                }
            }
        }
    }

    private fun setInputLayoutErrors(state: SignInState) {
        binding.layoutLogin.setError(state.isValidEmail == false) {
            getString(R.string.error_message_email_invalid)
        }
        binding.layoutPassword.setError(state.isValidPassword == false) {
            getString(R.string.error_message_password_invalid)
        }
    }

    private fun signInStateHandler(state: SignInState) {
        if (state.isLoggedIn) {
            navigateTo<CatalogFragment>(false)
        }

        setInputLayoutErrors(state)

        val errorMessage = state.error
        if (errorMessage != null) {
            snackbarListener?.showError(errorMessage)
            viewModel.clearError()
        }
    }
}