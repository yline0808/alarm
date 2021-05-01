package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RadioButton;

import net.ddns.myapplication.adapter.SoundListAdapter;

public class SoundSelectActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SoundListAdapter soundListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_select);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewSound);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        soundListAdapter = new SoundListAdapter();

        for(int i = 0; i < 100; i++){
            String str = i+"번째 아이템";
            soundListAdapter.setArrayList(str);
        }

        recyclerView.setAdapter(soundListAdapter);


//                    Map<String, String> list = getNotifications();
//
//                    if(ringtone != null && ringtone.isPlaying()){
//                        Log.d("playing!!!ringtone", ringtone.isPlaying()+"");
//                        break;
//                    }
//
//                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                    Log.d("___test", notification.getPath());
//
//                    try{
//                        mediaPlayer.setDataSource(getApplicationContext(), notification);
//                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
//                        mediaPlayer.setLooping(true);
//                        mediaPlayer.prepare();
//                    }catch(Exception e){
//                        Log.e("media_error", e.toString());
//                    }
//
//                    ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
//                    Log.d("___testsound", ringtone.toString());
//                    ringtone.play();
    }
}