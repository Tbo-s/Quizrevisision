package com.example.quizrevision;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.ext.SdkExtensions;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresExtension;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

public class GalleryActivity extends AppCompatActivity {
    GalleryViewModel model;
    DogRecycleViewAdapter adapter;
    Button pickImageButton, sortAsc, sortDesc;
    ActivityResultLauncher<Intent> resultLauncher;
    ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> Log.d("QUIZ_APP", "Permission result: " + result.toString())
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Vraag benodigde permissies
        if (Build.VERSION.SDK_INT <= 32) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }
        } else if (Build.VERSION.SDK_INT >= 34) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_DENIED) {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED});
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PERMISSION_DENIED) {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_IMAGES});
            }
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PERMISSION_DENIED) {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_IMAGES});
            }
        }

        setContentView(R.layout.activity_gallery);

        model = new ViewModelProvider(this, GalleryViewModel.initializer).get(GalleryViewModel.class);
        model.getUiState().observe(this, uiState -> adapter.notifyDataSetChanged());

        Button buttonExit = findViewById(R.id.buttonGalleryExit);
        buttonExit.setOnClickListener(v -> finish());

        pickImageButton = findViewById(R.id.btnPickImage);
        sortAsc = findViewById(R.id.btnSortAsc);
        sortDesc = findViewById(R.id.btnSortDesc);

        registerResult();
        if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
            pickImageButton.setOnClickListener(view -> pickImage());
        }
        sortAsc.setOnClickListener(v -> model.sortAscending());
        sortDesc.setOnClickListener(v -> model.sortDescending());

        RecyclerView recyclerView = findViewById(R.id.rycyclerview);
        adapter = new DogRecycleViewAdapter(model);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        Uri imageUri = result.getData().getData();
                        showNameDialog(imageUri);
                    } catch (Exception e) {
                        Toast.makeText(GalleryActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void showNameDialog(Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
        builder.setTitle("Enter a name for the image");
        final EditText input = new EditText(GalleryActivity.this);
        input.setHint("Name");
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getApplicationContext().getContentResolver().takePersistableUriPermission(imageUri, flag);
                GalleryItem item = new GalleryItem();
                item.name = name;
                item.uri = imageUri.toString();
                model.addItem(item);
            } else {
                Toast.makeText(GalleryActivity.this, "Name must not be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
