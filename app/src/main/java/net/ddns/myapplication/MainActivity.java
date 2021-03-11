package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    TabHost tabhost;
    String testTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabhost = (TabHost) findViewById(R.id.tabhost);
        tabhost.setup();

        TabHost.TabSpec ts1 = tabhost.newTabSpec("TAB 1");
        ts1.setContent(R.id.tab1);
        ts1.setIndicator("tab 1");
        tabhost.addTab(ts1);

        TabHost.TabSpec ts2 = tabhost.newTabSpec("TAB 2");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("tab 2");
        tabhost.addTab(ts2);

        TabHost.TabSpec ts3 = tabhost.newTabSpec("TAB 3");
        ts3.setContent(R.id.tab3);
        ts3.setIndicator("tab 3");
        tabhost.addTab(ts3);
    }
}