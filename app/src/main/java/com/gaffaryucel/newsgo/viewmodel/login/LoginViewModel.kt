package com.gaffaryucel.newsgo.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaffaryucel.newsgo.repo.FirebaseRepoInterFace
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository : FirebaseRepoInterFace
) : ViewModel() {
    var _authState = MutableLiveData<Boolean>()
    val authState : LiveData<Boolean>
        get() = _authState

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result: Task<AuthResult> = repository.signIn(email, password)
                result.addOnCompleteListener { task ->
                    _authState.value = task.isSuccessful
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

}