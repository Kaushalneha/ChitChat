package com

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewMode : ViewModel() {
    private val _verificationID = MutableStateFlow<String?>(null)
    private val _sentOTP = MutableStateFlow<Boolean>(false)
    val sentOTP = _sentOTP

    private val _isSignedIn = MutableStateFlow<Boolean?>(null)
    val isSignedIn = _isSignedIn

    fun sendOTP(phoneNumber: String, activity: Activity) {
        _verificationID.value = null
        _sentOTP.value = false
        
        Log.d("AuthDebug", "Sending OTP to: +91$phoneNumber")

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("AuthDebug", "Verification Completed Automatically")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("AuthDebug", "OTP SEND FAILED ERROR: ${e.message}")
                _sentOTP.value = false
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("AuthDebug", "OTP Sent Successfully. Verification ID: $verificationId")
                _verificationID.value = verificationId
                _sentOTP.value = true
            }
        }
        
        val options = PhoneAuthOptions.newBuilder(Utils.getFirebaseAuthInstance())
            .setPhoneNumber("+91$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(otp: String, activity: Activity) {
        val verificationId = _verificationID.value
        
        if (verificationId != null) {
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            Utils.getFirebaseAuthInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.d("AuthDebug", "Sign In Success!")
                        _isSignedIn.value = true
                    } else {
                        Log.e("AuthDebug", "Sign In Failed: ${task.exception?.message}")
                        _isSignedIn.value = false
                    }
                }
        } else {
            Log.e("AuthDebug", "ERROR: Verification ID is NULL")
            _isSignedIn.value = false
        }
    }

    fun resetIsSignedIn() {
        _isSignedIn.value = null
    }
}