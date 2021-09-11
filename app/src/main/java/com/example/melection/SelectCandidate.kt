package com.example.melection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

class SelectCandidate : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_candidate)

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("/Candidates")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                findViewById<TextView>(R.id.txt_view_society).text = "Elections for: ${Login.section} Society"

                var names = ArrayList<String>()
                var votes = ArrayList<Int>()
                var regNOs = ArrayList<String>()

                for (i in dataSnapshot.child(Login.section).children) {
                    names.add(i.child("Name").value.toString())
                    votes.add(i.child("Vote").value.toString().toInt())
                    regNOs.add(i.key.toString())
                }

                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.adapter = CandidateAdapter(names, regNOs, votes)
            }
            override fun onCancelled(error: DatabaseError) { }
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