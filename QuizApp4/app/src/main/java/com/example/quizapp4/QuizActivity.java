package com.example.quizapp4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class QuizActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use a container layout that holds the fragment
        setContentView(R.layout.activity_quiz_container);

        // Create an instance of QuizFragment
        QuizFragment quizFragment = new QuizFragment();

        // Pass the gallery list from the Intent to the fragment via arguments
        Bundle args = new Bundle();
        args.putSerializable("galleryList", getIntent().getSerializableExtra("galleryList"));
        quizFragment.setArguments(args);

        // Load the fragment into the container
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.quiz_fragment_container, quizFragment);
        transaction.commit();
    }
}
