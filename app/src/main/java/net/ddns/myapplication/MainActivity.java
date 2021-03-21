package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import net.ddns.myapplication.dao.NormalAlarmDao;
import net.ddns.myapplication.table.NormalAlarm;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    TabHost tabhost;
    ImageButton imgbtnAddAlarm;
    String testTitle;

    private static final int ACTIVITY_RESULT_OK = 0;
    private static final int ACTIVITY_RESULT_FAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AlarmDatabase db = AlarmDatabase.getAppDatabase(this);

//        //UI 갱신 (라이브데이터 Observer 이용, 해당 디비값이 변화가생기면 실행됨)
//        db.normalAlarmDao().getAll().observe(this, new Observer<List<NormalAlarm>>() {
//            @Override
//            public void onChanged(List<NormalAlarm> normalAlarms) {
//                view.setText(normalAlarms.toString());
//            }
//        });

//        //DB 데이터 불러오기 (SELECT)
//        String sel = db.normalAlarmDao().getAll().toString();
//
//        //DB에 데이터 INSERT
//        if(mTodoEditText.getText().toString().trim().length() <= 0) {
//            Toast.makeText(MainActivity.this, "한글자 이상입력해주세요.", Toast.LENGTH_SHORT).show();
//        }else{
//            new InsertAsyncTask (db.normalAlarmDao()).execute(new NormalAlarm("name", "time", 1));
//        }

        findId();
        setTabhost();
        setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void findId(){
        tabhost = (TabHost) findViewById(R.id.tabhost);
        imgbtnAddAlarm = (ImageButton) findViewById(R.id.addAlarm);
    }

    private void setTabhost(){
        tabhost.setup();

        TabHost.TabSpec ts1 = tabhost.newTabSpec(getString(R.string.tab1));
        ts1.setContent(R.id.tab1);
        ts1.setIndicator(getString(R.string.tab1));
        tabhost.addTab(ts1);

        TabHost.TabSpec ts2 = tabhost.newTabSpec(getString(R.string.tab2));
        ts2.setContent(R.id.tab2);
        ts2.setIndicator(getString(R.string.tab2));
        tabhost.addTab(ts2);

        TabHost.TabSpec ts3 = tabhost.newTabSpec(getString(R.string.tab3));
        ts3.setContent(R.id.tab3);
        ts3.setIndicator(getString(R.string.tab3));
        tabhost.addTab(ts3);

        for(int i = 0; i < tabhost.getTabWidget().getChildCount(); i++){
            tabhost.getTabWidget().getChildAt(i).getLayoutParams().height = 100;
        }
    }

    private void setListener(){
        imgbtnAddAlarm.setOnClickListener(addAlarmClickListener);
    }

    //    ===Listener===
    View.OnClickListener addAlarmClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            intent = new Intent(getApplicationContext(), SetAlarmActivity.class);
            startActivityForResult(intent, ACTIVITY_RESULT_OK);
        }
    };
}
