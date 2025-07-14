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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.ultipoll.Adapters.MultipleChoiceAdapter
import com.ultipoll.Adapters.PointChoiceAdapter
import com.ultipoll.Adapters.RankedChoiceAdapter
import com.ultipoll.Adapters.SingeChoiceAdapter
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

            when(type){
                "Point" -> setUpPointBooth(options)
                "Ranked" -> setUpRankedBooth(options)
                "Multiple" -> setUpMultipleBooth(options)
                "Single" -> setUpSingleBooth(options)
                else -> Log.e("setUpBooth", "Unknown ballot type")
            }
        }
    }

    fun setUpPointBooth(options: List<String>){
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val optionDataClasses: List<OptionPoint> = options.filterNotNull().map { str-> OptionPoint(str) }
        val adapter = PointChoiceAdapter(optionDataClasses)
        binding.recyclerView.adapter = adapter
    }
    fun setUpSingleBooth(options: List<String>){
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val optionDataClasses: List<OptionCheckbox> = options.filterNotNull().map { str-> OptionCheckbox(str) }
        val adapter = SingeChoiceAdapter(optionDataClasses)
        binding.recyclerView.adapter = adapter
    }
    fun setUpRankedBooth(options: List<String>){
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val optionDataClasses: List<OptionRanked> = options.filterNotNull().map { str-> OptionRanked(str) }
        val adapter = RankedChoiceAdapter(optionDataClasses)
        binding.recyclerView.adapter = adapter

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
                adapter.notifyItemMoved(sourcePos,targetPos)
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
    }
    fun setUpMultipleBooth(options: List<String>){
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val optionDataClasses: List<OptionCheckbox> = options.filterNotNull().map { str-> OptionCheckbox(str) }
        val adapter = MultipleChoiceAdapter(optionDataClasses)
        binding.recyclerView.adapter = adapter
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