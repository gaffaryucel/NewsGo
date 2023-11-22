package com.gaffaryucel.newsgo.repo

import com.gaffaryucel.newsgo.model.UserProfile
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

interface FirebaseRepoInterFace {
    fun signIn(email: String, password: String): Task<AuthResult>
    fun signUp(email: String, password: String): Task<AuthResult>
    fun getCurrentUser() : FirebaseUser?
    fun signOut()
    fun addUserToFirestore(data: UserProfile)
    fun deleteUserFromFirestore(documentId: String)
    fun getUserDataByDocumentId(documentId: String): Task<DocumentSnapshot>
}