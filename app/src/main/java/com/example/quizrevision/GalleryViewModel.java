package com.example.quizrevision;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class GalleryViewModel extends ViewModel {
    private final MutableLiveData<GalleryUiState> uiState = new MutableLiveData<>();
    private final GalleryItemRepository repository;

    public GalleryViewModel(GalleryItemRepository repository) {
        this.repository = repository;
        uiState.setValue(new GalleryUiState(repository.getAll()));
    }

    static final ViewModelInitializer<GalleryViewModel> initializer = new ViewModelInitializer<GalleryViewModel>(
            GalleryViewModel.class,
            creationExtras -> {
                MyApplication app = (MyApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new GalleryViewModel(app.getGalleryItemRepository());
            }
    );

    public LiveData<GalleryUiState> getUiState() {
        return uiState;
    }

    public void sortAscending() {
        List<GalleryItem> sorted = Objects.requireNonNull(uiState.getValue()).getImages();
        sorted.sort((o1, o2) -> o1.name.compareToIgnoreCase(o2.name));
        uiState.setValue(new GalleryUiState(sorted));
    }

    public void sortDescending() {
        List<GalleryItem> sorted = Objects.requireNonNull(uiState.getValue()).getImages();
        sorted.sort((o1, o2) -> o2.name.compareToIgnoreCase(o1.name));
        uiState.setValue(new GalleryUiState(sorted));
    }

    public void addItem(GalleryItem item) {
        List<GalleryItem> updated = Objects.requireNonNull(uiState.getValue()).getImages();
        updated.add(item);
        uiState.setValue(new GalleryUiState(updated));
        repository.insertAll(item);
    }

    public void removeItem(int position) {
        List<GalleryItem> updated = Objects.requireNonNull(uiState.getValue()).getImages();
        GalleryItem removed = updated.remove(position);
        uiState.setValue(new GalleryUiState(updated));
        repository.delete(removed);
    }
}
