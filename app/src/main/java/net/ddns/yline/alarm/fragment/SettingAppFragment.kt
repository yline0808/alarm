package net.ddns.yline.alarm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.ddns.yline.alarm.databinding.TabAppSettingBinding

class SettingAppFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TabAppSettingBinding.inflate(
            inflater,
            container,
            false
        ).apply {  }.root
    }
}