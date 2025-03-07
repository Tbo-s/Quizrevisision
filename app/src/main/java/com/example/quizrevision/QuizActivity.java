package com.example.quizrevision;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        Log.i("QUIZ_APP", "Loaded quiz");

        Button button = findViewById(R.id.buttonQuizExit);
        button.setOnClickListener(v -> {
            Log.i("QUIZ_APP", "Exiting quiz");
            finish();
        });

    }
}
