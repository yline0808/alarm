package net.ddns.yline.alarm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.ddns.yline.alarm.databinding.ItemSoundBinding
import net.ddns.yline.alarm.table.Song

class SoundAdapter(private val sounds: MutableList<Song>):RecyclerView.Adapter<SoundAdapter.SoundViewHolder>() {
    private lateinit var mListener:SoundAdapter.OnItemClickListener
    var lastCheckedPos:Int = 0

    interface OnItemClickListener{
        fun onItemClick(v:View, pos:Int, sound:Song)
    }

    fun setOnItemClickListener(listener:SoundAdapter.OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val binding = ItemSoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sounds.size
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        holder.bind(sounds[position], position == lastCheckedPos)
    }

    inner class SoundViewHolder(private val binding: ItemSoundBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Song, isSelected:Boolean = false){
            binding.apply {
                textviewName.text = item.title
                imageviewItemCheck.visibility = if(isSelected) View.VISIBLE else View.INVISIBLE
            }
            itemView.setOnClickListener {
                lastCheckedPos = bindingAdapterPosition
                mListener.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }
}