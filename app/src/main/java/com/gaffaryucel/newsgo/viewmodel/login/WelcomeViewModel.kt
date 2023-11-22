package com.gaffaryucel.newsgo.viewmodel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaffaryucel.newsgo.repo.FirebaseRepoImpl
import com.gaffaryucel.newsgo.repo.FirebaseRepoInterFace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val repo : FirebaseRepoInterFace
): ViewModel() {
    private var _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser : LiveData<FirebaseUser>
        get() = _currentUser

    init {
        _currentUser.value = repo.getCurrentUser()
    }
}