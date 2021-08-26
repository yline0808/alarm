package net.ddns.yline.alarm.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.ddns.yline.alarm.SetLocationActivity
import net.ddns.yline.alarm.databinding.TabLocationAlarmListBinding

class LocationAlarmListFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return TabLocationAlarmListBinding.inflate(inflater, container, false).apply {
            buttonLocationAdd.setOnClickListener { startActivity(Intent(context, SetLocationActivity::class.java)) }
        }.root
    }
}