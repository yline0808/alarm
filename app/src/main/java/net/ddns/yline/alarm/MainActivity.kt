package net.ddns.yline.alarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.ddns.yline.alarm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setTabhost()
    }

    private fun setTabhost(){
        binding.tabHostMain.run{
            val intent:Intent = Intent().setClass()
            val ts1 = this.newTabSpec(getString(R.string.tab1))

        }
    }
}