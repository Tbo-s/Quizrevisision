package com.example.quizrevision;

import androidx.lifecycle.LiveData;
import java.util.List;

public abstract class GalleryItemRepository {
    public abstract List<GalleryItem> getAll();
    public abstract LiveData<List<GalleryItem>> getAllLive();
    public abstract void insertAll(GalleryItem... items);
    public abstract void delete(GalleryItem item);
}
