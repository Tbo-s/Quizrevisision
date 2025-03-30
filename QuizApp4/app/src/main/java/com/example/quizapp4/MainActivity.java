package com.example.quizapp4;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // List to store gallery items for the RecyclerView
    ArrayList<gallerymodel> galleryModels = new ArrayList<>();

    // Predefined drawable image IDs
    int[] dogImages = {R.drawable.eagon, R.drawable.hank, R.drawable.lia};

    Button btnPickImage;
    Button sortButton;
    Button quizButton;
    Dog_recyclerviewadapter adapter;
    ActivityResultLauncher<Intent> resultLauncher;

    // Toggle for sorting order: true = A-Z, false = Z-A
    boolean sortAscending = true;

    // Room database instance
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("QUIZAPPMAIN", "onCreate started");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Adjust for system insets (edge-to-edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find views
        RecyclerView recyclerView = findViewById(R.id.rycyclerview);
        btnPickImage = findViewById(R.id.btnPickImage);
        sortButton = findViewById(R.id.Sortbutton);
        quizButton = findViewById(R.id.Quizbutton);

        // Initialize the Room database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "gallery-database").build();

        // Attempt to retrieve the gallery list from the Intent safely
        ArrayList<gallerymodel> intentGalleryList = getGalleryListFromIntent();
        if (intentGalleryList != null && !intentGalleryList.isEmpty()) {
            galleryModels = intentGalleryList;
        }

        // Load gallery items from the database on a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<GalleryEntity> galleryEntities = db.galleryDao().getAllGalleryItems();
                // If database is empty, insert default items
                if (galleryEntities.isEmpty()) {
                    String[] dogNames = getResources().getStringArray(R.array.Dog_full_names);
                    for (int i = 0; i < dogNames.length; i++) {
                        // Convert drawable resource to a URI string using the resource scheme
                        Uri resourceUri = Uri.parse("android.resource://" + getPackageName() + "/" + dogImages[i]);
                        GalleryEntity entity = new GalleryEntity(dogNames[i], resourceUri.toString());
                        db.galleryDao().insertGalleryItem(entity);
                    }
                    galleryEntities = db.galleryDao().getAllGalleryItems();
                }
                // Convert GalleryEntity objects into your model (gallerymodel)
                galleryModels.clear();
                for (GalleryEntity entity : galleryEntities) {
                    Uri uri = Uri.parse(entity.getImageUri());
                    galleryModels.add(new gallerymodel(entity.getNameOfDog(), uri));
                }
                // Update RecyclerView on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();

        // If the gallery list is still empty (no Intent extra and not yet loaded from Room),
        // load default items.
        if (galleryModels.isEmpty()) {
            setUpgalleryModels();
        }

        // Setup the RecyclerView adapter with the deletion callback
        adapter = new Dog_recyclerviewadapter(this, galleryModels, new Dog_recyclerviewadapter.OnItemDeleteListener() {
            @Override
            public void onDeleteItem(gallerymodel item, int position) {
                // Delete from the Room database on a background thread.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Use the helper DAO method to delete by name and URI.
                        db.galleryDao().deleteByNameAndUri(item.getNameOfDog(), item.getImageUri().toString());
                    }
                }).start();
                // Remove the item from the local list and notify the adapter.
                galleryModels.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, galleryModels.size() - position);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Register the image picker result launcher
        registerResult();

        // Set the button click listener to pick an image (requires API level R or higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            btnPickImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickImage();
                }
            });
        }

        // Sort button toggles between A-Z and Z-A sorting
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortGalleryModels();
            }
        });

        // Quiz button to start the quiz activity
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                quizIntent.putExtra("galleryList", galleryModels);
                startActivity(quizIntent);
            }
        });
    }

    /**
     * Helper method to safely retrieve the gallery list from the Intent extra.
     */
    private ArrayList<gallerymodel> getGalleryListFromIntent() {
        Serializable extra = getIntent().getSerializableExtra("galleryList");
        ArrayList<gallerymodel> images = new ArrayList<>();
        if (extra instanceof ArrayList<?>) {
            for (Object o : (ArrayList<?>) extra) {
                if (o instanceof gallerymodel) {
                    images.add((gallerymodel) o);
                }
            }
        }
        return images;
    }

    // Sorts the gallery list and updates the adapter
    private void sortGalleryModels() {
        if (sortAscending) {
            Collections.sort(galleryModels, new Comparator<gallerymodel>() {
                @Override
                public int compare(gallerymodel o1, gallerymodel o2) {
                    return o1.getNameOfDog().compareToIgnoreCase(o2.getNameOfDog());
                }
            });
        } else {
            Collections.sort(galleryModels, new Comparator<gallerymodel>() {
                @Override
                public int compare(gallerymodel o1, gallerymodel o2) {
                    return o2.getNameOfDog().compareToIgnoreCase(o1.getNameOfDog());
                }
            });
        }
        sortAscending = !sortAscending;
        adapter.notifyDataSetChanged();
    }

    // Launch the image picker (for API level R and higher)
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    // Registers the ActivityResultLauncher to receive the selected image and persists its URI permission.
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Uri imageUri = null;
                        try {
                            imageUri = result.getData().getData();
                            if (imageUri != null) {
                                // Persist the URI permission so the image remains accessible after a reboot.
                                int takeFlags = result.getData().getFlags() &
                                        (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            showNameDialog(imageUri);
                        } catch (SecurityException e) {
                            Log.d("QUIZAPPMAIN", e.getMessage());
                            Toast.makeText(MainActivity.this, "security issue", Toast.LENGTH_SHORT).show();
                            showNameDialog(imageUri);
                        } catch (Exception e) {
                            Log.d("QUIZAPPMAIN", e.getMessage());
                            Toast.makeText(MainActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    // Shows a dialog to ask the user for a name for the new image, then saves it
    private void showNameDialog(Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter a name for the image");

        final EditText input = new EditText(MainActivity.this);
        input.setHint("Name");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = input.getText().toString().trim();
                if (!name.isEmpty()) {
                    // Add the new image to the list and update the RecyclerView
                    galleryModels.add(new gallerymodel(name, imageUri));
                    adapter.notifyItemInserted(galleryModels.size() - 1);

                    // Insert the new item into the Room database on a background thread
                    GalleryEntity newEntity = new GalleryEntity(name, imageUri.toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.galleryDao().insertGalleryItem(newEntity);
                        }
                    }).start();
                } else {
                    Toast.makeText(MainActivity.this, "Name must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Inserts default items if no gallery items exist
    private void setUpgalleryModels() {
        String[] dogNames = getResources().getStringArray(R.array.Dog_full_names);
        for (int i = 0; i < dogNames.length; i++) {
            galleryModels.add(new gallerymodel(dogNames[i], dogImages[i]));
        }
    }
}
