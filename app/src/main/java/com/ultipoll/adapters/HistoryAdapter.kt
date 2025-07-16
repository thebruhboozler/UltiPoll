package com.ultipoll.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ultipoll.R
import com.ultipoll.dataclasses.PollResult

class HistoryAdapter(private  val results: List<PollResult>):
    RecyclerView.Adapter<HistoryAdapter.ResultHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.poll_result, parent, false)
        return ResultHolder(view)
    }

    override fun onBindViewHolder(
        holder: ResultHolder,
        position: Int
    ) {
        val result = results[position]
        holder.title.text = result.title
        holder.participantNum.text = result.participant.toString()
        holder.winner.text = result.winner
    }

    override fun getItemCount() = results.count()

    inner class ResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val participantNum: TextView = itemView.findViewById(R.id.participantNum)
        val winner: TextView = itemView.findViewById(R.id.winner)
    }
}