package net.ddns.yline.alarm.detailsLookup

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import net.ddns.yline.alarm.adapter.VibrationAdapter

class VibrationItemDetailsLookup(private val recyclerView: RecyclerView): ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if(view != null){
            return (recyclerView.getChildViewHolder(view) as VibrationAdapter.VibrationViewHolder).getItemDetails()
        }
        return null
    }
}