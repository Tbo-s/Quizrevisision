package com.example.quizrevision;

import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {
    AppDatabase db;

    AppDatabase getDb() {
        if (db == null) {
            db =
                    Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
        }
        return db;
    }
    GalleryItemRepository repository;
    public GalleryItemRepository getGalleryItemRepository() {
        if (repository == null) {
            repository = new SimpleGalleryItemRepository(this);
        }
        return repository;
    }
}
