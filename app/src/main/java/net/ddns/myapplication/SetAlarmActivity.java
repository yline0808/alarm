package net.ddns.myapplication;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.ddns.myapplication.table.NormalAlarm;
import net.ddns.myapplication.table.Song;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetAlarmActivity extends AppCompatActivity {
    ImageButton backBtn;
    TextView txtvTime;
    TextView txtvWeek;
    TextView txtvAlarmTitle;
    TextView txtvAlarmSound;
    TextView txtvAlarmVibration;
    TextView txtvAlarmAgain;
    TimePicker tpSelectTime;
    ToggleButton[] tbtnWeek = new ToggleButton[7];
    LinearLayout llAlarmTitle;
    LinearLayout llAlarmSound;
    LinearLayout llAlarmVibration;
    LinearLayout llAlarmAgain;
    Switch switchAlarmTitle;
    Switch switchAlarmSound;
    Switch switchAlarmVibration;
    Switch switchAlarmAgain;
    Button btnAlarmDelete;
    Button btnAlarmSave;
    SeekBar seekBarSound;
    SeekBar seekBarVibration;
    AlarmDatabase db;
    MediaPlayer mediaPlayer;
    Intent intent;
    Vibrator vibrator;

    Song selectedSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        db = AlarmDatabase.getAppDatabase(this);

//        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
//        db.normalAlarmDao().getAll().observe(this, new Observer<List<NormalAlarm>>() {
//            @Override
//            public void onChanged(List<NormalAlarm> normalAlarms) {
//                view.setText(normalAlarms.toString());
//            }
//        });

        findId();
        setDefaultTime();
        setListener();
    }

    private void findId(){
        backBtn = (ImageButton)findViewById(R.id.imgBtnBack);
        txtvTime = (TextView)findViewById(R.id.txtvTime);
        txtvWeek = (TextView)findViewById(R.id.txtvWeek);
        txtvAlarmTitle = (TextView)findViewById(R.id.txtvAlarmTitle);
        txtvAlarmSound = (TextView)findViewById(R.id.txtvAlarmSound);
        txtvAlarmVibration = (TextView)findViewById(R.id.txtvAlarmVibration);
        txtvAlarmAgain = (TextView)findViewById(R.id.txtvAlarmAgain);
        tpSelectTime = (TimePicker)findViewById(R.id.tpSelectTime);
        tbtnWeek[0] = (ToggleButton)findViewById(R.id.tbtnSun);
        tbtnWeek[1] = (ToggleButton)findViewById(R.id.tbtnMon);
        tbtnWeek[2] = (ToggleButton)findViewById(R.id.tbtnTue);
        tbtnWeek[3] = (ToggleButton)findViewById(R.id.tbtnWed);
        tbtnWeek[4] = (ToggleButton)findViewById(R.id.tbtnThu);
        tbtnWeek[5] = (ToggleButton)findViewById(R.id.tbtnFri);
        tbtnWeek[6] = (ToggleButton)findViewById(R.id.tbtnSat);
        llAlarmTitle = (LinearLayout)findViewById(R.id.llAlarmTitle);
        llAlarmSound = (LinearLayout)findViewById(R.id.llAlarmSound);
        llAlarmVibration = (LinearLayout)findViewById(R.id.llAlarmVibration);
        llAlarmAgain = (LinearLayout)findViewById(R.id.llAlarmAgain);
        switchAlarmTitle = (Switch)findViewById(R.id.switchAlarmTitle);
        switchAlarmSound = (Switch)findViewById(R.id.switchAlarmSound);
        switchAlarmVibration = (Switch)findViewById(R.id.switchAlarmVibration);
        switchAlarmAgain = (Switch)findViewById(R.id.switchAlarmAgain);
        btnAlarmDelete = (Button)findViewById(R.id.btnAlarmDelete);
        btnAlarmSave = (Button)findViewById(R.id.btnAlarmSave);
        seekBarSound = (SeekBar)findViewById(R.id.seekBarSound);
        seekBarVibration = (SeekBar)findViewById(R.id.seekBarVibration);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        seekBarSound.setMax(15);
        seekBarVibration.setMax(15);
    }

    private void setDefaultTime(){
        long sNow = System.currentTimeMillis();
        Date dNow = new Date(sNow);
        Format formatter = new SimpleDateFormat("H:mm");
        String now = formatter.format(dNow);
        int h = Integer.parseInt(now.split(":")[0]);
        int m = Integer.parseInt(now.split(":")[1]);

        tpSelectTime.setCurrentHour(h);
        tpSelectTime.setCurrentMinute(m);
        txtvTime.setText(convTimeToString(tpSelectTime.getCurrentHour(), tpSelectTime.getCurrentMinute()));
        txtvWeek.setText(convWeekInfo());
    }

    private void setListener(){
        backBtn.setOnClickListener(clickListener);
        btnAlarmSave.setOnClickListener(clickListener);
        btnAlarmDelete.setOnClickListener(clickListener);
        llAlarmTitle.setOnClickListener(clickListener);
        llAlarmSound.setOnClickListener(clickListener);
        llAlarmVibration.setOnClickListener(clickListener);
        llAlarmAgain.setOnClickListener(clickListener);
        tpSelectTime.setOnTimeChangedListener(timeChangedListener);
        seekBarSound.setOnSeekBarChangeListener(seekBarChangeListener);

        for(int i = 0; i < tbtnWeek.length; i++){
            tbtnWeek[i].setOnCheckedChangeListener(checkedChangeListener);
        }
    }

    private String convTimeToString(int hourOfDay, int minute){
        Time t = new Time(hourOfDay, minute, 0);
        Format formatter = new SimpleDateFormat("a h:mm");

        return formatter.format(t);
    }

    private String convWeekInfo(){
        String weekInfo = "";
        Boolean isAllDay = true;

        for(int i = 0; i < tbtnWeek.length; i++){
            if(tbtnWeek[i].isChecked()){
                weekInfo += tbtnWeek[i].getText();
            }else{
                isAllDay = false;
            }
        }

        weekInfo = isAllDay ? getResources().getString(R.string.all_day) : weekInfo;

        return weekInfo;
    }

    private String[] getAllMp3Path(){
        String[] resultPath = null;
        // 외장 메모리 접근 권한을 가지고 있는지 확인. ( Marshmallow 이상 )  // mAcitivity == Main Activity
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
            // 찾고자하는 파일 확장자명.
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");

            String[] selectionArgsMp3 = new String[]{mimeType};

            Cursor c = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Media.DATA}, selectionMimeType, selectionArgsMp3, null);

            if (c.getCount() == 0)
                return null;

            resultPath = new String[c.getCount()];
            while (c.moveToNext()) {
                // 경로 데이터 셋팅.
                resultPath[c.getPosition()] = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Log.d("===TAG", resultPath[c.getPosition()]);
            }
        }
        return resultPath;
    }

    //    ===Listener===
    View.OnClickListener clickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imgBtnBack:
                    finish();
                    break;
                case R.id.btnAlarmDelete:
                    finish();
                    break;
                case R.id.btnAlarmSave:
