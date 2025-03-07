package com.example.quizrevision;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class GalleryItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String uri;

    public String name;

    @Ignore
    public Bitmap image;
}
