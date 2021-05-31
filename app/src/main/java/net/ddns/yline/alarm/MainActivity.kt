package net.ddns.yline.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import net.ddns.yline.alarm.adapter.PagerFragmentStateAdapter
import net.ddns.yline.alarm.databinding.ActivityMainBinding
import net.ddns.yline.alarm.fragment.NormalAlarmListFragment
import net.ddns.yline.alarm.fragment.LocationAlarmListFragment
import net.ddns.yline.alarm.fragment.SettingAppFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pagerAdapter = PagerFragmentStateAdapter(this).apply {
            addFragment(NormalAlarmListFragment())
            addFragment(LocationAlarmListFragment())
            addFragment(SettingAppFragment())
        }

        val viewPager:ViewPager2 = binding.pager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        TabLayoutMediator(binding.tabLayout, viewPager){tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.tab1)
                1 -> getString(R.string.tab2)
                2 -> getString(R.string.tab3)
                else -> getString(R.string.default_sel)
            }
        }.attach()
    }
}