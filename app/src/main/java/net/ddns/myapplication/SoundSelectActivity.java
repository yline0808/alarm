package net.ddns.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import net.ddns.myapplication.adapter.SoundListAdapter;
import net.ddns.myapplication.table.Song;

import java.util.ArrayList;

public class SoundSelectActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    ImageButton imgBtnBack;
    ImageButton imgBtnSave;

    Intent intent;
    SoundListAdapter soundListAdapter;
    MediaPlayer mediaPlayer = new MediaPlayer();

    Song selectedSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_select);

        findId();
        setSoundRecyclerView(getNotifications());
        setListener();
    }

    private void findId(){
        searchView = (SearchView)findViewById(R.id.searchView);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewSound);
        imgBtnBack = (ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnSave = (ImageButton)findViewById(R.id.imgBtnSoundSave);
    }

    private void setListener(){
        searchView.setOnQueryTextListener(queryTextListener);
        soundListAdapter.setOnItemClickListener(itemClickListener);
        imgBtnBack.setOnClickListener(clickListener);
        imgBtnSave.setOnClickListener(clickListener);
    }

    private void setSoundRecyclerView(ArrayList<Song> defaultSoundList){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        soundListAdapter = new SoundListAdapter(defaultSoundList);

        recyclerView.setAdapter(soundListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
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

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgBtnBack:
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                    mediaPlayer.release();
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                case R.id.imgBtnSoundSave:
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                    mediaPlayer.release();
                    intent = new Intent();
                    intent.putExtra("song", selectedSong);
                    setResult(RESULT_OK, intent);
                    finish();
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

    SoundListAdapter.OnItemClickListener itemClickListener = new SoundListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int pos, Song s) {
            try{
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                }
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(s.getUri()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }catch(Exception e){
                mediaPlayer.pause();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
                Log.e("mediaPlay error", e.toString());
            }
            selectedSong = s;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}