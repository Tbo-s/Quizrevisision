package com.example.quizrevision;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GalleryUiState {
    private final List<GalleryItem> images;


    public GalleryUiState(List<GalleryItem> images) {
        this.images = images;
    }

    public List<GalleryItem> getImages() {
        return images;
    }
}
