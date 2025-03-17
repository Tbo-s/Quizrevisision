package com.example.quizrevision;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {GalleryItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GalleryItemDao galleryItemDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "my-database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Voeg ingebouwde items toe bij de eerste creatie
            databaseWriteExecutor.execute(() -> {
                GalleryItemDao dao = INSTANCE.galleryItemDao();
                GalleryItem item1 = new GalleryItem();
                item1.name = "Eagon";
                item1.uri = "android.resource://com.example.quizrevision/drawable/eagon";
                GalleryItem item2 = new GalleryItem();
                item2.name = "Buddy";
                item2.uri = "android.resource://com.example.quizrevision/drawable/buddy";
                GalleryItem item3 = new GalleryItem();
                item3.name = "Charlie";
                item3.uri = "android.resource://com.example.quizrevision/drawable/charlie";
                dao.insertAll(item1, item2, item3);
            });
        }
    };
}
