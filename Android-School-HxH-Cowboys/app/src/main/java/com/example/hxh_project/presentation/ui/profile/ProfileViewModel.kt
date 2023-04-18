package com.example.hxh_project.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.core.token_manager.TokenManager
import com.example.hxh_project.domain.use_case.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val tokenManager: TokenManager,
): ViewModel() {
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.initState)
    val uiState: StateFlow<ProfileState> = _uiState

    init {
        checkUserLoggedIn()
    }

    fun getUserProfile() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            val response = profileUseCase.getProfile()
            val appVersionResponse = profileUseCase.getAppVersion()
            _uiState.update { currentState ->
                currentState.copy(
                    appVersion = appVersionResponse,
                )
            }
            response.onSuccess { user ->
                _uiState.update { currentState ->
                    currentState.copy(
                        profile = user.data.profile,
                        isLoading = false,
                        appVersion = appVersionResponse,
                        error = null,
                    )
                }
            }.onFailure { message ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = message,
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.saveToken(null)
            _uiState.update { currentState ->
                currentState.copy(
                    isUserLoggedIn = false,
                )
            }
        }
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isUserLoggedIn = tokenManager.getToken() != null,
                )
            }
        }
    }
}