package com.example.quizrevision;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;
    private AppDatabase db;
    private GalleryItemRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = AppDatabase.getDatabase(getApplicationContext());
    }

    public static MyApplication getInstance() {
        return instance;
    }

    // Hiermee krijg je via de applicatie een repository
    public GalleryItemRepository getGalleryItemRepository() {
        if (repository == null) {
            repository = new SimpleGalleryItemRepository(this);
        }
        return repository;
    }

    // Voor gebruik in de ViewModel-initializer
    public static MyApplication getContext() {
        return instance;
    }
}
