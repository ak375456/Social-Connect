package com.example.socialconnect.authentication.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    // StateFlow for authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _userData = MutableStateFlow<Map<String, Any>?>(null)
    val userData = _userData.asStateFlow()

    fun getUserData(
//        uid: String
    ) {
        viewModelScope.launch {
            Log.d("FIREBASE",auth.currentUser?.uid.toString())
            _authState.value = AuthState.Loading
            db.collection("users")
                .whereEqualTo("id", auth.currentUser?.uid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val userData = querySnapshot.documents[0].data
                        if (userData != null) {
                            _userData.value = userData // Update userData state
                            _authState.value = AuthState.Success
                        } else {
                            _authState.value = AuthState.Error("User data is null")
                        }
                    } else {
                        _authState.value = AuthState.Error("No user found with the given ID")
                    }
                }
                .addOnFailureListener { e ->
                    _authState.value = AuthState.Error(e.message ?: "Failed to fetch user data")
                }
        }
    }


    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Success
                    } else {
                        _authState.value =
                            AuthState.Error(task.exception?.message ?: "Unknown error")
                    }
                }
        }
    }

    //    this function will create car object in db with uid auto-generated
    fun createUser(
        firstName: String,
        lastName: String,
        number: String,
        profilePictureLink: String,
        bio: String
    ) {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid == null) {
            _authState.value = AuthState.Error("No authenticated user found")
            return
        }

        val user = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "id" to currentUserUid, // Use Firebase Auth UID
            "number" to number,
            "profilePictureLink" to profilePictureLink,
            "bio" to bio
        )

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            db.collection("users")
                .document(currentUserUid) // Set document ID to Firebase Auth UID
                .set(user)
                .addOnSuccessListener {
                    _authState.value = AuthState.Success
                }
                .addOnFailureListener { e ->
                    _authState.value = AuthState.Error(e.message ?: "Unknown error")
                }
        }
    }


    fun signInUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Success
                    } else {
                        _authState.value =
                            AuthState.Error(task.exception?.message ?: "Login failed")
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            auth.signOut()
            _authState.value = AuthState.Success
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }


    val isPasswordVisible = mutableStateOf(false)
    fun togglePasswordVisibility() {
        isPasswordVisible.value = !isPasswordVisible.value
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}