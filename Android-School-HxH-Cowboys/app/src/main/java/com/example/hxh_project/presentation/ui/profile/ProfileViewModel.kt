package com.example.hxh_project.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.data.token_manager.TokenManager
import com.example.hxh_project.domain.use_case.ProfileUseCase
import com.example.hxh_project.utils.extensions.updateStateProperty
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

    fun clearError() =
        updateStateProperty(_uiState) { copy( error = null ) }

    fun getUserProfile() {
        viewModelScope.launch {
            updateStateProperty(_uiState) { copy( isLoading = true ) }

            val response = profileUseCase.getProfile()
            val appVersionResponse = profileUseCase.getAppVersion()

            updateStateProperty(_uiState) { copy( appVersion = appVersionResponse ) }

            response.onSuccess { user ->
                updateStateProperty(_uiState) {
                    copy(
                        profile = user.data.profile,
                        appVersion = appVersionResponse,
                        error = null,
                    )
                }
                getUserImage()
            }.onFailure { message ->
                updateStateProperty(_uiState) {
                    copy(
                        isLoading = false,
                        error = message,
                    )
                }
            }
        }
    }

    private fun getUserImage() {
        val imageId = uiState.value.profile?.avatarId ?: return

        viewModelScope.launch {
            val response = profileUseCase.getUserPhoto(imageId)
            response.onSuccess { image ->
                updateStateProperty(_uiState) {
                    copy(
                        isLoading = false,
                        userImage = image,
                        error = null,
                    )
                }
            }.onFailure { message ->
                updateStateProperty(_uiState) {
                    copy(
                        isLoading = false,
                        error = message,
                    )
                }
            }
        }
    }

    fun logout() {
        tokenManager.deleteToken()
        updateStateProperty(_uiState) { copy( isUserLoggedIn = false ) }
    }

    private fun checkUserLoggedIn() =
        updateStateProperty(_uiState) { copy( isUserLoggedIn = tokenManager.getToken() != null ) }
}