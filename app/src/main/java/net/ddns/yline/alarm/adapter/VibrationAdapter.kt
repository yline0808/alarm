package net.ddns.yline.alarm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.ddns.yline.alarm.databinding.ItemVibrationBinding
import net.ddns.yline.alarm.table.Vibration

class VibrationAdapter(private val vibrations: MutableList<Vibration>) : RecyclerView.Adapter<VibrationAdapter.VibrationViewHolder>() {
    private lateinit var mListener:VibrationAdapter.OnItemClickListener
    var lastCheckedPos:Int = 0

    interface OnItemClickListener {
        fun onItemClick(v: View, pos: Int, vib: Vibration)
    }

    fun setOnItemClickListener(listener: VibrationAdapter.OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VibrationAdapter.VibrationViewHolder {
        val binding = ItemVibrationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VibrationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vibrations.size
    }

    override fun onBindViewHolder(holder: VibrationViewHolder, position: Int) {
        holder.bind(vibrations[position], position == lastCheckedPos)

    }

    inner class VibrationViewHolder(private val binding: ItemVibrationBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Vibration, isSelected:Boolean = false){
            binding.apply {
                textviewName.text = item.name
                imageviewItemCheck.visibility = if(isSelected) View.VISIBLE else View.INVISIBLE
            }
            itemView.setOnClickListener {
                notifyItemChanged(lastCheckedPos)
                notifyItemChanged(bindingAdapterPosition)
                lastCheckedPos = bindingAdapterPosition
                mListener.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }
}