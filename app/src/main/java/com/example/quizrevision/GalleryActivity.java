package com.example.quizrevision;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ext.SdkExtensions;
import android.provider.MediaStore;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresExtension;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    GalleryViewModel model;

    Button pickImageButton;
    ActivityResultLauncher<Intent> resultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);

        model = new ViewModelProvider(this).get(GalleryViewModel.class);
        final Observer<List<GalleryItem>> galleryObserver = new Observer<List<GalleryItem>>() {

            @Override
            public void onChanged(List<GalleryItem> galleryItems) {
                // todo update UI
            }
        };
        model.getImages().observe(this, galleryObserver);

        Log.i("QUIZ_APP", "Loaded gallery");

        Button button = findViewById(R.id.buttonGalleryExit);
        button.setOnClickListener(v -> {
            Log.i("QUIZ_APP", "Exiting gallery");
            finish();
        });


        registerResult();

        // Click listener for the button to pick an image
        if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
            pickImageButton.setOnClickListener(view -> pickImage());
        }


    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    // Registers the ActivityResultLauncher to receive the result of the image selection
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri imageUri = result.getData().getData();
                            showNameDialog(imageUri);
                        } catch (Exception e) {
                            Toast.makeText(GalleryActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    // Shows a dialog where the user can enter a name for the image
    private void showNameDialog(Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
        builder.setTitle("Enter a name for the image");

        final EditText input = new EditText(GalleryActivity.this);
        input.setHint("Name");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString().trim();
                if (!name.isEmpty()) {
//                    model.add(new gallerymodel(name, imageUri));
//                    adapter.notifyItemInserted(galleryModels.size() - 1);
                    // todo
                } else {
                    Toast.makeText(GalleryActivity.this, "Name must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
