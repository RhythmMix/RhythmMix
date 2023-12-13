package com.example.rhythmix;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Log_In_Page extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        TextView clickableTextView = findViewById(R.id.textView5);
        Button gradientButton = findViewById(R.id.button5);

        clickableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log_In_Page.this, Sign_Up_Page.class);
                startActivity(intent);
            }
        });
    }
        // Set the GradientDrawable as the background for the button


}