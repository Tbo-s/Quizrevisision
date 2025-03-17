package com.example.quizrevision;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class SimpleGalleryItemRepository extends GalleryItemRepository {

    private final GalleryItemDao dao;

    public SimpleGalleryItemRepository(Application application) {
        dao = AppDatabase.getDatabase(application).galleryItemDao();
    }

    @Override
    public List<GalleryItem> getAll() {
        return dao.getAll();
    }

    @Override
    public LiveData<List<GalleryItem>> getAllLive() {
        return dao.getAllLive();
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
