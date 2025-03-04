package com.example.quizrevision;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Log.i("QUIZ_APP", "Loaded main");

        Button buttonGallery = (Button)findViewById(R.id.buttonMainToGallery);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QUIZ_APP", "Moving from main to gallery");
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });

        Button buttonQuiz = (Button)findViewById(R.id.buttonMainToQuiz);
        buttonQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QUIZ_APP", "Moving from main to quiz");
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        Button buttonEnd = findViewById(R.id.buttonMainExit);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QUIZ_APP", "Exiting main");
                finish();
            }
        });

    }
}
