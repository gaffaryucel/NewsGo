package com.gaffaryucel.newsgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaffaryucel.newsgo.model.SavedNewsModel
import com.gaffaryucel.newsgo.model.UserProfile
import com.gaffaryucel.newsgo.repo.FirebaseRepoInterFace
import com.gaffaryucel.newsgo.repo.NewsDaoRepoInterface
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val repo : NewsDaoRepoInterface,
    private val firebaseRepo : FirebaseRepoInterFace,
    private val auth : FirebaseAuth
) : ViewModel() {

    val savedNews = repo.getAllSavedNews()

    private var _userData = MutableLiveData<UserProfile>()
    val userData : LiveData<UserProfile>
        get() = _userData

    init {
        getUserInfo()
    }

    fun saveNews(savedNewsModel : SavedNewsModel) = viewModelScope.launch{
        try {
            CoroutineScope(Dispatchers.IO).launch{
                repo.insertNews(savedNewsModel)
            }
        }catch (e : Exception){
            println("error321"+ e.localizedMessage)
        }
    }
    fun deleteNews(savedNewsModel : SavedNewsModel) = viewModelScope.launch{
        try {
            CoroutineScope(Dispatchers.IO).launch{
                delay(2000)
                repo.deleteNews(savedNewsModel)
            }
        }catch (e : Exception){
            println("error321"+ e.localizedMessage)
        }
    }
    private fun getUserInfo() = viewModelScope.launch{
        firebaseRepo.getUserDataByDocumentId(auth.currentUser?.uid ?:"")
            .addOnSuccessListener { documentSnapshot ->
                print("snepshot : "+documentSnapshot)
                if (documentSnapshot.exists()) {
                    val user: UserProfile? = documentSnapshot.toObject(UserProfile::class.java)
                    print("user : "+user)
                    // Belge    varsa kullanıcı verilerini kullanarak istediğiniz işlemleri gerçekleştirin
                    if (user != null) {
                        _userData.value = user!!
                    }
                } else {
                    // Belge bulunamadı
                    println("Belge bulunamadı.")
                }
        }.addOnFailureListener { e ->
            // Hata durumunda burada işlem yapabilirsiniz
        }
    }
}