package net.ddns.yline.alarm.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.ddns.yline.alarm.SetAlarmActivity
import net.ddns.yline.alarm.databinding.TabNormalAlarmListBinding

class NormalAlarmListFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TabNormalAlarmListBinding.inflate(inflater, container, false).apply {
            buttonAlarmAdd.setOnClickListener { startActivity(Intent(context, SetAlarmActivity::class.java)) }
        }.root
    }
}