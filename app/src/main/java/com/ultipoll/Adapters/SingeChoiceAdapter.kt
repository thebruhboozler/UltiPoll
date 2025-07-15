package com.ultipoll.Adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.ultipoll.dataclasses.OptionCheckbox
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.ultipoll.R

class SingeChoiceAdapter(private  var options: List<OptionCheckbox>):
    RecyclerView.Adapter<SingeChoiceAdapter.OptionHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.option_checkbox, parent, false)
        return OptionHolder(view)
    }

    private var selectedPosition = -1
    override fun onBindViewHolder(
        holder: OptionHolder,
        position: Int
    ) {
        val option = options[position]
        holder.optionText.text = option.option
        holder.checkBox.isChecked = (position == selectedPosition)
    }

    fun getChecked(): List<OptionCheckbox> =
        options.filter { it.isChecked }

    override fun getItemCount() = options.count()

    inner  class OptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val optionText: TextView = itemView.findViewById(R.id.option)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        init {
            checkBox.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {

                    if (selectedPosition != -1) {
                        options[selectedPosition].isChecked = false
                        notifyItemChanged(selectedPosition)
                    }

                    selectedPosition = pos
                    options[pos].isChecked = true
                    notifyItemChanged(pos)
                }
            }
        }
    }
}