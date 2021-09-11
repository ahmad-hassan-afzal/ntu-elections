package com.example.melection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        val auth = FirebaseAuth.getInstance()

        val edtEmail = findViewById<EditText>(R.id.edt_email_for_login_admin)
        val edtPassword = findViewById<EditText>(R.id.edt_password_for_login_admin)

        val txtError = findViewById<TextView>(R.id.txt_error_for_login_admin)

        val btnLogin = findViewById<Button>(R.id.btn_login_admin)

        val aniSlideLeft = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_left)
        val aniSlideRight = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_right)

        findViewById<TextView>(R.id.txt_welcome_login_admin).startAnimation(aniSlideLeft)
        findViewById<TextView>(R.id.txt_details_login_admin).startAnimation(aniSlideLeft)

        edtEmail.startAnimation(aniSlideRight)
        edtPassword.startAnimation(aniSlideRight)
        btnLogin.startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.lbl1_login_admin).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.lbl2_login_admin).startAnimation(aniSlideRight)

        findViewById<ImageView>(R.id.image_view_bg_login_admin).startAnimation(aniSlideRight)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                when {
                    email != "admin@ntu.edu.pk" -> {
                        txtError.visibility = View.VISIBLE
                        txtError.text = "Invalid Email"
                    }
                    password.length < 8 -> {
                        txtError.visibility = View.VISIBLE
                        txtError.text = "Password too short"
                    }
                    else -> {
                        Toast.makeText(applicationContext,"Fetching Data..\n Please Wait :)", Toast.LENGTH_SHORT).show()

                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    startActivity(Intent(applicationContext, AdminPanel::class.java))
                                } else {
                                    Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            } else {
                txtError.visibility = View.VISIBLE
                txtError.text = "Please Enter Email & Password First"
            }
        }

    }
}