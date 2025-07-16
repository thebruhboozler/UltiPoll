package com.ultipoll

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.ultipoll.adapters.HistoryAdapter
import com.ultipoll.databinding.FragmentHistoryBinding
import com.ultipoll.databinding.FragmentWaitingRoomBinding
import com.ultipoll.dataclasses.PollResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


suspend fun getPollIds(context: Context): Set<String> {
    val preferences = context.pollDataStore.data.first()
    return preferences[POLL_ID_SET_KEY] ?: emptySet()
}


class HistoryFragment : Fragment() {

    private var polls: MutableList<PollResult> = mutableListOf()
    private lateinit var binding: FragmentHistoryBinding

    val database =
        FirebaseDatabase.getInstance("https://ultipoll-default-rtdb.europe-west1.firebasedatabase.app")
    val ref = database.getReference("polls")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val pollIds = getPollIds(requireContext()).toList()
            val pollResults = mutableListOf<PollResult>()

            for (id in pollIds) {
                val snapshot = ref.child("poll_$id").get().await()

                if(!snapshot.exists()) continue

                val winner = snapshot.child("winner").getValue(Int::class.java) ?: 0
                val options = snapshot.child("options").getValue(object :
                    GenericTypeIndicator<List<String>>() {})
                val winningOption = if (winner == -1) "undecided" else options?.getOrNull(winner) ?: "undecided"

                pollResults.add(
                    PollResult(
                        winningOption,
                        snapshot.child("participants").getValue(Int::class.java) ?: 0,
                        snapshot.child("title").getValue(String::class.java) ?: "Unknown"
                    )
                )
            }

            binding.history.layoutManager = LinearLayoutManager(requireContext())
            binding.history.adapter = HistoryAdapter(pollResults)
        }
    }

}