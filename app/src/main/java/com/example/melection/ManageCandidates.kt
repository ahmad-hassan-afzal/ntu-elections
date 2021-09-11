package com.example.melection

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageCandidates : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_candidates)
        Toast.makeText(applicationContext, "Fetching Data\nPlease Wait..", Toast.LENGTH_SHORT).show()

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("/Candidates")
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_candidates)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var candidates = ArrayList<Candidate>()
                for (i in dataSnapshot.children) {
                    for (j in i.children){
                        candidates.add(Candidate(j.child("Name").value.toString(), j.key.toString(), i.key.toString()))
                    }
                }
                recyclerView.adapter = ManageCandidateAdapter(candidates)
            }
            override fun onCancelled(error: DatabaseError) { }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_add_candidate, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_item_add_candidate) {

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_new_candidate)

            val spinnerSection = dialog.findViewById<Spinner>(R.id.spnr_section_dialog)
            val spinnerBatch = dialog.findViewById<Spinner>(R.id.spnr_batch_dialog)
            val edtRegNo = dialog.findViewById<EditText>(R.id.edt_reg_for_dialog)
            val edtName = dialog.findViewById<EditText>(R.id.edt_name_for_dialog)

            ArrayAdapter.createFromResource(
                this,
                R.array.batch_list,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerBatch.adapter = adapter
            }
            ArrayAdapter.createFromResource(
                this,
                R.array.section_list,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSection.adapter = adapter
            }
            dialog.findViewById<Button>(R.id.btn_add_candidate_dialog)
                .setOnClickListener{
                    val batch = spinnerBatch.selectedItem.toString()
                    val section = spinnerSection.selectedItem.toString()
                    val name = edtName.text.toString()
                    val regNo = edtRegNo.text.toString()

                    if (name.isNotEmpty() && regNo.isNotEmpty()){
                        val db = FirebaseDatabase.getInstance()
                        db.getReference("Candidates/${section}/${batch}ntu${regNo}/Name").setValue(name)
                        db.getReference("Candidates/${section}/${batch}ntu${regNo}/Vote").setValue(0)
                        dialog.dismiss()
                        Toast.makeText(applicationContext, "Candidate Registered Successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Name and Registration Number are Required", Toast.LENGTH_SHORT).show()
                    }
                }
            dialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}