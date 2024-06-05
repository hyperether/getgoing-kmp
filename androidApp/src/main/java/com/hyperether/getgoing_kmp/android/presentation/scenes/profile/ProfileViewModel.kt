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
            if (currentUser != null) {
                if (currentUser.gender != gender) {
                    val updatedUser = currentUser.copy(gender = gender)
                    repository.updateUser(updatedUser)
                }
            } else {
                val newUser = User(gender = gender)
                val userId = repository.insertUser(newUser)
                fetchUserById(userId)
            }
        }
    }

    fun updateUserAge(age: Int) {
        viewModelScope.launch {
            val currentUser = _user.value
            if (currentUser != null) {
                if (currentUser.age != age) {
                    val updatedUser = currentUser.copy(age = age)
                    repository.updateUser(updatedUser)
                }
            } else {
                val newUser = User(age = age)
                val userId = repository.insertUser(newUser)
                fetchUserById(userId)
            }
        }
    }

    fun updateUserHeight(height: Int) {
        viewModelScope.launch {
            val currentUser = _user.value
            if (currentUser != null) {
                if (currentUser.height != height) {
                    val updatedUser = currentUser.copy(height = height)
                    repository.updateUser(updatedUser)
                }
            } else {
                val newUser = User(height = height)
                val userId = repository.insertUser(newUser)
                fetchUserById(userId)
            }
        }
    }

    fun updateUserWeight(weight: Int) {
        viewModelScope.launch {
            val currentUser = _user.value
            if (currentUser != null) {
                if (currentUser.weight != weight) {
                    val updatedUser = currentUser.copy(weight = weight)
                    repository.updateUser(updatedUser)
                }
            } else {
                val newUser = User(weight = weight)
                val userId = repository.insertUser(newUser)
                fetchUserById(userId)
            }
        }
    }
}