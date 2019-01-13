package app.mov.movieteca.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.FavoritePreviewMediaDao;

@Database(entities = {FavoritePreviewMedia.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavoritePreviewMediaDao getDao();

}
