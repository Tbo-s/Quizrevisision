package com.example.quizrevision;


import static android.content.pm.PackageManager.PERMISSION_DENIED;

import android.Manifest;
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
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


public class GalleryActivity extends AppCompatActivity {
    GalleryViewModel model;
    DogRecycleViewAdapter adapter;

    Button pickImageButton;
    Button sortAsc;
    Button sortDesc;

    ActivityResultLauncher<Intent> resultLauncher;
    ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), map -> {
            Log.d("QUIZ_APP", "something happened");
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

//        Uri imageUri = Uri.parse("android.resource://com.example.quizrevision/drawable/eagon.jpg");

//        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
//        getApplicationContext().getContentResolver().takePersistableUriPermission(imageUri, flag);
        if (Build.VERSION.SDK_INT <= 32) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
                Log.d("QUIZ_APP", "requesting permission 32");
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            } else {
                Log.d("QUIZ_APP", "permission granted 32");
            }
        } else if (Build.VERSION.SDK_INT >= 34) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_DENIED) {
                Log.d("QUIZ_APP", "requesting permission 34");
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED});
            } else {
                Log.d("QUIZ_APP", "permission granted 34");
            }
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PERMISSION_DENIED) {
                Log.d("QUIZ_APP", "requesting permission 34 images");
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_IMAGES});
            } else {
                Log.d("QUIZ_APP", "permission granted 34 images");
            }
        } else {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PERMISSION_DENIED) {
                Log.d("QUIZ_APP", "requesting permission");
                requestPermissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_IMAGES});
            } else {
                Log.d("QUIZ_APP", "permission granted");
            }
        }
        setContentView(R.layout.activity_gallery);
        model = new ViewModelProvider(
                this,
                ViewModelProvider.Factory.from(GalleryViewModel.initializer)
        ).get(GalleryViewModel.class);

        final Observer<GalleryUiState> galleryObserver = uiState -> {
            // todo update UI
            adapter.notifyDataSetChanged();
        };
        model.getUiState().observe(this, galleryObserver);

        Log.i("QUIZ_APP", "Loaded gallery");

        Button button = findViewById(R.id.buttonGalleryExit);
        button.setOnClickListener(v -> {
            Log.i("QUIZ_APP", "Exiting gallery");
            finish();
        });

        pickImageButton = findViewById(R.id.btnPickImage);
        sortAsc = findViewById(R.id.btnSortAsc);
        sortDesc = findViewById(R.id.btnSortDesc);

        registerResult();

        // Click listener for the button to pick an image
        if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
            pickImageButton.setOnClickListener(view -> pickImage());
        }

        sortAsc.setOnClickListener(v -> {
            model.sortAscending();
        });
        sortDesc.setOnClickListener(v -> {
            model.sortDescending();
        });

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

    // Registers the ActivityResultLauncher to receive the result of the image selection
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
                    int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    getApplicationContext().getContentResolver().takePersistableUriPermission(imageUri, flag);
                    GalleryItem item = new GalleryItem();
                    item.name = name;
                    item.uri = imageUri.toString();
                    model.addItem(item);
//                    adapter.notifyItemInserted();
                } else {
                    Toast.makeText(GalleryActivity.this, "Name must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
