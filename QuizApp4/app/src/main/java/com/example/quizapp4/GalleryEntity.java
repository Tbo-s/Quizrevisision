
// GalleryEntity.java
package com.example.quizapp4;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "gallery")
public class GalleryEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nameOfDog;
    private String imageUri; // This will store a URI string for both resource and gallery images

    public GalleryEntity(String nameOfDog, String imageUri) {
        this.nameOfDog = nameOfDog;
        this.imageUri = imageUri;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNameOfDog() {
        return nameOfDog;
    }
    public String getImageUri() {
        return imageUri;
    }
}
