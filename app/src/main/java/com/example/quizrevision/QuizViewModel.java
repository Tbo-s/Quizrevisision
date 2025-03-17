package com.example.quizrevision;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizViewModel extends AndroidViewModel {
    private final GalleryItemRepository repository;
    private final LiveData<List<GalleryItem>> galleryItems;
    private GalleryItem currentItem;
    private List<String> currentAlternatives;
    private int score = 0;
    private int attempts = 0;
    private final Random random = new Random();

    public QuizViewModel(@NonNull Application application) {
        super(application);
        repository = ((MyApplication) application).getGalleryItemRepository();
        galleryItems = ((SimpleGalleryItemRepository) repository).getAllLive();
    }

    public void generateNewQuestion() {
        List<GalleryItem> items = galleryItems.getValue();
        if (items == null || items.size() < 3) {
            return;
        }
        currentItem = items.get(random.nextInt(items.size()));
        List<String> wrongNames = new ArrayList<>();
        for (GalleryItem item : items) {
            if (item.id != currentItem.id) {
                wrongNames.add(item.name);
            }
        }
        Collections.shuffle(wrongNames);
        List<String> alternatives = new ArrayList<>();
        alternatives.add(currentItem.name);
        alternatives.add(wrongNames.get(0));
        alternatives.add(wrongNames.get(1));
        Collections.shuffle(alternatives);
        currentAlternatives = alternatives;
    }

    public GalleryItem getCurrentItem() {
        return currentItem;
    }

    public List<String> getCurrentAlternatives() {
        return currentAlternatives;
    }

    public void submitAnswer(String answer) {
        attempts++;
        if (currentItem != null && answer.equals(currentItem.name)) {
            score++;
        }
    }

    public int getScore() {
        return score;
    }

    public int getAttempts() {
        return attempts;
    }
}
