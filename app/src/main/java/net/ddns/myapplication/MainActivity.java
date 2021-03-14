package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    TabHost tabhost;
    ImageButton imgbtn;
    String testTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findId();
        setTabhost();

//        add alarm
        imgbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), SetAlarmActivity.class);
                intent.putExtra("test", "valuetest");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void findId(){
        tabhost = (TabHost) findViewById(R.id.tabhost);
        imgbtn = (ImageButton) findViewById(R.id.addAlarm);
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
}