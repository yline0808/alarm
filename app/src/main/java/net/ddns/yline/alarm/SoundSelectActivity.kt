package net.ddns.yline.alarm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import net.ddns.yline.alarm.databinding.ActivitySoundSelectBinding
import net.ddns.yline.alarm.table.Song

class SoundSelectActivity : AppCompatActivity()  {
    private val binding by lazy { ActivitySoundSelectBinding.inflate(layoutInflater) }
    private var selectedSound:Song? = null


    // ===== overrid function =====
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        selectedSound = intent.getSerializableExtra("selectedSound") as Song?
        setListener()
        setSoundRecyclerview()
    }


    // ===== function =====
    private fun setListener(){
        binding.apply {
            ClickListener().also {

            }
        }
    }

    private fun setSoundRecyclerview(){

    }

    private fun saveActioin(){
        setResult(RESULT_OK, intent.putExtra("selectedSound", selectedSound))
    }

    private fun backAction(){
        setResult(RESULT_CANCELED)
        finish()
    }

    // ===== listener =====
    inner class ClickListener:View.OnClickListener{
        override fun onClick(v: View) {
            binding.apply {
                when(v.id){

                }
            }
        }
    }
}