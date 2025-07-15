package com.ultipoll.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ultipoll.R
import com.ultipoll.dataclasses.OptionPoint
import kotlin.math.max
import kotlin.math.min

class PointChoiceAdapter(private var options: List<OptionPoint>):
    RecyclerView.Adapter<PointChoiceAdapter.OptionHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.option_point, parent, false)
        return OptionHolder(view)
    }

    override fun onBindViewHolder(
        holder: OptionHolder,
        position: Int
    ) {
        val option = options[position]
        holder.optionText.text = option.option
        holder.optionPoints.text = (option.points?: "").toString()
    }

    override fun getItemCount() = options.count()

    inner class OptionHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val optionText: TextView = itemView.findViewById(R.id.option)
        val increaseRating: ImageButton = itemView.findViewById(R.id.increase)
        val decreaseRating: ImageButton = itemView.findViewById(R.id.decrease)
        val optionPoints: TextView = itemView.findViewById(R.id.points)

        init{
            increaseRating.setOnClickListener {
                val pos = adapterPosition
                if(pos == RecyclerView.NO_POSITION) return@setOnClickListener
                val item = options[pos]
                if(item.points == null) {
                    item.points = 1
                }else{
                    item.points = min(item.points!! +1, 10)
                }
                notifyItemChanged(pos)
            }
            decreaseRating.setOnClickListener {
                val pos = adapterPosition
                if(pos == RecyclerView.NO_POSITION) return@setOnClickListener
                val item = options[pos]
                if(item.points != null) {
                    item.points = max(item.points!! -1, 1)
                    notifyItemChanged(pos)
                }
            }
        }
    }
}