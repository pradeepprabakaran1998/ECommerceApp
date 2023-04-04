package com.example.ecommerceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Register : AppCompatActivity() {
    lateinit var inputUsername:String
    lateinit var inputEmail:String
    lateinit var inputPwd:String
    lateinit var inputConfirmPwd:String
    lateinit var regBtn:Button

    lateinit var mAuth:FirebaseAuth
    lateinit var mUser:FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        // mUser = mAuth.currentUser!!
        regBtn = findViewById(R.id.regBtn)

        regBtn.setOnClickListener{
            inputUsername = findViewById<EditText?>(R.id.userName).text.toString()
            inputEmail = findViewById<EditText?>(R.id.email).text.toString()
            inputPwd = findViewById<EditText?>(R.id.password).text.toString()
            inputConfirmPwd = findViewById<EditText?>(R.id.confirmPwd).text.toString()
            mAuth.createUserWithEmailAndPassword(inputEmail,inputPwd).addOnCompleteListener(this){ task->
                if(task.isSuccessful){
                    Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,Login::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this,"Registration Failed",Toast.LENGTH_SHORT).show()
                }
            }
                .addOnFailureListener { exception ->
                    // Handle the failure here, e.g. log the error or show an error message to the user
                    Log.e("RegisterActivity", "Registration failed", exception)
                    Toast.makeText(this, "Registration Failed: " + exception.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

}