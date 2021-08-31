package net.ddns.yline.alarm

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioManager.STREAM_RING
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import net.ddns.yline.alarm.databinding.ActivitySetAlarmBinding
import net.ddns.yline.alarm.table.Song
import net.ddns.yline.alarm.table.Vibration
import java.lang.Exception
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class SetAlarmActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySetAlarmBinding.inflate(layoutInflater) }
    private var selectedSong: Song? = null
    private var selectedVibration: Vibration? = null
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setDefaultTime()
        setListener()
    }

    // ===== 기능 =====
    private fun setDefaultTime(){
        val now:String = SimpleDateFormat("H:mm").format(Date(System.currentTimeMillis()))
        val h:Int = now.substring(0, now.indexOf(":")).toInt()
        val m:Int = now.substring(now.indexOf(":") + 1).toInt()

        binding.timepickerTimeSet.apply {
            hour = h
            minute = m
        }
        binding.textviewTopTime.text = convTimeToString(h, m)
        binding.textviewTopWeek.text = convWeekInfo()
    }

    private fun convTimeToString(hourOfDay:Int, minute:Int):String{
        return SimpleDateFormat("a h:mm").format(Time(hourOfDay, minute, 0))
    }

    private fun convWeekInfo():String {
        return arrayOf(
            binding.togglebuttonSelSun, binding.togglebuttonSelMon, binding.togglebuttonSelTue,
            binding.togglebuttonSelWed, binding.togglebuttonSelThu, binding.togglebuttonSelFri,
            binding.togglebuttonSelSat).fold(""){ acc, item ->
            if(item.isChecked) acc + item.text
            else acc + ""
        }
    }

    private fun setListener(){
        with(binding) {
            clickListener().also {
                imageButtonBack.setOnClickListener(it)
                buttonAlarmSave.setOnClickListener(it)
                buttonAlarmDelete.setOnClickListener(it)
                buttonTimeAll.setOnClickListener(it)
                buttonTimeWeekday.setOnClickListener(it)
                buttonTimeWeekend.setOnClickListener(it)
                constraintNameSet.setOnClickListener(it)
                constraintSoundSet.setOnClickListener(it)
                constraintVibrationSet.setOnClickListener(it)
                constraintRepeatSet.setOnClickListener(it)
            }
            checkedChangeListener().also {
                togglebuttonSelSun.setOnCheckedChangeListener(it)
                togglebuttonSelMon.setOnCheckedChangeListener(it)
                togglebuttonSelTue.setOnCheckedChangeListener(it)
                togglebuttonSelWed.setOnCheckedChangeListener(it)
                togglebuttonSelThu.setOnCheckedChangeListener(it)
                togglebuttonSelFri.setOnCheckedChangeListener(it)
                togglebuttonSelSat.setOnCheckedChangeListener(it)
            }
            timepickerTimeSet.setOnTimeChangedListener(timeChangedListener())
            seekbarSound.setOnSeekBarChangeListener(seekBarChangeListener())
        }
    }

    private fun convWeekCheck(weekInfo:BooleanArray){
        arrayOf(
            binding.togglebuttonSelSun, binding.togglebuttonSelMon, binding.togglebuttonSelTue,
            binding.togglebuttonSelWed, binding.togglebuttonSelThu, binding.togglebuttonSelFri,
            binding.togglebuttonSelSat).forEachIndexed { idx, toggleButton -> toggleButton.isChecked = weekInfo[idx] }
    }

    private fun setSoundSelect(s:Song){
        selectedSong = s
        binding.textviewSoundTitle.text = s.title
        binding.switchSound.isChecked = s != null
    }

    private fun setVibrationSelect(vib: Vibration){
        selectedVibration = vib
        binding.textviewVibrationTitle.text = vib.name
        binding.switchVibration.isChecked = vib != null
    }

    private fun pretreatmentAlarmTitle(title:String):String{
        return title.replace("\n", "").run {
            if(this.length > 20) this.substring(0, 20) else this
        }
    }

    // ===== 리스너 =====
    inner class clickListener:View.OnClickListener{
        override fun onClick(v: View?) {
            binding.run {
                when(v?.id){
                    imageButtonBack.id -> finish()
                    buttonAlarmDelete.id -> finish()
                    buttonAlarmSave.id -> {
                        Toast.makeText(applicationContext, "save!!!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    buttonTimeAll.id -> convWeekCheck(booleanArrayOf(true, true, true, true, true, true, true))
                    buttonTimeWeekday.id -> convWeekCheck(booleanArrayOf(false, true, true, true, true, true, false))
                    buttonTimeWeekend.id -> convWeekCheck(booleanArrayOf(true, false, false, false, false, false, true))
                    constraintNameSet.id -> setTitleDialog()
                    constraintSoundSet.id -> {
                        startForSoundResult.launch(Intent(applicationContext, SoundSelectActivity::class.java))
                    }
                    constraintVibrationSet.id -> {
                        startForVibrationResult.launch(
                            Intent(applicationContext, VibrationSelectActivity::class.java).apply {
                                putExtra("selectedVibration", if(selectedVibration != null) selectedVibration else null)
                            }
                        )
                    }
                    constraintRepeatSet.id -> {
//                        DialogFragment().show(supportFragmentManager, "dialog")
//                        startActivity(Intent(applicationContext, ))
                    }
                    else -> {
                        Log.e("|||null listener error", "${v.toString()}")
                        Toast.makeText(applicationContext, "클릭 오류!\n관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    inner class checkedChangeListener:CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            when{
                buttonView == null -> Toast.makeText(applicationContext, "Click listener error! please contact app manager!", Toast.LENGTH_SHORT).show()
                buttonView.text == "일" && !isChecked -> buttonView.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                buttonView.text == "토" && !isChecked -> buttonView.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                isChecked -> buttonView.setTextColor(ContextCompat.getColor(applicationContext, R.color.main))
                else -> buttonView.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            }
            binding.textviewTopWeek.text = convWeekInfo()
        }
    }

    inner class seekBarChangeListener:SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            (getSystemService(AUDIO_SERVICE) as AudioManager).apply {
                setStreamVolume(STREAM_RING, progress, 0)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            try {
                if(selectedSong == null){
                    Toast.makeText(applicationContext, "설정된 소리가 없습니다.", Toast.LENGTH_SHORT).show()
                    return
                }
                mediaPlayer = MediaPlayer()
                mediaPlayer.apply {
                    setDataSource(applicationContext, Uri.parse(selectedSong!!.uri))
                    setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(STREAM_RING).build())
                    isLooping = true
                    prepare()
                    start()
                }
            }catch (e:Exception){
                mediaPlayer.pause()
                mediaPlayer.release()
                mediaPlayer = MediaPlayer()
                Log.e("media seekbar error", e.stackTraceToString())
            }
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            mediaPlayer.run {
                if(this != null && isPlaying){
                    stop()
                    release()
                }
                release()
            }
        }
    }

    inner class timeChangedListener:TimePicker.OnTimeChangedListener{
        override fun onTimeChanged(v: TimePicker?, hourOfDay: Int, minute: Int) {
            binding.textviewTopTime.text = convTimeToString(hourOfDay, minute)
        }
    }

    // ===== 다이얼로그 =====
    private fun setTitleDialog(){
        val editText:EditText = EditText(applicationContext).apply {
            text = binding.textviewNameTitle.text.let {
                if(it == resources.getString(R.string.textview_default_select)) null
                else it.toString().toEditable()
            }
        }
        AlertDialog.Builder(this@SetAlarmActivity).apply {
            setTitle(resources.getString(R.string.alarm_name))
            setMessage("20자 이내로 알람 이름을 설정해주세요.")
            setView(editText)
            setPositiveButton(resources.getString(R.string.ok), DialogInterface.OnClickListener { _, _ ->
                if(editText.text.toString() == ""){
                    binding.textviewNameTitle.text = resources.getString(R.string.textview_default_select)
                    binding.switchName.isChecked = false
                    Toast.makeText(applicationContext, "알람 제목을 설정하지 않았습니다.", Toast.LENGTH_SHORT).show()
                }else{
                    binding.textviewNameTitle.text = pretreatmentAlarmTitle(editText.text.toString())
                    Log.d("|||text len", binding.textviewNameTitle.text.length.toString())
                    binding.switchName.isChecked = true
                }
            })
        }.show()
    }

    // ===== 엑티비티 결과 =====
    private val startForSoundResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        when(result.resultCode){
            RESULT_OK -> {
                val intent = result.data
                Toast.makeText(applicationContext, "sound result ok", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val startForVibrationResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        when(result.resultCode){
            RESULT_OK -> {
                selectedVibration = result.data?.getSerializableExtra("selectedVibration") as Vibration
                if(selectedVibration == null){
                    Log.e("vibration is null", "SetAlarmActivity/startForVibrationResult")
                    Toast.makeText(applicationContext, "진동 설정 오류!\n관리자에게 문의하세요.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext, "설정 완료", Toast.LENGTH_SHORT).show()
                }
            }
            RESULT_CANCELED -> {
                Log.d("vibration set canceled", "SetAlarmActivity/startForVibrationResult")
            }
        }
    }

    // ===== 확장 =====
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    // ===== 테스트용 함수 =====
    private fun toast(s:String){
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    // ===== 준비중 =====
//    private fun setCalendarDialog(){
//        var listener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
//            val test = "${i}년 ${i2 + 1}월 ${i3}일 ${SimpleDateFormat("EEEE").format(Date(i, i2, i3-1))}"
//            toast(test)
//        }
//
//        Calendar.getInstance().apply {
//            DatePickerDialog(this@SetAlarmActivity, listener, get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)).show()
//
//        }
//    }
}