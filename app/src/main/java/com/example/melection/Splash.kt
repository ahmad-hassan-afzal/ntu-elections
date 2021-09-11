package com.example.melection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.system.exitProcess

class Splash : AppCompatActivity() {

    private var backPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.supportActionBar?.hide()

        val img = findViewById<ImageView>(R.id.imageView)
        val logo = findViewById<ImageView>(R.id.imageView2)
        val imgNTU = findViewById<ImageView>(R.id.img_ntu_logo)

        val btnSignIn = findViewById<Button>(R.id.btn_sign_in)
        val btnSignUp = findViewById<Button>(R.id.btn_sign_up)
        val btnAdmin = findViewById<TextView>(R.id.btn_admin)

        val tvHeading = findViewById<TextView>(R.id.textView)
        val tvSubHeading = findViewById<TextView>(R.id.textView2)
        val tvNTU = findViewById<TextView>(R.id.textView4)

        val aniSlideUp = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
        val aniSlideDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)

        img.startAnimation(aniSlideUp)
        imgNTU.startAnimation(aniSlideUp)
        btnSignIn.startAnimation(aniSlideUp)
        btnSignUp.startAnimation(aniSlideUp)
        tvNTU.startAnimation(aniSlideUp)
        btnAdmin.startAnimation(aniSlideUp)

        tvHeading.startAnimation(aniSlideDown)
        tvSubHeading.startAnimation(aniSlideDown)
        logo.startAnimation(aniSlideDown)

        btnSignIn.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }
        btnSignUp.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
        }
        btnAdmin.setOnClickListener{
            startActivity(Intent(this, LoginAdmin::class.java))
        }
    }

    override fun onBackPressed() {
        if (!backPressed){
            Toast.makeText(applicationContext, "Press again to Exit the Application", Toast.LENGTH_LONG).show()
            backPressed = true
        } else {
            finishAffinity()
            exitProcess(0)
        }
    }
}