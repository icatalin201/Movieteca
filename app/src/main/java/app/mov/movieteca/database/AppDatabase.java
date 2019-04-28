package app.mov.movieteca.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.FavoritePreviewMediaDao;

@Database(entities = {FavoritePreviewMedia.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public abstract FavoritePreviewMediaDao getDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room
                    .databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "movieteca")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return appDatabase;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
