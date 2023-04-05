package com.example.ecommerceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    lateinit var inputEmail: String
    lateinit var inputPwd: String
    lateinit var loginBtn: Button
    lateinit var regBtn: Button

    lateinit var mAuth: FirebaseAuth
    lateinit var mUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        // mUser = mAuth.currentUser!!
        loginBtn = findViewById(R.id.loginBtn)
        regBtn = findViewById(R.id.regBtn)

        loginBtn.setOnClickListener {
            inputEmail = findViewById<EditText?>(R.id.email).text.toString()
            inputPwd = findViewById<EditText?>(R.id.etPwd).text.toString()
            mAuth.signInWithEmailAndPassword(inputEmail, inputPwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this,"Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        regBtn.setOnClickListener {
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }
    }
}