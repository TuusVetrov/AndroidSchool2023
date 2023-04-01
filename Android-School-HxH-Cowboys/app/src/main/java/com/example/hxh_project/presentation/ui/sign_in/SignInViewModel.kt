package com.example.hxh_project.presentation.ui.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hxh_project.domain.model.response.AuthResponse
import com.example.hxh_project.data.repository.UserRepository
import com.example.hxh_project.domain.use_case.SignInUseCase
import com.example.hxh_project.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<State<AuthResponse>> = MutableStateFlow(State.idle())
    val uiState: StateFlow<State<AuthResponse>> = _uiState

    fun login(email: String, password: String) {
        _uiState.update {
            State.loading()
        }
        viewModelScope.launch {
            try {
                val data = signInUseCase.invoke(email, password)

                data.getOrThrow().apply {
                    _uiState.update {
                        State.success(this)
                    }
                }
            }catch (e: java.lang.Exception){
                _uiState.update {
                    State.error("")
                }
            }
        }
    }
}