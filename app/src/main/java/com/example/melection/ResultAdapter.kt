package com.example.melection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultAdapter (private val names: ArrayList<String>, private val votes: ArrayList<Int>) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById<TextView>(R.id.tv_result_candidate_name)
        val txtVotes: TextView = view.findViewById<TextView>(R.id.tv_result_votes)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_result, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtName.text = names[position]
        viewHolder.txtVotes.text = votes[position].toString()
    }

    override fun getItemCount() = names.size
}
