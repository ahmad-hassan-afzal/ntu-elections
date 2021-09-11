package com.example.melection

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity

import com.google.android.gms.tasks.OnCompleteListener




class Login : AppCompatActivity() {

    companion object {
        var batch: String = ""
        var section: String = ""
        var regNo: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()

        val spnrBatch = findViewById<Spinner>(R.id.spnr_batch_for_login)
        val spnrSection = findViewById<Spinner>(R.id.spnr_section_for_login)
        val edtRegNo = findViewById<EditText>(R.id.edt_reg_for_login)
        val edtPassword = findViewById<EditText>(R.id.edt_password_for_login)

        val txtError = findViewById<TextView>(R.id.txt_error_for_login)

        val btnLogin = findViewById<Button>(R.id.btn_login)

        val aniSlideLeft = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_left)
        val aniSlideRight = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_right)

        findViewById<TextView>(R.id.txt_welcome_login).startAnimation(aniSlideLeft)
        findViewById<TextView>(R.id.txt_details_login).startAnimation(aniSlideLeft)

        spnrBatch.startAnimation(aniSlideRight)
        spnrSection.startAnimation(aniSlideRight)
        edtRegNo.startAnimation(aniSlideRight)
        edtPassword.startAnimation(aniSlideRight)
        btnLogin.startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.lbl1_login).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.lbl2_login).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.txt_forgot_password).startAnimation(aniSlideRight)
        findViewById<TextView>(R.id.txt_register_for_login).startAnimation(aniSlideRight)

        findViewById<ImageView>(R.id.image_reg_no_for_login).startAnimation(aniSlideRight)
        findViewById<ImageView>(R.id.image_view_bg_login).startAnimation(aniSlideRight)


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

        btnLogin.setOnClickListener{
            if (edtRegNo.text.toString().isNotEmpty() && edtPassword.text.toString().isNotEmpty()){
                if (edtPassword.text.toString().length < 8){
                    txtError.visibility = View.VISIBLE
                    txtError.text = "Password too short"
                } else {
                    Toast.makeText(applicationContext, "Fetching Data..\n Please Wait :)", Toast.LENGTH_SHORT).show()
                    val regNo = "${spnrBatch.selectedItem}ntu${edtRegNo.text}"
                    val section = spnrSection.selectedItem.toString()
                    val email = "$regNo@student.ntu.edu.pk"
                    val password = edtPassword.text.toString()

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {

                                Login.regNo = regNo
                                Login.section = section
                                Login.batch = spnrBatch.selectedItem.toString()


                                val ref = database.getReference("RegisteredVoters/$section/$regNo")

                                ref.addValueEventListener(object: ValueEventListener{
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if ( dataSnapshot.child("VoteCasted").value.toString() == "1" ){
                                            startActivity(Intent(applicationContext, Results::class.java))
                                        } else {
                                            startActivity(Intent(applicationContext, SelectCandidate::class.java))
                                        }
                                    }
                                    override fun onCancelled(databaseError: DatabaseError) { }
                                })
                            } else {
                                Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } else {
                txtError.visibility = View.VISIBLE
                txtError.text = "Please Enter Registration Number & Password First"
            }

        }

    }

    fun startSignUpActivity(view: View) {
        startActivity(Intent(this, Register::class.java))
    }

    fun forgotPassword(view: View) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_forgot_password)

        val spinnerSection = dialog.findViewById<Spinner>(R.id.spnr_section_forgot)
        val spinnerBatch = dialog.findViewById<Spinner>(R.id.spnr_batch_forgot)
        val edtRegNo = dialog.findViewById<EditText>(R.id.edt_reg_forgot)

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

        dialog.findViewById<Button>(R.id.btn_send_forgot)
            .setOnClickListener{
                if (edtRegNo.text.isNotEmpty()){

                    val batch = spinnerBatch.selectedItem.toString()
                    val regNo = edtRegNo.text.toString()
                    val email = "${batch}ntu${regNo}@student.ntu.edu.pk"

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "Email Sent to:\n${email}\n{Also Check Junk Emails}", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                        }

                } else {
                    Toast.makeText(applicationContext, "Please Enter Registration Number", Toast.LENGTH_SHORT).show()
                }
            }
        dialog.show()
    }
}