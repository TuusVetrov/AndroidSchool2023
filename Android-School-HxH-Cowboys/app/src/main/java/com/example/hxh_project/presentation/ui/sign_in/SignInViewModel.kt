package com.example.hxh_project.presentation.ui.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.core.token_manager.TokenManager
import com.example.hxh_project.domain.model.response.AuthResponse
import com.example.hxh_project.data.repository.UserRepository
import com.example.hxh_project.domain.use_case.SignInUseCase
import com.example.hxh_project.utils.AuthValidator
import com.example.hxh_project.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState: MutableStateFlow<SignInState> = MutableStateFlow(SignInState.initState)
    val uiState: StateFlow<SignInState> = _uiState

    fun setEmail(email: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    email = email,
                )
            }
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    password = password,
                )
            }
        }
    }

    fun clearError() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    error = null,
                )
            }
        }
    }

    fun login() {
        //FIXME: uncomment when on the backend the profile will be validated
        //if (!validateCredentials())
        //    return

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            val response = signInUseCase.invoke(
                uiState.value.email,
                uiState.value.password
            )

            response.onSuccess { authCredential ->
                tokenManager.saveToken(authCredential.data.accessToken)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        error = null,
                    )
                }
            }.onFailure { message ->
                _uiState.update { currentState ->
                    currentState.copy(
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

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isValidEmail = isValidEmail,
                    isValidPassword = isValidPassword,
                )
            }
        }

        return isValidEmail && isValidPassword
    }
}