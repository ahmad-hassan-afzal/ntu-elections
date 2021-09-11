package com.example.melection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.system.exitProcess
import androidx.core.content.IntentCompat

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK


class Results : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("/Results/${Login.section}")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.value != null){ // condition working fine
                    findViewById<ImageView>(R.id.img_winner_icon).setImageResource(R.drawable.winner)
                    findViewById<TextView>(R.id.txt_batch_section).text = dataSnapshot.child("Vote").value.toString() + " votes - Society: " + dataSnapshot.key.toString()
                    findViewById<TextView>(R.id.text_view_name).text = dataSnapshot.child("Name").value.toString()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) { }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_logout, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_item_logout -> {
                auth.signOut()
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