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
import io.reactivex.rxjava3.disposables.CompositeDisposable
import net.ddns.yline.alarm.databinding.ActivitySetAlarmBinding
import net.ddns.yline.alarm.fragment.RepeatDialogFragment
import net.ddns.yline.alarm.table.Song
import net.ddns.yline.alarm.table.Vibration
import java.lang.Exception
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class SetAlarmActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySetAlarmBinding.inflate(layoutInflater) }
    private var selectedSound: Song? = null
    private var selectedVibration: Vibration? = null
    private val myCompositeDisposable = CompositeDisposable()
    private lateinit var mediaPlayer: MediaPlayer

    // ===== override function =====
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setDefaultTime()
        setListener()
    }

    override fun onDestroy() {
        this.myCompositeDisposable.clear()
        super.onDestroy()
    }

    // ===== function =====
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

    private fun setListener(){
        with(binding) {
            ClickListener().also {
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
                switchVibration.setOnClickListener(it)
            }
            CheckedChangeListener().also {
                togglebuttonSelSun.setOnCheckedChangeListener(it)
                togglebuttonSelMon.setOnCheckedChangeListener(it)
                togglebuttonSelTue.setOnCheckedChangeListener(it)
                togglebuttonSelWed.setOnCheckedChangeListener(it)
                togglebuttonSelThu.setOnCheckedChangeListener(it)
                togglebuttonSelFri.setOnCheckedChangeListener(it)
                togglebuttonSelSat.setOnCheckedChangeListener(it)
            }

            timepickerTimeSet.setOnTimeChangedListener(TimeChangedListener())
            seekbarSound.setOnSeekBarChangeListener(SeekBarChangeListener())
        }
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

    private fun convWeekCheck(weekInfo:BooleanArray){
        arrayOf(
            binding.togglebuttonSelSun, binding.togglebuttonSelMon, binding.togglebuttonSelTue,
            binding.togglebuttonSelWed, binding.togglebuttonSelThu, binding.togglebuttonSelFri,
            binding.togglebuttonSelSat).forEachIndexed { idx, toggleButton -> toggleButton.isChecked = weekInfo[idx] }
    }

    private fun setSoundSelect(s:Song){
        selectedSound = s
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

    private fun vibrationSwitchAction(){
//        ===test===
//        val switchChangeObservable = binding.switchVibration.checkedChanges()
//        val switchChangeSubscription:Disposable =
//            switchChangeObservable.debounce( 800, TimeUnit.MICROSECONDS )
//                .subscribeOn(Schedulers.io())
//                .subscribeBy {
//
//                }
//        myCompositeDisposable.add(switchChangeSubscription)

        binding.run {
            if(textviewVibrationTitle.text == resources.getString(R.string.textview_default_select) && switchVibration.isChecked){
                switchVibration.isChecked = false
                toast("선택된 진동이 없습니다.")
                Log.d("vibration selection null", "SetAlarmActivity/vibrationSwitchAction")
            }
        }
    }

    private fun soundSetAction(){
        startForSoundResult.launch(
            Intent(applicationContext, SoundSelectActivity::class.java).apply {
                putExtra("selectedSound", if(selectedSound != null) selectedSound else null)
            }
        )
    }

    private fun vibrationSetAction(){
        startForVibrationResult.launch(
            Intent(applicationContext, VibrationSelectActivity::class.java).apply {
                putExtra("selectedVibration", if(selectedVibration != null) selectedVibration else null)
            }
        )
    }

    private fun repeatSetAction(){
        RepeatDialogFragment().apply {
            setButtonClickListener(object:RepeatDialogFragment.OnCompleteListener{
                override fun onButtonRepeatSetClicked(minute: String?, count: String?) {
                    binding.apply {
                        textviewRepeatTitle.text = String.format(getString(R.string.textview_repeat_text), minute, count)
                        switchRepeat.isChecked = true
                    }
                }
            })
        }.show(supportFragmentManager, "SetRepeatDialog")
    }

    private fun alarmSaveAction(){
        toast("save!!!")
        finish()
    }

    private fun clickDefaultAction(v:View?){
        Log.e("|||null listener error", v.toString())
        toast("error")
    }

    private fun repeatSwitchAction(){

    }

    private fun setTitleAction(){
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
            setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                if (editText.text.toString() == "") {
                    binding.textviewNameTitle.text = resources.getString(R.string.textview_default_select)
                    binding.switchName.isChecked = false
                    toast("알람 제목을 설정하기 않았습니다.")
                } else {
                    binding.textviewNameTitle.text = pretreatmentAlarmTitle(editText.text.toString())
                    binding.switchName.isChecked = true
                    Log.d("|||text len", binding.textviewNameTitle.text.length.toString())
                }
            }
        }.show()
    }


    // ===== listener =====
    inner class ClickListener:View.OnClickListener{
        override fun onClick(v: View?) {
            binding.run {
                when(v?.id){
                    imageButtonBack.id -> finish()
                    buttonAlarmDelete.id -> finish()
                    buttonAlarmSave.id -> alarmSaveAction()
                    buttonTimeAll.id -> convWeekCheck(booleanArrayOf(true, true, true, true, true, true, true))
                    buttonTimeWeekday.id -> convWeekCheck(booleanArrayOf(false, true, true, true, true, true, false))
                    buttonTimeWeekend.id -> convWeekCheck(booleanArrayOf(true, false, false, false, false, false, true))
                    constraintNameSet.id -> setTitleAction()
                    constraintSoundSet.id -> soundSetAction()
                    constraintVibrationSet.id -> vibrationSetAction()
                    constraintRepeatSet.id -> repeatSetAction()
                    switchVibration.id -> vibrationSwitchAction()
                    switchRepeat.id -> repeatSwitchAction()
                    else -> clickDefaultAction(v)
                }
            }
        }
    }

    inner class CheckedChangeListener:CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            when{
                buttonView == null -> toast("error")
                buttonView.text == "일" && !isChecked -> buttonView.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                buttonView.text == "토" && !isChecked -> buttonView.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue))
                isChecked -> buttonView.setTextColor(ContextCompat.getColor(applicationContext, R.color.main))
                else -> buttonView.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            }
            binding.textviewTopWeek.text = convWeekInfo()
        }
    }

    inner class SeekBarChangeListener:SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            (getSystemService(AUDIO_SERVICE) as AudioManager).apply {
                setStreamVolume(STREAM_RING, progress, 0)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            try {
                if(selectedSound == null){
                    toast("설정된 소리가 없습니다.")
                    return
                }
                mediaPlayer = MediaPlayer()
                mediaPlayer.apply {
                    setDataSource(applicationContext, Uri.parse(selectedSound!!.uri))
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

    inner class TimeChangedListener:TimePicker.OnTimeChangedListener{
        override fun onTimeChanged(v: TimePicker?, hourOfDay: Int, minute: Int) {
            binding.textviewTopTime.text = convTimeToString(hourOfDay, minute)
        }
    }


    // ===== activity result =====
    private val startForSoundResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        when(result.resultCode){
            RESULT_OK -> {
                selectedSound = result.data?.getSerializableExtra("selectedSound") as Song
                if(selectedSound == null){
                    binding.switchSound.isChecked = false
                    Log.e("sound is null", "SetAlarmActivity/startForSoundResult")

                    toast("error")
                }else{
                    binding.apply {
                        textviewSoundTitle.text = selectedSound!!.title
                        switchSound.isChecked = true
                    }
                    toast("success")
                }
            }
            RESULT_CANCELED -> {
                Log.d("sound set canceled", "SetAlarmActivity/startForSoundResult")
            }
        }
    }

    private val startForVibrationResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        when(result.resultCode){
            RESULT_OK -> {
                selectedVibration = result.data?.getSerializableExtra("selectedVibration") as Vibration
                if(selectedVibration == null){
                    binding.switchVibration.isChecked = false
                    Log.e("vibration is null", "SetAlarmActivity/startForVibrationResult")
                    toast("error")
                }else{
                    binding.apply {
                        textviewVibrationTitle.text = selectedVibration!!.name
                        switchVibration.isChecked = true
                    }
                    toast("success")
                }
            }
            RESULT_CANCELED -> {
                Log.d("vibration set canceled", "SetAlarmActivity/startForVibrationResult")
            }
        }
    }

    // ===== extension function =====
    private fun String.toEditable() = Editable.Factory.getInstance().newEditable(this)

    // ===== test function =====
    private fun toast(s:String){
        val str = when(s){
            "success" -> getString(R.string.setting_complete)
            "error" -> getString(R.string.setting_error)
            else -> s
        }
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    // ===== getting ready =====
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