package com.example.melection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class CandidateAdapter(private val names: ArrayList<String>, private val regNOs: ArrayList<String>, private val votes: ArrayList<Int>) :
    RecyclerView.Adapter<CandidateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tv_name)
        val btnVote: Button = view.findViewById(R.id.rv_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_candidate, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = names[position]

        val database = FirebaseDatabase.getInstance()

        viewHolder.btnVote.setOnClickListener{
            database.getReference("Candidates/${Login.section}/${regNOs[position]}/Vote")
                .setValue(votes[position]+1)
            database.getReference("RegisteredVoters/${Login.section}/${Login.regNo}/VoteCasted")
                .setValue(1)
        }
    }
    override fun getItemCount() = names.size

}