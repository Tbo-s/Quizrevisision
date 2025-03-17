package com.example.quizrevision;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface GalleryItemDao {
    @Query("SELECT * FROM GalleryItem")
    LiveData<List<GalleryItem>> getAllLive();

    @Query("SELECT * FROM GalleryItem")
    List<GalleryItem> getAll();

    @Insert
    void insertAll(GalleryItem... items);

    @Delete
    void delete(GalleryItem item);
}
