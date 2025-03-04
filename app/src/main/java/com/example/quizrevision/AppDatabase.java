package com.example.quizrevision;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GalleryItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GalleryItemDao galleryItemDao();
}
