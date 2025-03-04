package com.example.quizrevision;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GalleryItemDao {
    @Query("SELECT * FROM GalleryItem")
    List<GalleryItem> getAll();

    @Query("SELECT * FROM GalleryItem WHERE name LIKE :name LIMIT 1")
    GalleryItem findByName(String name);

    @Insert
    void insertAll(GalleryItem... items);

    @Delete
    void delete(GalleryItem item);
}
