package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class Ktodos : AppCompatActivity() {
    val RC_SIGN_IN =10
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var firebaseAuthListener:FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuthListener =FirebaseAuth.AuthStateListener(){user->
            val loginedUser = firebaseAuth.currentUser
            if (loginedUser!=null){
            startActivity(Intent(this@Ktodos,MainActivity::class.java))
                Toast.makeText(this@Ktodos,"Welcome ${loginedUser.displayName}",Toast.LENGTH_LONG).show()
        }else{
                // Choose authentication providers
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.PhoneBuilder().build()
                )

// Create and launch sign-in intent

                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN)
            }

        }
        firebaseAuth.addAuthStateListener(firebaseAuthListener)


    }
}
