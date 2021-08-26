package net.ddns.yline.alarm

import android.os.Bundle
import android.os.Vibrator
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import net.ddns.yline.alarm.adapter.VibrationAdapter
import net.ddns.yline.alarm.databinding.ActivityVibrationSelectBinding
import net.ddns.yline.alarm.table.Vibration
import java.util.ArrayList

class VibrationSelectActivity : AppCompatActivity() {
    private val binding by lazy { ActivityVibrationSelectBinding.inflate(layoutInflater) }
    private val vibrationList: MutableList<Vibration> = mutableListOf()
    private val vibrator:Vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    private var vibrationAdapter:VibrationAdapter = VibrationAdapter(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

    }

    private fun setVibrationRecyclerview(){

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

    inner class queryTextListener:SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {

        }
    }

    // ===== 테스트 =====

}