package net.ddns.yline.alarm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import net.ddns.yline.alarm.adapter.VibrationAdapter
import net.ddns.yline.alarm.databinding.ActivityVibrationSelectBinding
import net.ddns.yline.alarm.table.Vibration

class VibrationSelectActivity : AppCompatActivity() {
    private val binding by lazy { ActivityVibrationSelectBinding.inflate(layoutInflater) }
    private val vibrationAdapter by lazy { VibrationAdapter(vibrationList) }
    private val vibrator:Vibrator by lazy { getSystemService(VIBRATOR_SERVICE) as Vibrator }
    private val vibrationList: MutableList<Vibration> = mutableListOf()
    private var selectedVibration: Vibration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        selectedVibration = intent.getSerializableExtra("selectedVibration") as Vibration?
        pushVibrationType()
        setListener()
        setVibrationRecyclerview()
    }
    // ===== 기능 =====
    private fun pushVibrationType() {
        val basicVibrationTiming = longArrayOf(100, 1000, 900)
        vibrationList.add(Vibration(resources.getString(R.string.vibration_basic), basicVibrationTiming))
        val highVibrationTiming = longArrayOf(100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100)
        vibrationList.add(Vibration(resources.getString(R.string.vibration_high), highVibrationTiming))
        val staccatoVibrationTiming = longArrayOf(100, 100, 100, 500)
        vibrationList.add(Vibration(resources.getString(R.string.vibration_staccato), staccatoVibrationTiming))
        val heartVibrationTiming = longArrayOf(100, 100, 100, 100, 500, 100, 100, 100)
        vibrationList.add(Vibration(resources.getString(R.string.vibration_heart), heartVibrationTiming))
        val symphonyVibrationTiming = longArrayOf(100, 100, 100, 100, 100, 100, 100, 1000, 500, 100, 100, 100, 100, 100, 100, 1000)
        vibrationList.add(Vibration(resources.getString(R.string.vibration_symphony), symphonyVibrationTiming))
        val accentVibrationTiming = longArrayOf(100, 50, 200, 500)
        vibrationList.add(Vibration(resources.getString(R.string.vibration_accent), accentVibrationTiming))
        val sosVibrationTiming = longArrayOf(100, 200, 100, 200, 100, 200, 400, 500, 100, 500, 100, 500, 400, 200, 100, 200, 100, 200)
        vibrationList.add(Vibration(resources.getString(R.string.vibration_sos), sosVibrationTiming))
    }

    private fun setListener(){
        binding.apply {
            ClickListener().also {
                buttonBack.setOnClickListener(it)
                buttonVibrationSave.setOnClickListener(it)
            }
        }
    }

    private fun setVibrationRecyclerview(){
        val selectedIdx = vibrationList.indexOfFirst { it.name == selectedVibration?.name }
        vibrationAdapter.apply {
            lastCheckedPos = if(selectedIdx == -1) 0 else selectedIdx
            setOnItemClickListener(ItemClickListener())
        }
        binding.recyclerviewVibrationList.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewVibrationList.adapter = vibrationAdapter
    }

    private fun saveAction(){
        vibrator.cancel()
        setResult(RESULT_OK, intent.putExtra("selectedVibration", selectedVibration))
        finish()
    }

    private fun backAction(){
        vibrator.cancel()
        setResult(RESULT_CANCELED)
        finish()
    }

    // ===== 리스너 =====
    inner class ClickListener:View.OnClickListener{
        override fun onClick(v: View) {
            binding.apply {
                when(v.id){
                    buttonVibrationSave.id -> saveAction()
                    buttonBack.id -> backAction()
                }
            }
        }
    }

    inner class ItemClickListener:VibrationAdapter.OnItemClickListener{
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onItemClick(v: View, pos: Int, vib: Vibration) {
            vibrator.vibrate(VibrationEffect.createWaveform(vib.timing, -1))
            selectedVibration = vib
        }
    }
}