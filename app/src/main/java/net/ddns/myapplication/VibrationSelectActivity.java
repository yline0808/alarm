package net.ddns.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import net.ddns.myapplication.adapter.VibrationListAdapter;
import net.ddns.myapplication.table.Vibration;

import java.util.ArrayList;

public class VibrationSelectActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    ImageButton imgBtnBack;
    ImageButton imgBtnSave;

    Intent intent;
    Vibrator vibrator;

    ArrayList<Vibration> vibrationList = new ArrayList<>();
    Vibration selectedVibration;
    VibrationListAdapter vibrationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration_select);

        pushVibrationType();
        findId();
        setVibrationRecyclerview();
        setListener();
    }

    private void pushVibrationType(){
        long basicVibrationTiming[] = {100, 1000, 900};
        vibrationList.add(new Vibration(getRStr(R.string.vibration_basic), basicVibrationTiming));
        long highVibrationTiming[] = {100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100,100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
        vibrationList.add(new Vibration(getRStr(R.string.vibration_high), highVibrationTiming));
        long staccatoVibrationTiming[] = {100, 100, 100, 500};
        vibrationList.add(new Vibration(getRStr(R.string.vibration_staccato), staccatoVibrationTiming));
        long heartVibrationTiming[] = {100, 100, 100, 100, 500, 100, 100, 100};
        vibrationList.add(new Vibration(getRStr(R.string.vibration_heart), heartVibrationTiming));
        long symphonyVibrationTiming[] = {100, 100, 100, 100, 100, 100, 100, 1000, 500, 100, 100, 100, 100, 100, 100, 1000};
        vibrationList.add(new Vibration(getRStr(R.string.vibration_symphony), symphonyVibrationTiming));
        long accentVibrationTiming[] = {100, 50, 200, 500};
        vibrationList.add(new Vibration(getRStr(R.string.vibration_accent), accentVibrationTiming));
        long sosVibrationTiming[] = {100, 200, 100, 200, 100, 200, 400, 500, 100, 500, 100, 500, 400, 200, 100, 200, 100, 200};
        vibrationList.add(new Vibration(getRStr(R.string.vibration_sos), sosVibrationTiming));
    }

    private String getRStr(int id){
        return getResources().getString(id);
    }

    private void findId(){
        searchView = (SearchView)findViewById(R.id.searchView);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewVibration);
        imgBtnBack = (ImageButton)findViewById(R.id.imgBtnBack);
        imgBtnSave = (ImageButton)findViewById(R.id.imgBtnVibrationSave);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }

    private void setVibrationRecyclerview(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        vibrationListAdapter = new VibrationListAdapter(vibrationList);

        recyclerView.setAdapter(vibrationListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void setListener(){
        searchView.setOnQueryTextListener(queryTextListener);
        vibrationListAdapter.setOnItemClickListener(itemClickListener);
        imgBtnSave.setOnClickListener(clickListener);
        imgBtnBack.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imgBtnVibrationSave:
                    vibrator.cancel();
                    intent = new Intent();
                    intent.putExtra("vibration", selectedVibration);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case R.id.imgBtnBack:
                    vibrator.cancel();
                    setResult(RESULT_CANCELED);
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
            vibrationListAdapter.getFilter().filter(newText);
            return false;
        }
    };

    VibrationListAdapter.OnItemClickListener itemClickListener = new VibrationListAdapter.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onItemClick(View v, int pos, Vibration vib) {
            vibrator.vibrate(VibrationEffect.createWaveform(vib.getTimimg(), -1));
            selectedVibration = vib;
            Log.d("vibration!!!", vib.toString());
        }
    };
}