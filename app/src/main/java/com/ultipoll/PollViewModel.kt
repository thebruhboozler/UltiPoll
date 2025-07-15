package com.ultipoll

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class PollViewModel: ViewModel() {
    private val luaRunner = LuaRunner()

    val database =
        FirebaseDatabase.getInstance("https://ultipoll-default-rtdb.europe-west1.firebasedatabase.app")
    val ref = database.getReference("polls")


    public fun startLuaRunner(id: String , script: String){
        val pollRef = ref.child("poll_$id")

        pollRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                attachContinuousListener(pollRef, script)
            }
            override fun onCancelled(error: DatabaseError) { }
        })


    }
    private fun attachContinuousListener(
        pollRef: DatabaseReference,
        script: String
    ) {
        pollRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                handleSnapshot(snapshot, script)
            }
            override fun onCancelled(error: DatabaseError) {  }
        })
    }

    private fun handleSnapshot(snapshot: DataSnapshot, script: String) {
        val numVotes = snapshot.child("numOfVotesCast").getValue(Int::class.java) ?: 0
        val participantNum = snapshot.child("participants").getValue(Int::class.java) ?: 0
        if (numVotes != participantNum) return

        val ballotType = snapshot.child("type").getValue(String::class.java)
        val votesMap: Map<*, *> = when (ballotType) {
            "Ranked" -> snapshot.child("votes")
                .getValue(object : GenericTypeIndicator<List<List< String>>>() {})
                ?.withIndex()?.associate { (i,v) -> i+1 to
                        v.withIndex().associate { (j,k) -> j+1 to k }
                }
                ?: emptyMap()
            "Single", "Multiple" -> snapshot.child("votes")
                .getValue(object : GenericTypeIndicator<Map<String, Int>>() {})
                ?: emptyMap()
            "Point" -> snapshot.child("votes")
                .getValue(object : GenericTypeIndicator<List<Map<String, Int>>>() {})
                ?.withIndex()?.associate { (i,v) -> (i+1) to v }
                ?: emptyMap()
            else -> return
        }
        val winner = luaRunner.runLua(script, votesMap)
    }
}