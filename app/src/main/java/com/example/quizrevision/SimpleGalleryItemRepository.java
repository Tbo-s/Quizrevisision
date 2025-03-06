package com.example.quizrevision;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.Collections;
import java.util.List;

public class SimpleGalleryItemRepository extends GalleryItemRepository {

    public SimpleGalleryItemRepository(Application application) {
        this.dao = AppDatabase.getDatabase(application).galleryItemDao();
    }

    GalleryItemDao dao;

    @Override
    public List<GalleryItem> getAll() {
        return dao.getAll();
    }


    @Override
    public void insertAll(GalleryItem... items) {
        dao.insertAll(items);
    }

    @Override
    public void delete(GalleryItem item) {
        dao.delete(item);
    }
}
