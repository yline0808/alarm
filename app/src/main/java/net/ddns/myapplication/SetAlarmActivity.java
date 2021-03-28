package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.ddns.myapplication.table.NormalAlarm;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

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
    AlarmDatabase db;

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
        backBtn = (ImageButton)findViewById(R.id.img_btn_back);
        txtvTime = (TextView)findViewById(R.id.txtv_time);
        txtvWeek = (TextView)findViewById(R.id.txtv_week);
        txtvAlarmTitle = (TextView)findViewById(R.id.txtv_alarm_title);
        txtvAlarmSound = (TextView)findViewById(R.id.txtv_alarm_sound);
        txtvAlarmVibration = (TextView)findViewById(R.id.txtv_alarm_vibration);
        txtvAlarmAgain = (TextView)findViewById(R.id.txtv_alarm_again);
        tpSelectTime = (TimePicker)findViewById(R.id.tp_select_time);
        tbtnWeek[0] = (ToggleButton)findViewById(R.id.tbtn_sun);
        tbtnWeek[1] = (ToggleButton)findViewById(R.id.tbtn_mon);
        tbtnWeek[2] = (ToggleButton)findViewById(R.id.tbtn_tue);
        tbtnWeek[3] = (ToggleButton)findViewById(R.id.tbtn_wed);
        tbtnWeek[4] = (ToggleButton)findViewById(R.id.tbtn_thu);
        tbtnWeek[5] = (ToggleButton)findViewById(R.id.tbtn_fri);
        tbtnWeek[6] = (ToggleButton)findViewById(R.id.tbtn_sat);
        llAlarmTitle = (LinearLayout)findViewById(R.id.ll_alarm_title);
        llAlarmSound = (LinearLayout)findViewById(R.id.ll_alarm_sound);
        llAlarmVibration = (LinearLayout)findViewById(R.id.ll_alarm_vibration);
        llAlarmAgain = (LinearLayout)findViewById(R.id.ll_alarm_vibration);
        switchAlarmTitle = (Switch)findViewById(R.id.switch_alarm_title);
        switchAlarmSound = (Switch)findViewById(R.id.switch_alarm_sound);
        switchAlarmVibration = (Switch)findViewById(R.id.switch_alarm_vibration);
        switchAlarmAgain = (Switch)findViewById(R.id.switch_alarm_again);
        btnAlarmDelete = (Button)findViewById(R.id.btn_alarm_delete);
        btnAlarmSave = (Button)findViewById(R.id.btn_alarm_save);
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
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.img_btn_back:
                    finish();
                    break;
                case R.id.btn_alarm_delete:
                    finish();
                    break;
                case R.id.btn_alarm_save:
//                    //DB에 데이터 INSERT
//                    if(txtvAlarmTitle.getText().toString().trim().length() <= 0) {
//                        Toast.makeText(getApplicationContext(), "no title", Toast.LENGTH_SHORT).show();
//                    }else{
//                        new InsertAsyncTask (db.normalAlarmDao()).execute(new NormalAlarm(txtvAlarmTitle.getText().toString().trim(), "time", 1));
//                    }
                    break;
                case R.id.ll_alarm_title:
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
                            }
                        }
                    });
                    setTitleDialog.show();
                    break;
                case R.id.ll_alarm_sound:
//                    String[] allMp3Path = getAllMp3Path();
//                    String test = "";
//                    for(int i = 0; i < allMp3Path.length; i++){
//                        test += allMp3Path[i] + "\n";
//                    }
//                    Log.d("===mp3path", test);

                    break;
                case R.id.ll_alarm_vibration:

                    break;
                case R.id.ll_alarm_again:

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
}