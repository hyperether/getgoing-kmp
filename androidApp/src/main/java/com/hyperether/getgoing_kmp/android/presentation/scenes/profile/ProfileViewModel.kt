package com.hyperether.getgoing_kmp.android.presentation.scenes.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperether.getgoing_kmp.android.App
import com.hyperether.getgoing_kmp.model.User
import com.hyperether.getgoing_kmp.model.UserGender
import com.hyperether.getgoing_kmp.repository.GgRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: GgRepository = App.getRepository()
) : ViewModel() {

    private var _user = MutableStateFlow<User?>(null)
    val user
        get() = _user.asStateFlow()

    fun fetchUserById(userId: Long) {
        viewModelScope.launch {
            repository.getUser(userId).collect {
                _user.value = it
            }
        }
    }

    fun updateUserGender(gender: UserGender) {
        viewModelScope.launch {
            val currentUser = _user.value
            currentUser?.let {
                repository.updateUser(
                    it.copy(
                        gender = gender
                    )
                )
            }
        }
    }

    fun updateUserAge(age: Int) {
        viewModelScope.launch {
            val currentUser = _user.value
            currentUser?.let {
                repository.updateUser(
                    it.copy(
                        age = age
                    )
                )
            }
        }
    }

    fun updateUserHeight(height: Int) {
        viewModelScope.launch {
            val currentUser = _user.value
            currentUser?.let {
                repository.updateUser(
                    it.copy(
                        height = height
                    )
                )
            }
        }
    }

    fun updateUserWeight(weight: Int) {
        viewModelScope.launch {
            val currentUser = _user.value
            currentUser?.let {
                repository.updateUser(
                    it.copy(
                        weight = weight
                    )
                )
            }
        }
    }
}