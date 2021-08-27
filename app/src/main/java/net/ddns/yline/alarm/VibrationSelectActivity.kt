package net.ddns.yline.alarm

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.ddns.yline.alarm.adapter.VibrationAdapter
import net.ddns.yline.alarm.databinding.ActivityVibrationSelectBinding
import net.ddns.yline.alarm.detailsLookup.VibrationItemDetailsLookup
import net.ddns.yline.alarm.table.Vibration

class VibrationSelectActivity : AppCompatActivity() {
    private val binding by lazy { ActivityVibrationSelectBinding.inflate(layoutInflater) }
    private val vibrationAdapter by lazy { VibrationAdapter(vibrationList) }
    private val vibrator:Vibrator by lazy { getSystemService(VIBRATOR_SERVICE) as Vibrator }
    private val vibrationList: MutableList<Vibration> = mutableListOf()
    private lateinit var selectedVibration: Vibration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        pushVibrationType()
        setListener()
        setVibrationRecyclerview()
    }
    // ===== 기능 =====
    private fun pushVibrationType() {
        val noVibrationTiming = longArrayOf()
        vibrationList.add(Vibration(resources.getString(R.string.textview_default_select), noVibrationTiming))
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
            vibrationAdapter.setOnItemClickListener(itemClickListener())
            clickListener().also {
                buttonBack.setOnClickListener(it)
                buttonVibrationSave.setOnClickListener(it)
            }
        }
    }

    private fun setVibrationRecyclerview(){
        binding.recyclerviewVibrationList.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewVibrationList.adapter = vibrationAdapter

        val tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            binding.recyclerviewVibrationList,
            StableIdKeyProvider(binding.recyclerviewVibrationList),
            VibrationItemDetailsLookup(binding.recyclerviewVibrationList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()

        vibrationAdapter.tracker = tracker
    }

    // ===== 리스너 =====
    inner class clickListener:View.OnClickListener{
        override fun onClick(v: View) {
            binding.run {
                when(v.id){
                    buttonVibrationSave.id -> {
                        vibrator.cancel()
                        finish()
                    }
                    buttonBack.id -> {
                        vibrator.cancel()
                        finish()
                    }
                }
            }
        }
    }

    inner class itemClickListener:VibrationAdapter.OnItemClickListener{
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onItemClick(v: View, pos: Int, vib: Vibration) {
            vibrator.vibrate(VibrationEffect.createWaveform(vib.timing, -1))
            Log.d("|||", "${pos}, ${vib}")
            selectedVibration = vib
        }
    }
}