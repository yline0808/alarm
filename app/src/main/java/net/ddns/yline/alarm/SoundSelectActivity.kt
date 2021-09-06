package net.ddns.yline.alarm

import android.database.Cursor
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import net.ddns.yline.alarm.adapter.SoundAdapter
import net.ddns.yline.alarm.databinding.ActivitySoundSelectBinding
import net.ddns.yline.alarm.table.Song
import java.lang.Exception

class SoundSelectActivity : AppCompatActivity()  {
    private val binding by lazy { ActivitySoundSelectBinding.inflate(layoutInflater) }
    private lateinit var soundList:MutableList<Song>
    private val soundAdapter by lazy { SoundAdapter(soundList) }
    private var selectedSound:Song? = null
    private val mediaPlayer:MediaPlayer = MediaPlayer()

    // ===== overrid function =====
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getNotifications()
        selectedSound = intent.getSerializableExtra("selectedSound") as Song?
        setSoundRecyclerview()
        setListener()
    }


    // ===== function =====
    private fun setListener(){
        binding.apply {
            ClickListener().also {
                buttonBack.setOnClickListener(it)
                buttonSoundSave.setOnClickListener(it)
            }
        }
    }

    private fun setSoundRecyclerview(){
        val selectedIdx = soundList.indexOfFirst { it.title == selectedSound?.title }
        soundAdapter.apply {
            lastCheckedPos = if(selectedIdx == -1) 0 else selectedIdx
            setOnItemClickListener(ItemClickListener())
        }
        binding.recyclerviewSoundList.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewSoundList.adapter = soundAdapter
    }

    private fun getNotifications(){
        val sList:MutableList<Song> = mutableListOf()
        val manager = RingtoneManager(applicationContext)
        manager.setType(RingtoneManager.TYPE_RINGTONE)
        val cursor:Cursor = manager.cursor

        while (cursor.moveToNext()){
            sList.add(
                Song(
                    cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX),
                    cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
                )
            )
        }
        soundList = sList
    }

    private fun saveAction(){
        setResult(RESULT_OK, intent.putExtra("selectedSound", selectedSound))
        finish()
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
                    buttonSoundSave.id -> saveAction()
                    buttonBack.id -> backAction()
                }
            }
        }
    }

    inner class ItemClickListener:SoundAdapter.OnItemClickListener{
        override fun onItemClick(v: View, pos: Int, sound: Song) {

            if(mediaPlayer.isPlaying){
                mediaPlayer.reset()
            }


            try {
                mediaPlayer.apply {
                    setDataSource(applicationContext, Uri.parse(sound.uri))
                    setAudioStreamType(AudioManager.STREAM_RING)
                    isLooping = true
                    prepare()
                    start()
                }
            }catch (e:Exception){
                mediaPlayer.apply {
                    pause()
                    release()
                    Log.e("mediaplay error", "SoundSelectActivity/ItemClickListener")
                }
            }
            selectedSound = sound
        }
    }

    // ===== test =====
}