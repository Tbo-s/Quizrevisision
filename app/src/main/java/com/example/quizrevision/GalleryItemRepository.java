package com.example.quizrevision;

import androidx.lifecycle.LiveData;

import java.util.List;

public abstract class GalleryItemRepository {
    abstract public List<GalleryItem> getAll();

    abstract public void insertAll(GalleryItem... items);

    abstract public void delete(GalleryItem item);
}