//                    //DB에 데이터 INSERT
//                    if(txtvAlarmTitle.getText().toString().trim().length() <= 0) {
//                        Toast.makeText(getApplicationContext(), "no title", Toast.LENGTH_SHORT).show();
//                    }else{
//                        new InsertAsyncTask (db.normalAlarmDao()).execute(new NormalAlarm(txtvAlarmTitle.getText().toString().trim(), "time", 1));
//                    }
                    break;
                case R.id.llAlarmTitle:
                    final EditText editText = new EditText(getApplicationContext());
                    editText.setText(txtvAlarmTitle.getText().toString().equals(getResources().getString(R.string.default_sel)) ? null : txtvAlarmTitle.getText().toString());
                    AlertDialog.Builder setTitleDialog = new AlertDialog.Builder(SetAlarmActivity.this);
                    setTitleDialog.setTitle(getResources().getString(R.string.alarm_title));
                    setTitleDialog.setView(editText);
                    setTitleDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(editText.getText().toString().equals("")){
                                txtvAlarmTitle.setText(getResources().getString(R.string.default_sel));
                                switchAlarmTitle.setChecked(false);
                                Toast.makeText(getApplicationContext(), "알람 제목을 설정하기 않았습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                new InsertAsyncTask(db.normalAlarmDao()).execute(
                                        new NormalAlarm(
                                                editText.getText().toString(),
                                                convTimeToString(tpSelectTime.getCurrentHour(), tpSelectTime.getCurrentMinute()),
                                                11
                                        )
                                );

                                txtvAlarmTitle.setText(editText.getText().toString());
                                switchAlarmTitle.setChecked(true);
                            }
                        }
                    });
                    setTitleDialog.show();
                    break;
                case R.id.llAlarmSound:
                    intent = new Intent(getApplicationContext(), SoundSelectActivity.class);
                    startActivityForResult(intent, 3000);
                    break;
                case R.id.llAlarmVibration:
