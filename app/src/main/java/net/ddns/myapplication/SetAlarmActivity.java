package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

//        String test = (String) getIntent().getSerializableExtra("test");
//        Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();

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
        backBtn.setOnClickListener(backBtnClickListener);
        btnAlarmSave.setOnClickListener(btnAlarmSaveClickListener);
        tpSelectTime.setOnTimeChangedListener(tpSelectTimeTimeChangedListener);

        llAlarmTitle.setOnClickListener(llAlarmTitleClickListener);

        for(int i = 0; i < tbtnWeek.length; i++){
            tbtnWeek[i].setOnCheckedChangeListener(tbtnWeekCheckedChangeListener);
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

    //    ===Listener===
    View.OnClickListener backBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    TimePicker.OnTimeChangedListener tpSelectTimeTimeChangedListener = new TimePicker.OnTimeChangedListener(){
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            txtvTime.setText( convTimeToString(hourOfDay, minute) );
        }
    };

    CompoundButton.OnCheckedChangeListener tbtnWeekCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
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

    View.OnClickListener btnAlarmSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener llAlarmTitleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
                        txtvAlarmTitle.setText(editText.getText().toString());
                    }
                }
            });
            setTitleDialog.show();
        }
    };
}