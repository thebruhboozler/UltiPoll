package com.ultipoll

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ultipoll.databinding.FragmentIdInputBinding
import com.ultipoll.databinding.FragmentWaitingRoomBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WaitingRoomFragment : Fragment() {

    private lateinit var binding: FragmentWaitingRoomBinding
    val database =
        FirebaseDatabase.getInstance("https://ultipoll-default-rtdb.europe-west1.firebasedatabase.app")
    val ref = database.getReference("polls")
    private lateinit var pollId:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWaitingRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimer()
        pollId = arguments?.getString(ARG_ID).toString()
        Log.d("Waiting Room fragment" , pollId)
        ref.child("poll_$pollId").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.numOfVotes.text = snapshot.child("numOfVotesCast").getValue(Int::class.java).toString()
                binding.participantNum.text = snapshot.child("participants").getValue(Int::class.java).toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun startTimer() {
        var timePassed = 1

        fun secondsToClock(timePassed: Int):String{
            return "${Math.floor((timePassed/60).toDouble()).toInt()}:${(timePassed%60).toString().padStart(2,'0')}"
        }
        CoroutineScope(Dispatchers.Main).launch {
            while(true){
                delay(1000L)
                timePassed++
                binding.timer.text = secondsToClock(timePassed)
            }
        }
    }

    companion object {
        private const val ARG_ID = "arg_id"

        fun newInstance(id: String): WaitingRoomFragment {
            return WaitingRoomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                }
            }
        }
    }
    
}