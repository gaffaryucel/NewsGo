package com.gaffaryucel.newsgo.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaffaryucel.newsgo.model.UserProfile
import com.gaffaryucel.newsgo.repo.FirebaseRepoInterFace
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository : FirebaseRepoInterFace
) : ViewModel() {

    var _authState = MutableLiveData<Boolean>()
    val authState : LiveData<Boolean>
        get() = _authState

    fun signUp(userName : String,email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result: Task<AuthResult> = repository.signUp(email, password)
                result.addOnCompleteListener { task ->
                    _authState.value = task.isSuccessful
                    createUserModelAndSaveUser(task.result.user?.uid.toString(),userName,email)
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    private fun createUserModelAndSaveUser(id : String,username : String,email: String) {
        val user = UserProfile(
            id,
            username,
            email,
            email.substringBefore("@"),
            ""
        )
        saveUserToFirestore(user)
    }

    private fun saveUserToFirestore(user : UserProfile){
        repository.addUserToFirestore(user)
    }
}