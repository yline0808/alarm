package net.ddns.yline.alarm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.ddns.yline.alarm.R
import net.ddns.yline.alarm.table.Vibration
import java.util.ArrayList

class VibrationAdapter(private val context: Context) : RecyclerView.Adapter<VibrationAdapter.VibrationViewHolder>(), Filterable {
    val vibrations = mutableListOf<Vibration>()
    val vibrationsAll = mutableListOf<Vibration>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VibrationAdapter.VibrationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_sound_vibration, parent, false)
        return VibrationViewHolder(view)
    }

    override fun onBindViewHolder(holder: VibrationViewHolder, position: Int) {
        holder.bind(vibrations[position])
    }

    override fun getItemCount(): Int {
        return vibrations.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredVibrations = ArrayList<Vibration>()

                if (constraint == null || constraint.isEmpty()) {
                    filteredVibrations.addAll(vibrationsAll)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim { it <= ' ' }
                    for (vibration in vibrationsAll) {
                        if (vibration.name.lowercase().contains(filterPattern)) {
                            filteredVibrations.add(vibration)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredVibrations
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                vibrations.clear()
                vibrations.addAll(results.values as MutableList<Vibration>)
                notifyDataSetChanged()
            }
        }
    }

    inner class VibrationViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val radiobutton:RadioButton = itemView.findViewById(R.id.radio_button)
        private val textview:TextView = itemView.findViewById(R.id.textview_name)

        fun bind(item: Vibration){
            textview.text = item.name

        }
    }
}