package net.ddns.myapplication;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.ddns.myapplication.fragment.TimeRepeatDialogFragment;
import net.ddns.myapplication.table.NormalAlarm;
import net.ddns.myapplication.table.Song;
import net.ddns.myapplication.table.TimeRepeat;
import net.ddns.myapplication.table.Vibration;

import java.sql.Date;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;

public class SetAlarmActivity extends AppCompatActivity implements TimeRepeatDialogFragment.OnCompleteListener {
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
    Button btnWeekday;
    Button btnWeekend;
    Button btnAllday;
    SeekBar seekBarSound;
    AlarmDatabase db;
    MediaPlayer mediaPlayer;
    Intent intent;

    Song selectedSong;
    Vibration selectedVibration;
    TimeRepeat selectedTimeRepeat;

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
        btnWeekday = (Button)findViewById(R.id.btnWeekday);
        btnWeekend = (Button)findViewById(R.id.btnWeekend);
        btnAllday = (Button)findViewById(R.id.btnAllday);
        seekBarSound = (SeekBar)findViewById(R.id.seekBarSound);

        seekBarSound.setMax(15);
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
        btnWeekday.setOnClickListener(clickListener);
        btnWeekend.setOnClickListener(clickListener);
        btnAllday.setOnClickListener(clickListener);
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

        weekInfo = isAllDay ? getRStr(R.string.all_day) : weekInfo;

        return weekInfo;
    }

    private void convWeekCheck(Boolean weekInfo[]){
        for(int i = 0; i < tbtnWeek.length; i++){
            tbtnWeek[i].setChecked(weekInfo[i]);
        }
    }

    private String getRStr(int id){
        return getResources().getString(id);
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

    @Override
    public void onInputedData(String minute, String count) {
        txtvAlarmAgain.setText(
                minute+"분, "+count + (count.equals(getRStr(R.string.repeat_infinite))? "":"회")
        );

        selectedTimeRepeat = new TimeRepeat(
                Integer.parseInt(minute),
                count.equals(getRStr(R.string.repeat_infinite))?0:Integer.parseInt(count)
        );
        switchAlarmAgain.setChecked(true);
    }

    //    ===Listener===
    View.OnClickListener clickListener = new View.OnClickListener() {
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
                    finish();
                    break;
                case R.id.btnWeekday:
                    convWeekCheck( new Boolean[] {false, true,true,true,true,true, false});
                    break;
                case R.id.btnWeekend:
                    convWeekCheck( new Boolean[] {true, false,false,false,false,false, true});
                    break;
                case R.id.btnAllday:
                    convWeekCheck( new Boolean[] {true, true,true,true,true,true, true});
                    break;
                case R.id.llAlarmTitle:
                    final EditText editText = new EditText(getApplicationContext());
                    editText.setText(txtvAlarmTitle.getText().toString().equals(getRStr(R.string.default_sel)) ? null : txtvAlarmTitle.getText().toString());
                    AlertDialog.Builder setTitleDialog = new AlertDialog.Builder(SetAlarmActivity.this);
                    setTitleDialog.setTitle(getRStr(R.string.alarm_title));
                    setTitleDialog.setView(editText);

                    setTitleDialog.setPositiveButton(getRStr(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(editText.getText().toString().equals("")){
                                txtvAlarmTitle.setText(getRStr(R.string.default_sel));
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
                    intent = new Intent(getApplicationContext(), VibrationSelectActivity.class);
                    startActivityForResult(intent, 3001);
                    break;
                case R.id.llAlarmAgain:
                    DialogFragment timeRepeatDialogFragment = new TimeRepeatDialogFragment();
                    timeRepeatDialogFragment.show(getSupportFragmentManager(),"dialog");

//                    intent = new Intent(getApplicationContext(), TimeRepeatActivity.class);
//                    startActivityForResult(intent, 3002);
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
            if(buttonView.getText().equals(getRStr(R.string.sun)) && !isChecked){
                buttonView.setTextColor(getResources().getColor(R.color.red));
            }else if(buttonView.getText().equals(getRStr(R.string.sat)) && !isChecked){
                buttonView.setTextColor(getResources().getColor(R.color.blue));
            }else if(isChecked){
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
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            switch(seekBar.getId()){
                case R.id.seekBarSound:
                    mediaPlayer = new MediaPlayer();

                    if(selectedSong == null){
                        Toast.makeText(getApplicationContext(), getRStr(R.string.sound_not_set_message), Toast.LENGTH_SHORT).show();
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
                        setVibrationSelect((Vibration)data.getSerializableExtra("vibration"));
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
                        setVibrationSelect(selectedVibration);
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
        txtvAlarmSound.setText(s != null? s.getTitle():getRStr(R.string.default_sel));
        switchAlarmSound.setChecked(s != null);
    }

    private void setVibrationSelect(Vibration vib){
        selectedVibration = vib;
        txtvAlarmVibration.setText(vib != null ? vib.getName():getRStr(R.string.default_sel));
        switchAlarmVibration.setChecked(vib != null);
    }
}