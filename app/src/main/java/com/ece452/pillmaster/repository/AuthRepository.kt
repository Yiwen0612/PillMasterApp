package com.ece452.pillmaster.repository

import com.ece452.pillmaster.model.User
import com.ece452.pillmaster.di.FirebaseModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface IAuthRepository {
    val currentUser: FirebaseUser?
    val currentUserFlow: Flow<User>
    fun hasUser(): Boolean
    fun getUserId(): String
    suspend fun login(
        email: String,
        password: String,
        onComplete: (Boolean)->Unit
    )
    suspend fun signup(
       email: String,
       password: String,
       onComplete: (Boolean)->Unit
    )
    fun signout()
}

// Used Resources: https://www.youtube.com/watch?v=n7tUmLP6pdo
class AuthRepository
@Inject constructor(private val auth: FirebaseAuth) : IAuthRepository{

    override val currentUser: FirebaseUser? = auth.currentUser

    override val currentUserFlow: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override fun hasUser(): Boolean = auth.currentUser != null

    override fun getUserId(): String = auth.currentUser?.uid.orEmpty()

    override suspend fun login(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            // Perform the login operation in a background thread
            auth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete.invoke(true)
                    } else {
                        onComplete.invoke(false)
                    }
                }.await()
        }
    }

    override suspend fun signup(
       email: String,
       password: String,
       onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            // Perform the signup operation in a background thread
            auth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete.invoke(true)
                    } else {
                        onComplete.invoke(false)
                    }
                }.await()
        }
    }

    override fun signout() {
        auth.signOut()
    }
}