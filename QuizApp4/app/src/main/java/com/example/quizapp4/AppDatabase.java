// AppDatabase.java
package com.example.quizapp4;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GalleryEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GalleryDao galleryDao();
}
