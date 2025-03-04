package com.example.quizrevision;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);

        Log.i("QUIZ_APP", "Loaded gallery");

        Button button = (Button)findViewById(R.id.buttonGalleryExit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QUIZ_APP", "Exiting gallery");
                finish();
            }
        });

    }
}
