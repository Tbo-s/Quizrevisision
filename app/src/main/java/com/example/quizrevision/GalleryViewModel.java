package com.example.quizrevision;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import java.util.List;

public class GalleryViewModel extends ViewModel {
    private MutableLiveData<List<GalleryItem>> images;

    public MutableLiveData<List<GalleryItem>> getImages() {
        if (images == null) {
//            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "dataase-name").build();
//            GalleryItemDao galleryItemDao = db.galleryItemDao();
        }
        return images;
    }
}
