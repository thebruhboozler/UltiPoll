package com.ultipoll

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.MutableData
import com.google.firebase.database.ServerValue
import com.google.firebase.database.Transaction
import com.ultipoll.adapters.MultipleChoiceAdapter
import com.ultipoll.adapters.PointChoiceAdapter
import com.ultipoll.adapters.RankedChoiceAdapter
import com.ultipoll.adapters.SingeChoiceAdapter
import com.ultipoll.databinding.FragmentBoothBinding
import com.ultipoll.dataclasses.OptionCheckbox
import com.ultipoll.dataclasses.OptionPoint
import com.ultipoll.dataclasses.OptionRanked
import java.util.Collections

class BoothFragment : Fragment() {

    val database = FirebaseDatabase.getInstance("https://ultipoll-default-rtdb.europe-west1.firebasedatabase.app")
    val ref = database.getReference("polls")
    private lateinit var binding: FragmentBoothBinding
    private lateinit var pollId:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pollId = requireArguments().getString(ARG_ID).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoothBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref.child("poll_$pollId").get().addOnSuccessListener { snapshot->
            val type = snapshot.child("type").getValue(String::class.java).toString()
            val title = snapshot.child("title").getValue(String::class.java)
            binding.Title.text = title

            val optionsType = object : GenericTypeIndicator<List<String>>() {}
            val options = snapshot.child("options").getValue(optionsType) as List<String>

            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            when(type){
                "Point" -> {
                    val optionDataClasses: List<OptionPoint> = options.map { str-> OptionPoint(str) }
                    binding.recyclerView.adapter = PointChoiceAdapter(optionDataClasses)
                    binding.finish.setOnClickListener { onFinishClickedPoint(optionDataClasses) ; gotoWaitingRoom() }
                }

                "Multiple" -> {
                    val optionDataClasses: List<OptionCheckbox> = options.map { str-> OptionCheckbox(str) }
                    val adapter = MultipleChoiceAdapter(optionDataClasses)
                    binding.recyclerView.adapter = adapter
                    binding.finish.setOnClickListener { onFinishClickedCheckBox(adapter.getChecked()) ; gotoWaitingRoom()}
                }
                "Single" -> {
                    val optionDataClasses: List<OptionCheckbox> = options.map { str-> OptionCheckbox(str) }
                    val adapter = SingeChoiceAdapter(optionDataClasses)
                    binding.recyclerView.adapter = adapter
                    binding.finish.setOnClickListener { onFinishClickedCheckBox(adapter.getChecked()) ; gotoWaitingRoom()}
                }
                "Ranked" -> {
                    val optionDataClasses: MutableList<OptionRanked> = options.map { str-> OptionRanked(str) }.toMutableList()
                    binding.recyclerView.adapter = RankedChoiceAdapter(optionDataClasses)
                    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN,0){
                        override fun onMove(
                            recyclerView: RecyclerView,
                            source: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            val sourcePos = source.adapterPosition
                            val targetPos = target.adapterPosition

                            Collections.swap(optionDataClasses,sourcePos,targetPos)
                            binding.recyclerView.adapter!!.notifyItemMoved(sourcePos,targetPos)
                            return true
                        }

                        override fun onSwiped(
                            viewHolder: RecyclerView.ViewHolder,
                            direction: Int
                        ) {
                            TODO("Not yet implemented")
                        }
                    })
                    itemTouchHelper.attachToRecyclerView(binding.recyclerView)

                    binding.finish.setOnClickListener { onFinishClickedRanked(optionDataClasses) ; gotoWaitingRoom()}
                }
                else -> Log.e("setUpBooth", "Unknown ballot type")
            }

        }
    }


    private fun onFinishClickedRanked(options:List<OptionRanked>){
        val pollRef = ref.child("poll_$pollId")

        pollRef.runTransaction(object : Transaction.Handler{
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val currentCount = currentData.child("numOfVotesCast").getValue(Int::class.java) ?: 0
                currentData.child("votes").child(currentCount.toString()).value = options.map {option -> option.option}
                currentData.child("numOfVotesCast").value = currentCount+1
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    Log.e("poll", "Vote failed", error.toException())
                } else {
                    Log.d("poll", "Vote recorded")
                }
            }
        })
    }
    private  fun onFinishClickedPoint(options:List<OptionPoint>){
        val pollRef = ref.child("poll_$pollId")

        pollRef.runTransaction(object : Transaction.Handler{
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val currentCount = currentData.child("numOfVotesCast").getValue(Int::class.java) ?: 0
                currentData.child("votes").child(currentCount.toString()).value = options.associate {it.option to (it.points ?: -1) }
                currentData.child("numOfVotesCast").value = currentCount+1
                return Transaction.success(currentData)
            }
            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    Log.e("poll", "Vote failed", error.toException())
                } else {
                    Log.d("poll", "Vote recorded")
                }
            }
        })
    }

    private fun onFinishClickedCheckBox( options:List<OptionCheckbox>){
        val pollRef = ref.child("poll_$pollId")

        val updates = mutableMapOf<String, Any>(
            "numOfVotesCast" to ServerValue.increment(1)
        )
        for (opt in options) {
                updates["votes/${opt.option}"] = ServerValue.increment(1)
        }
        pollRef.updateChildren(updates)
    }

    private fun gotoWaitingRoom(){
        parentFragmentManager.beginTransaction().replace(R.id.FrameLayout, WaitingRoomFragment.newInstance(pollId)).commit()
    }

    companion object {
        private const val ARG_ID = "arg_id"

        fun newInstance(id: String): BoothFragment {
            return BoothFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                }
            }
        }
    }
}