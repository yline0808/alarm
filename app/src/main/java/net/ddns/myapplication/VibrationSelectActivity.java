package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageButton;
import android.widget.SearchView;

public class VibrationSelectActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    ImageButton imgBtnBack;
    ImageButton imgBtnSave;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration_select);

        findId();

        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(VibrationEffect.);
    }

    private void findId(){
        searchView = (SearchView)findViewById(R.id.searchView);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewVibration);
        imgBtnBack = (ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnSave = (ImageButton)findViewById(R.id.imgBtnVibrationSave);
    }

    private void setListener(){

    }
}