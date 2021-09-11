package com.example.melection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val database = FirebaseDatabase.getInstance()
        var ref: DatabaseReference? = null

        val btnRegister = findViewById<Button>(R.id.btn_register)

        val spnrBatch = findViewById<Spinner>(R.id.spnr_batch_for_register)
        val spnrSection = findViewById<Spinner>(R.id.spnr_section_for_register)
        val edtRegNo = findViewById<EditText>(R.id.edt_reg_for_register)
        val edtName = findViewById<EditText>(R.id.edt_name_for_register)
        val edtPassword1 = findViewById<EditText>(R.id.edt_password_for_register)
        val edtPassword2 = findViewById<EditText>(R.id.edt_password_for_register2)



        val aniSlideLeft = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_left)
        val aniSlideRight = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_right)

        findViewById<TextView>(R.id.textView6).startAnimation(aniSlideLeft)
        findViewById<TextView>(R.id.textView9).startAnimation(aniSlideLeft)

        spnrBatch.startAnimation(aniSlideRight)
        spnrSection.startAnimation(aniSlideRight)
        edtRegNo.startAnimation(aniSlideRight)
        edtName.startAnimation(aniSlideRight)
        edtPassword1.startAnimation(aniSlideRight)
        edtPassword2.startAnimation(aniSlideRight)
        btnRegister.startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.textView10).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.textView11).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.textView12).startAnimation(aniSlideRight)

        findViewById<ImageView>(R.id.image_reg_no_for_register).startAnimation(aniSlideRight)
        findViewById<ImageView>(R.id.image_view_bg_register).startAnimation(aniSlideRight)


        val txtError = findViewById<TextView>(R.id.txt_error_register)

        ArrayAdapter.createFromResource(
            this,
            R.array.batch_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnrBatch.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.section_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spnrSection.adapter = adapter
        }

        btnRegister.setOnClickListener{

            if (edtRegNo.text.toString().isNotEmpty() && edtPassword1.text.toString().isNotEmpty()){
                when {
                    edtPassword1.text.toString().length < 8 -> {
                        txtError.visibility = View.VISIBLE
                        txtError.text = "Password must be of 8-characters"
                    }
                    edtPassword1.text.toString() != edtPassword2.text.toString() -> {
                        txtError.visibility = View.VISIBLE
                        txtError.text = "Passwords mismatch"
                    }
                    else -> {
                        val email = spnrBatch.selectedItem.toString() + "ntu" + edtRegNo.text.toString() + "@student.ntu.edu.pk"
                        val password = edtPassword1.text.toString()
                        val section = spnrSection.selectedItem.toString()
                        val registration = spnrBatch.selectedItem.toString() + "ntu" + edtRegNo.text.toString()


                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {

                                    ref = database.getReference("/RegisteredVoters/$section/$registration/")
                                    ref?.child("Name")?.setValue(edtName.text.toString())
                                    ref?.child("VoteCasted")?.setValue(0)

                                    startActivity(Intent(applicationContext, Login::class.java)

                                    )
                                } else {
                                    Toast.makeText(
                                        this, task.exception?.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            } else {
                txtError.visibility = View.VISIBLE
                txtError.text = "Please Enter Reg.No. & Password First"
            }

        }

    }

}