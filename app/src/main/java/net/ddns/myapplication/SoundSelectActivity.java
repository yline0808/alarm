package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import net.ddns.myapplication.adapter.SoundListAdapter;
import net.ddns.myapplication.table.Song;

import java.util.ArrayList;

public class SoundSelectActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    SoundListAdapter soundListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_select);

        findId();
        setListener();
        setSoundRecyclerView(getNotifications());
    }

    private void findId(){
        searchView = (SearchView)findViewById(R.id.searchView);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewSound);
    }

    private void setListener(){
        searchView.setOnQueryTextListener(queryTextListener);
    }

    private void setSoundRecyclerView(ArrayList<Song> defaultSoundList){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        soundListAdapter = new SoundListAdapter(defaultSoundList);

//        for(int i = 0; i < defaultSoundList.size(); i++){
//            soundListAdapter.setArrayList(defaultSoundList.get(i).getTitle());
//            Log.d("arrayList!!!", defaultSoundList.get(i).getTitle());
//        }

        recyclerView.setAdapter(soundListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

//        soundListAdapter.setOnClickListener((SoundListAdapter.onSongListener) this);
    }

    private ArrayList<Song> getNotifications() {
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();

        ArrayList<Song> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            Log.d("song!!!", notificationTitle + ":::" + notificationUri);
            Song song = new Song(notificationTitle, notificationUri);
            list.add(song);
        }

        return list;
    }

    //    private Map<String, String> findDefaultSoundList(){
//        Map<String, String> list = getNotifications();
//
//        if(ringtone != null && ringtone.isPlaying()){
//            Log.d("playing!!!ringtone", ringtone.isPlaying()+"");
//            break;
//        }
//
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        Log.d("___test", notification.getPath());
//
//        try{
//            mediaPlayer.setDataSource(getApplicationContext(), notification);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
//            mediaPlayer.setLooping(true);
//            mediaPlayer.prepare();
//        }catch(Exception e){
//            Log.e("media_error", e.toString());
//        }
//
//        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        Log.d("___testsound", ringtone.toString());
//        ringtone.play();
//
//        return list;
//    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.searchView:

                    break;
            }
        }
    };

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            soundListAdapter.getFilter().filter(newText);
            return false;
        }
    };
}