package com.example.melection

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageResults : AppCompatActivity() {

    var csCandidates = ArrayList<String>()
    var seCandidates = ArrayList<String>()
    var aiCandidates = ArrayList<String>()
    var itCandidates = ArrayList<String>()

    var csVotes = ArrayList<Int>()
    var seVotes = ArrayList<Int>()
    var aiVotes = ArrayList<Int>()
    var itVotes = ArrayList<Int>()

    val database = FirebaseDatabase.getInstance()

    var csVoters = ArrayList<String>()
    var seVoters = ArrayList<String>()
    var aiVoters = ArrayList<String>()
    var itVoters = ArrayList<String>()

    var society = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_results)

        Toast.makeText(applicationContext, "Fetching Data\nPlease Wait.. ", Toast.LENGTH_SHORT).show()

        database.getReference("/Candidates")
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                csCandidates = ArrayList<String>()
                seCandidates = ArrayList<String>()
                aiCandidates = ArrayList<String>()
                itCandidates = ArrayList<String>()

                csVotes = ArrayList<Int>()
                seVotes = ArrayList<Int>()
                aiVotes = ArrayList<Int>()
                itVotes = ArrayList<Int>()

                for (i in dataSnapshot.children){
                    when (i.key) {
                        "CS" -> {
                            for (j in dataSnapshot.child("CS").children){
                                csCandidates.add(j.child("Name").value.toString())
                                csVotes.add(j.child("Vote").value.toString().toInt())
                            }
                        }
                        "SE" -> {
                            for (j in dataSnapshot.child("SE").children){
                                seCandidates.add(j.child("Name").value.toString())
                                seVotes.add(j.child("Vote").value.toString().toInt())
                            }
                        }
                        "IT" -> {
                            for (j in dataSnapshot.child("IT").children){
                                itCandidates.add(j.child("Name").value.toString())
                                itVotes.add(j.child("Vote").value.toString().toInt())
                            }
                        }
                        "AI" -> {
                            for (j in dataSnapshot.child("AI").children){
                                aiCandidates.add(j.child("Name").value.toString())
                                aiVotes.add(j.child("Vote").value.toString().toInt())
                            }
                        }
                    }
                }

                findViewById<RecyclerView>(R.id.recycler_result_se).apply {
                    layoutManager = LinearLayoutManager(applicationContext)
                    adapter = ResultAdapter(seCandidates, seVotes)
                }
                findViewById<RecyclerView>(R.id.recycler_result_cs).apply {
                    layoutManager = LinearLayoutManager(applicationContext)
                    adapter = ResultAdapter(csCandidates, csVotes)
                }
                findViewById<RecyclerView>(R.id.recycler_result_it).apply {
                    layoutManager = LinearLayoutManager(applicationContext)
                    adapter = ResultAdapter(itCandidates, itVotes)
                }
                findViewById<RecyclerView>(R.id.recycler_result_ai).apply {
                    layoutManager = LinearLayoutManager(applicationContext)
                    adapter = ResultAdapter(aiCandidates, aiVotes)
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })

        database.getReference("RegisteredVoters/")
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in dataSnapshot.children){
                    when (i.key) {
                        "SE" -> {
                            for (j in dataSnapshot.child("SE").children){
                                seVoters.add(j.key.toString())
                            }
                        }
                        "CS" -> {
                            for (j in dataSnapshot.child("CS").children){
                                csVoters.add(j.key.toString())
                            }
                        }
                        "IT" -> {
                            for (j in dataSnapshot.child("IT").children){
                                itVoters.add(j.key.toString())
                            }
                        }
                        "AI" -> {
                            for (j in dataSnapshot.child("AI").children){
                                aiVoters.add(j.key.toString())
                            }
                        }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })

    }

    private fun getIndexOfLargest(array: ArrayList<Int>): Int {
        var largest = 0
        for (i in array.indices) {
            if (array[i] > array[largest])
                largest = i
        }
        return largest
    }
    private fun setElectionState(casted: Int, voters: ArrayList<String>){
        for (i in voters){
            database.getReference("RegisteredVoters/$society/$i/VoteCasted").setValue(casted)
        }
    }

    fun publishResult(view: View) {
        when (view.id) {
            R.id.btn_publish_se -> {
                val firstIndex = getIndexOfLargest(seVotes)
                val ref = FirebaseDatabase.getInstance().getReference("Results/SE/")
                ref.child("Name").setValue(seCandidates[firstIndex])
                ref.child("Vote").setValue(seVotes[firstIndex])
                society = "SE"
                setElectionState(1, seVoters)
            } R.id.btn_publish_cs -> {
                val firstIndex = getIndexOfLargest(csVotes)
                val ref = FirebaseDatabase.getInstance().getReference("Results/CS/")
                ref.child("Name").setValue(csCandidates[firstIndex])
                ref.child("Vote").setValue(csVotes[firstIndex])
                society = "CS"
                setElectionState(1, csVoters)
            } R.id.btn_publish_it -> {
                val firstIndex = getIndexOfLargest(itVotes)
                val ref = FirebaseDatabase.getInstance().getReference("Results/IT/")
                ref.child("Name").setValue(itCandidates[firstIndex])
                ref.child("Vote").setValue(itVotes[firstIndex])
                society = "IT"
                setElectionState(1, itVoters)
            } R.id.btn_publish_ai -> {
                val firstIndex = getIndexOfLargest(aiVotes)
                val ref = FirebaseDatabase.getInstance().getReference("Results/AI/")
                ref.child("Name").setValue(aiCandidates[firstIndex])
                ref.child("Vote").setValue(aiVotes[firstIndex])
                society = "AI"
                setElectionState(1, aiVoters)
            }
        }
    }

    fun resetResults(view: View) {
        when (view.id) {
            R.id.btn_reset_se -> {
                society = "SE"
                setElectionState(0, seVoters)
            } R.id.btn_reset_cs -> {
                society = "CS"
                setElectionState(0, csVoters)
            } R.id.btn_reset_it -> {
                society = "IT"
                setElectionState(0, itVoters)
            } R.id.btn_reset_ai -> {
                society = "AI"
                setElectionState(0, aiVoters)
            }
        }
        database.getReference("Results/$society").setValue(null)

    }


}