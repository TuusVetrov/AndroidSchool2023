package com.example.hxh_project.presentation.ui.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.data.token_manager.TokenManager
import com.example.hxh_project.domain.use_case.SignInUseCase
import com.example.hxh_project.utils.extensions.updateStateProperty
import com.example.hxh_project.utils.validators.AuthValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState: MutableStateFlow<SignInState> = MutableStateFlow(SignInState.initState)
    val uiState: StateFlow<SignInState> = _uiState

    init {
        checkUserLoggedIn()
    }

    fun setEmail(email: String) =
        updateStateProperty(_uiState) { copy( email = email ) }

    fun setPassword(password: String) =
        updateStateProperty(_uiState) { copy( password = password ) }

    fun clearError() =
        updateStateProperty(_uiState) { copy( error = null ) }

    fun login() {
        if (!validateCredentials())
            return

        viewModelScope.launch {
            updateStateProperty(_uiState) { copy( isLoading = true ) }

            val response = signInUseCase.invoke(
                uiState.value.email,
                uiState.value.password
            )

            response.onSuccess { authCredential ->
                tokenManager.saveToken(authCredential.data.accessToken)
                updateStateProperty(_uiState) {
                    copy(
                        isLoading = false,
                        isLoggedIn = true,
                        error = null,
                    )
                }
            }.onFailure { message ->
                updateStateProperty(_uiState) {
                    copy(
                        isLoading = false,
                        isLoggedIn = false,
                        error = message,
                    )
                }
            }
        }
    }

    private fun validateCredentials(): Boolean {
        val isValidEmail = AuthValidator.isEmailValid(uiState.value.email)
        val isValidPassword = AuthValidator.isPasswordValid(uiState.value.password)

        updateStateProperty(_uiState) {
            copy(
                isValidEmail = isValidEmail,
                isValidPassword = isValidPassword,
            )
        }

        return isValidEmail && isValidPassword
    }

    private fun checkUserLoggedIn() =
        updateStateProperty(_uiState) { copy( isLoggedIn = tokenManager.getToken() != null ) }
}