//                    vibrator.vibrate(500);

                    long timings[] = {100, 100, 0,400,0,200,0,400};
                    int amplitudes[] = {0, 50, 0, 100, 0, 50, 0, 150};

                    long timings2[] = {1000, 500, 1000, 1000};
                    vibrator.vibrate(VibrationEffect.createWaveform(timings2, 0));
                    break;
                case R.id.llAlarmAgain:
                    vibrator.cancel();
                    break;
            }
        }
    };

    TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener(){
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            txtvTime.setText( convTimeToString(hourOfDay, minute) );
        }
    };

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getText().equals(getResources().getString(R.string.sun)) && !isChecked){
                buttonView.setTextColor(getResources().getColor(R.color.red));
            }else if(buttonView.getText().equals(getResources().getString(R.string.sat)) && !isChecked){
                buttonView.setTextColor(getResources().getColor(R.color.blue));
            }
            else if(isChecked){
                buttonView.setTextColor(getResources().getColor(R.color.main));
            }else{
                buttonView.setTextColor(getResources().getColor(R.color.black));
            }

            txtvWeek.setText(convWeekInfo());
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch(seekBar.getId()){
                case R.id.seekBarSound:
                    AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
                    break;
                case R.id.seekBarVibration:

                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            switch(seekBar.getId()){
                case R.id.seekBarSound:
                    mediaPlayer = new MediaPlayer();

                    if(selectedSong == null){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.sound_not_set_message), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    try{
                        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(selectedSong.getUri()));
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }catch(Exception e){
                        mediaPlayer.pause();
                        mediaPlayer.release();
                        mediaPlayer = new MediaPlayer();
                        Log.e("seekbar error", e.toString());
                    }
                    break;
                case R.id.seekBarVibration:

                    break;
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            switch(seekBar.getId()){
                case R.id.seekBarSound:
                    if(mediaPlayer != null && mediaPlayer.isPlaying()){
                        Log.d("ring play!!!", "");
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                    mediaPlayer.release();
                    break;
                case R.id.seekBarVibration:

                    break;
            }
        }
    };


//    ===ActivityResult===
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode){
            case RESULT_OK:
                switch(requestCode){
                    case 3000:
                        setSoundSelect((Song)data.getSerializableExtra("song"));
                        break;
                    case 3001:

                        break;
                    default:

                        break;
                }
                break;
            case RESULT_CANCELED:
                switch (requestCode){
                    case 3000:
                        setSoundSelect(selectedSong);
                        break;
                    case 3001:

                        break;
                    default:
                        break;
                }
                break;
            default:
                switch(requestCode){
                    case 3000:
                        Log.e("requesCode:3000", "sound select error");
                        break;
                    case 3001:
                        Log.e("requesCode:3001", "vibration select error");
                        break;
                    default:

                        break;
                }
                break;
        }
    }

    private void setSoundSelect(Song s){
        selectedSong = s;
        txtvAlarmSound.setText(s != null? s.getTitle():getResources().getString(R.string.default_sel));
        switchAlarmSound.setChecked(s != null);
    }

//    lifecycle

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
    }
}