package com.example.todo

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId

class MyFirebaseInstanceIdService {
    val TAG = "PushNotifService"
    lateinit var name: String

    fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "New msg: ${token}")
    }
}