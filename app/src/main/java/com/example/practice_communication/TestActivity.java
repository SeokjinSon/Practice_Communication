package com.example.practice_communication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    // View Variable
    private EditText age, name, address;
    private Button btn_send;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        init();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init() {
        age     = findViewById(R.id.ageETXT);
        name    = findViewById(R.id.nameETXT);
        address = findViewById(R.id.addressETXT);
        btn_send = findViewById(R.id.btn_send);
    }

}
