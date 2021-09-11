package com.example.melection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ManageCandidateAdapter (private val candidate: ArrayList<Candidate>) :
    RecyclerView.Adapter<ManageCandidateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtCandidateName: TextView = view.findViewById<TextView>(R.id.tv_candidate_name)
        val txtCandidateSectionReg: TextView = view.findViewById<TextView>(R.id.tv_candidate_section_reg)
        val btnDelete: Button = view.findViewById(R.id.btn_delete_candidate)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_manage_candidate, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtCandidateName.text = candidate[position].name
        viewHolder.txtCandidateSectionReg.text = "${candidate[position].section}: ${candidate[position].regNo}"

        viewHolder.btnDelete.setOnClickListener{
            FirebaseDatabase.getInstance()
                .getReference("/Candidates/${candidate[position].section}/${candidate[position].regNo}")
                .setValue(null)
        }
    }
    override fun getItemCount() = candidate.size

}