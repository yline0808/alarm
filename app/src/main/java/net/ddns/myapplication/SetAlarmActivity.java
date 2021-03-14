package net.ddns.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SetAlarmActivity extends AppCompatActivity {
    Button createAlarmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        String test = (String) getIntent().getSerializableExtra("test");
        Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();

        createAlarmBtn = (Button)findViewById(R.id.createAlarmBtn);
        createAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}