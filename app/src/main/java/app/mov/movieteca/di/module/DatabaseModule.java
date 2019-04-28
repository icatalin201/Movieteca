package app.mov.movieteca.di.module;

import android.content.Context;

import javax.inject.Singleton;

import app.mov.movieteca.database.AppDatabase;
import app.mov.movieteca.model.FavoritePreviewMediaDao;
import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public FavoritePreviewMediaDao getFavoriteDao(AppDatabase appDatabase) {
        return appDatabase.getDao();
    }

    @Provides
    @Singleton
    public AppDatabase getAppDatabase(Context context) {
        return AppDatabase.getInstance(context);
    }
}
