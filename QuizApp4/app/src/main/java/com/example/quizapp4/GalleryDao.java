// GalleryDao.java
package com.example.quizapp4;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface GalleryDao {
    @Query("SELECT * FROM gallery")
    List<GalleryEntity> getAllGalleryItems();

    @Insert
    void insertGalleryItem(GalleryEntity galleryEntity);

    @Delete
    void deleteGalleryItem(GalleryEntity galleryEntity);

    // Added helper method to delete based on name and URI.
    @Query("DELETE FROM gallery WHERE nameOfDog = :name AND imageUri = :uri")
    void deleteByNameAndUri(String name, String uri);
}
