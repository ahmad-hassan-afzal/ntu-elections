package com.example.melection

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        val aniSlideRight = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_right)
        val aniSlideUp = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)

        val btnManageCandidates = findViewById<Button>(R.id.btn_manage_candidates)
        val btnManageResults = findViewById<Button>(R.id.btn_manage_results)

        findViewById<ImageView>(R.id.img_icon_admin_panel).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.txt_welcome_admin_panel2).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.txt_welcome_admin_panel).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.txt_sub_heading_admin_panel).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.txt_details_admin_panel).startAnimation(aniSlideRight)

        btnManageCandidates.startAnimation(aniSlideUp)
        btnManageResults.startAnimation(aniSlideUp)
        findViewById<ImageView>(R.id.bg_admin_panel).startAnimation(aniSlideUp)

        btnManageCandidates.setOnClickListener{
                startActivity(Intent(applicationContext, ManageCandidates::class.java))
            }
        btnManageResults.setOnClickListener{
                startActivity(Intent(applicationContext, ManageResults::class.java))
            }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_item_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intents = Intent(applicationContext, Splash::class.java)
                intents.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intents)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}