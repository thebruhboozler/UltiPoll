package com.ultipoll.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ultipoll.R
import com.ultipoll.dataclasses.OptionCheckbox

class MultipleChoiceAdapter(private  val options: List<OptionCheckbox>):
    RecyclerView.Adapter<MultipleChoiceAdapter.OptionHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.option_checkbox, parent, false)
        return OptionHolder(view)
    }

    override fun onBindViewHolder(
        holder: OptionHolder,
        position: Int
    ) {
        val option = options[position]
        holder.optionText.text = option.option
        holder.checkBox.isChecked = option.isChecked
    }

    fun getChecked(): List<OptionCheckbox> =
        options.filter { it.isChecked }

    override fun getItemCount() = options.count()

    inner class OptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val optionText: TextView = itemView.findViewById(R.id.option)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        init{
            checkBox.setOnClickListener {
                val pos = adapterPosition
                if (pos == RecyclerView.NO_POSITION) return@setOnClickListener
                options[pos].isChecked = !options[pos].isChecked
            }
        }
    }
}