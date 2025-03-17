package com.example.quizrevision;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GalleryViewModel extends ViewModel {
    private final MutableLiveData<GalleryUiState> uiState = new MutableLiveData<>();
    private final GalleryItemRepository repository;

    public GalleryViewModel(GalleryItemRepository repository) {
        this.repository = repository;
        // Gebruik de bestaande data (bijv. de getAll()-lijst) voor de initiële staat
        uiState.setValue(new GalleryUiState(repository.getAll()));
    }

    // Factory voor het verkrijgen van de ViewModel in de Activity
    public static final ViewModelProvider.Factory initializer = new ViewModelProvider.Factory() {
        @Override
        @SuppressWarnings("unchecked")
        public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
            MyApplication app = MyApplication.getContext();
            return (T) new GalleryViewModel(app.getGalleryItemRepository());
        }
    };

    public LiveData<GalleryUiState> getUiState() {
        return uiState;
    }

    public void sortAscending() {
        List<GalleryItem> sorted = Objects.requireNonNull(uiState.getValue()).getImages();
        Collections.sort(sorted, (o1, o2) -> o1.name.compareToIgnoreCase(o2.name));
        uiState.setValue(new GalleryUiState(sorted));
    }

    public void sortDescending() {
        List<GalleryItem> sorted = Objects.requireNonNull(uiState.getValue()).getImages();
        Collections.sort(sorted, (o1, o2) -> o2.name.compareToIgnoreCase(o1.name));
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
