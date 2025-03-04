package com.example.quizrevision;

import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);

        Log.i("QUIZ_APP", "Loaded gallery");

        Button button = findViewById(R.id.buttonGalleryExit);
        button.setOnClickListener(v -> {
            Log.i("QUIZ_APP", "Exiting gallery");
            finish();
        });

    }
}
