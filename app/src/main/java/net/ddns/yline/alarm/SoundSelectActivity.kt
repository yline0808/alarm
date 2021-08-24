package net.ddns.yline.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.ddns.yline.alarm.databinding.ActivitySoundSelectBinding

class SoundSelectActivity : AppCompatActivity()  {
    private val binding by lazy { ActivitySoundSelectBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}