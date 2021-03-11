package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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

        for(int i = 0; i < tabhost.getTabWidget().getChildCount(); i++){
            tabhost.getTabWidget().getChildAt(i).getLayoutParams().height = 50;
        }
        Log.v("tttt", Float.toString(dpToPx(50)) );

    }
    public int dpToPx(int sizeInDP){
        int pxVal = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources(), getDisplayMetrics()
        );
        return pxVal;
    }

    //dp를 px로 변환 (dp를 입력받아 px을 리턴)
    public float convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    //px을 dp로 변환 (px을 입력받아 dp를 리턴)
    public float convertPixelsToDp(float px){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

//    //dp를 px로 변환 (dp를 입력받아 px을 리턴)
//    public static float convertDpToPixel(float dp, Context context){
//        Resources resources = context.getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//        return px;
//    }
//
//    //px을 dp로 변환 (px을 입력받아 dp를 리턴)
//    public static float convertPixelsToDp(float px, Context context){
//        Resources resources = context.getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//        return dp;
//    }
}