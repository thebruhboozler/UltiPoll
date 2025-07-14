package com.ultipoll.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ultipoll.R
import com.ultipoll.dataclasses.OptionRanked

class RankedChoiceAdapter(private var options: List<OptionRanked>):
    RecyclerView.Adapter<RankedChoiceAdapter.OptionHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.option_ranked, parent, false)
        return OptionHolder(view)
    }

    override fun onBindViewHolder(
        holder: OptionHolder,
        position: Int
    ) {
        val option = options[position]
        holder.optionText.text = option.option
    }

    override fun getItemCount() = options.count()

    inner class OptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val optionText: TextView = itemView.findViewById(R.id.option)
    }
}