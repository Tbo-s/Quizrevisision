package com.example.quizrevision;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class QuizActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        Log.i("QUIZ_APP", "Loaded quiz");

        if (savedInstanceState == null) {
            QuizFragment quizFragment = new QuizFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.quiz_fragment_container, quizFragment);
            ft.commit();
        }

        Button exitButton = findViewById(R.id.buttonQuizExit);
        exitButton.setOnClickListener(v -> finish());
    }
}
