package com.example.hxh_project.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.domain.model.response.AuthResponse
import com.example.hxh_project.domain.model.response.GetUserResponse
import com.example.hxh_project.domain.model.Profile
import com.example.hxh_project.domain.use_case.ProfileUseCase
import com.example.hxh_project.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileData(
    val profile: GetUserResponse,
    val version: String
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<State<ProfileData>> = MutableStateFlow(State.init())
    val uiState: StateFlow<State<ProfileData>> = _uiState

    fun getData() {
        _uiState.update {
            State.loading()
        }
        viewModelScope.launch {
            try {
                val userdata = profileUseCase.getProfile()
                val version = profileUseCase.getAppVersion()
                State.success(
                    ProfileData(userdata.getOrThrow(), version)
                )
                userdata.getOrThrow().apply {
                    _uiState.update {
                        State.success(
                            ProfileData(this, version)
                        )
                    }
                }
            }catch (e: java.lang.Exception){
                _uiState.update {
                    State.error("")
                }
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            profileUseCase.logOut()
        }
    }
}