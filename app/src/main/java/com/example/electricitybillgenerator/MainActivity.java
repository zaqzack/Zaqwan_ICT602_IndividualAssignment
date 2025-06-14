package com.example.electricitybillgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonCalculate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalculateActivity.class);
            startActivity(intent);
        });

        Button buttonAbout = findViewById(R.id.buttonAbout);
        buttonAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

}

