package com.example.hxh_project.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.data.token_manager.TokenManager
import com.example.hxh_project.data.model.request.ChangeUserProfileRequest
import com.example.hxh_project.domain.use_case.ProfileUseCase
import com.example.hxh_project.utils.extensions.updateStateProperty
import com.example.hxh_project.utils.validators.SettingsValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val tokenManager: TokenManager,
): ViewModel() {
    private val _uiState: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState.initState)
    val uiState: StateFlow<SettingsState> = _uiState

    init {
        checkUserLoggedIn()
    }

    fun setImage(uri: String) =
        updateStateProperty(_uiState) { copy( newImageUrl = uri ) }

    fun setName(name: String) =
        updateStateProperty(_uiState) { copy( name = name ) }

    fun setSurname(surname: String) =
        updateStateProperty(_uiState) { copy( surname = surname ) }

    fun setJob(job: String) =
        updateStateProperty(_uiState) { copy( job = job ) }

    fun clearError() =
        updateStateProperty(_uiState) { copy( error = null ) }

    fun getUserProfile() {
        viewModelScope.launch {
            val response = profileUseCase.getProfile()
            response.onSuccess { user ->
                updateStateProperty(_uiState) {
                    copy(
                        name = user.data.profile.name,
                        surname = user.data.profile.surname,
                        job = user.data.profile.occupation,
                        error = null,
                    )
                }
                getUserImage(user.data.profile.avatarId)
            }.onFailure { message ->
                updateStateProperty(_uiState) { copy( error = message ) }
            }
        }
    }

    private fun getUserImage(id: String) {
        viewModelScope.launch {
            val response = profileUseCase.getUserPhoto(id)
            response.onSuccess { image ->
                updateStateProperty(_uiState) {
                    copy(
                        userImage = image,
                        error = null,
                    )
                }
            }.onFailure { message ->
                updateStateProperty(_uiState) { copy( error = message ) }
            }
        }
    }

    fun changeCredentials() {
        if (!validateCredentials())
            return

        viewModelScope.launch {
            updateStateProperty(_uiState) { copy( isLoading = true ) }

            if (uiState.value.newImageUrl.isNotEmpty()) {
                val response =  profileUseCase.changeUserPhoto(uiState.value.newImageUrl)
                response.onSuccess {
                    updateStateProperty(_uiState) { copy( error = null ) }
                    changeUserData()
                }.onFailure { message ->
                    updateStateProperty(_uiState) {
                        copy(
                            isLoading = false,
                            error = message,
                        )
                    }
                }
            } else {
                changeUserData()
            }
        }
    }

    private fun changeUserData() {
        val requestData = listOf(
            ChangeUserProfileRequest(
                path = "/name",
                value = uiState.value.name
            ),
            ChangeUserProfileRequest(
                path = "/surname",
                value = uiState.value.surname
            ),
            ChangeUserProfileRequest(
                path = "/occupation",
                value = uiState.value.job
            ),
        )

        viewModelScope.launch {
            val response = profileUseCase.changeUserProfile(requestData)
            response.onSuccess {
                updateStateProperty(_uiState) {
                    copy(
                        error = null,
                        isLoading = false,
                        isSuccessfullyModified = true
                    )
                }
            }.onFailure { message ->
                updateStateProperty(_uiState) {
                    copy(
                        error = message,
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun checkUserLoggedIn() =
        updateStateProperty(_uiState) { copy( isUserLoggedIn = tokenManager.getToken() != null ) }

    private fun validateCredentials(): Boolean {
        val isValidName = SettingsValidator.isNameOrSurnameValid(uiState.value.name)
        val isValidSurname = SettingsValidator.isNameOrSurnameValid(uiState.value.surname)
        val isValidJob = SettingsValidator.isJobValid(uiState.value.job)

        updateStateProperty(_uiState) {
            copy(
                isValidName = isValidName,
                isValidSurname = isValidSurname,
                isValidJob = isValidJob
            )
        }

        return isValidName && isValidSurname && isValidJob
    }
}