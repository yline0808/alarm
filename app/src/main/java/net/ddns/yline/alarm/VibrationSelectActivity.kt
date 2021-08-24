package net.ddns.yline.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.ddns.yline.alarm.databinding.ActivityVibrationSelectBinding

class VibrationSelectActivity : AppCompatActivity() {
    private val binding by lazy { ActivityVibrationSelectBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}