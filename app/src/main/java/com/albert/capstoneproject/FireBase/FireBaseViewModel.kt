package com.albert.capstoneproject.FireBase

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class FireBaseViewModel : ViewModel() {

    val TAG = "FirebaseViewModel" // Tag for logging

    private val firebaseAuth =
        FirebaseAuth.getInstance() // FirebaseAuth instance for authentication

    // LiveData to observe the current user
    private var _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    // Function to sign in a user with email and password
    fun login(email: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i(TAG, "Login is Successful: $_currentUser")
                _currentUser.value = it.result.user
            } else {
                Log.e(TAG, "Login failed: ${it.exception}")
            }
        }
    }

    // Function to sign up a new user with email and password
    fun signup(email: String, pass: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e(ContentValues.TAG, "Signup isSuccessful: ${it}")
                login(email, pass) // Automatically sign in the user after successful signup
            } else {
                Log.e(ContentValues.TAG, "Signup failed: ${it.exception}")
            }
        }
    }

    // Function to log out the current user
    fun logOut() {
        firebaseAuth.signOut()
        _currentUser.value =
            firebaseAuth.currentUser // Update LiveData with the current user after sign out
    }
}