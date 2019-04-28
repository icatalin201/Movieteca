package app.mov.movieteca.view.viewmodel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.FavoritePreviewMediaDao;

public class FavoriteViewModel extends ViewModel {

    private final MediatorLiveData<List<FavoritePreviewMedia>> data = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    @Inject FavoritePreviewMediaDao favoritePreviewMediaDao;

    public FavoriteViewModel() {
        Movieteca.getApplicationComponent().inject(this);
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<List<FavoritePreviewMedia>> getData() {
        return data;
    }

    public void getFavorites(String type) {
        isLoading.setValue(true);
        new Thread(() -> {
            List<FavoritePreviewMedia> list = favoritePreviewMediaDao.getFavorites(type);
            new Handler(Looper.getMainLooper()).post(() -> {
                data.setValue(list);
                isLoading.setValue(false);
            });
        }).start();
    }
}
