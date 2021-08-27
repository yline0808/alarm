package net.ddns.yline.alarm.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import net.ddns.yline.alarm.R
import net.ddns.yline.alarm.table.Vibration

class VibrationAdapter(private val vibrations: MutableList<Vibration>) : RecyclerView.Adapter<VibrationAdapter.VibrationViewHolder>() {
    private lateinit var mListener:VibrationAdapter.OnItemClickListener
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VibrationAdapter.VibrationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vibration, parent, false)
        return VibrationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vibrations.size
    }

    override fun onBindViewHolder(holder: VibrationViewHolder, position: Int) {
        tracker?.let {
            holder.bind(vibrations[position], it.isSelected(position.toLong()))
        }
    }

    fun setOnItemClickListener(listener:VibrationAdapter.OnItemClickListener){
        mListener = listener
    }

    interface OnItemClickListener : (View) -> Unit {
        fun onItemClick(v: View, pos: Int, vib: Vibration)
    }

    inner class VibrationViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val imageviewStatus:ImageView = itemView.findViewById(R.id.imageview_item_status)
        private val textview:TextView = itemView.findViewById(R.id.textview_name)
        private val imageviewCheckBox:ImageView = itemView.findViewById(R.id.imageview_item_check)

        fun bind(item: Vibration, isSelected:Boolean){
            itemView.setOnClickListener(View.OnClickListener(
                mListener
            ))
            textview.text = item.name
            itemView.setBackgroundColor(
                if(isSelected) Color.argb(100, 100, 100, 100)
                else Color.argb(0,255,255,255)
            )
            imageviewCheckBox.visibility = if(isSelected) View.VISIBLE else View.INVISIBLE
            imageviewStatus.apply {
                when {
                    item.name == "설정 안 함" -> setImageResource(R.drawable.ic_vibration_off_24)
                    isSelected -> setImageResource(R.drawable.ic_vibration_activiate_24)
                    else -> setImageResource(R.drawable.ic_vibration_normal_24)
                }
            }
        }

        fun getItemDetails():ItemDetailsLookup.ItemDetails<Long> = object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = bindingAdapterPosition
            override fun getSelectionKey(): Long? = itemId
        }
    }
}