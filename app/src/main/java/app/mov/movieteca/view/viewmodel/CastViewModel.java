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
import app.mov.movieteca.model.response.MovieCastsForPerson;
import app.mov.movieteca.model.response.Person;
import app.mov.movieteca.model.response.ShowCastsForPerson;
import app.mov.movieteca.repository.CastRepository;
import app.mov.movieteca.util.Constants;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CastViewModel extends ViewModel {

    private final MediatorLiveData<List<MovieCastsForPerson>> movies = new MediatorLiveData<>();
    private final MediatorLiveData<List<ShowCastsForPerson>> shows = new MediatorLiveData<>();
    private final MediatorLiveData<Person> person = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isFavorite = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private int count = 0;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject CastRepository castRepository;
    @Inject FavoritePreviewMediaDao favoritePreviewMediaDao;

    public CastViewModel() {
        Movieteca.getApplicationComponent().inject(this);
    }

    public MediatorLiveData<List<MovieCastsForPerson>> getMovies() {
        return movies;
    }

    public MediatorLiveData<List<ShowCastsForPerson>> getShows() {
        return shows;
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }

    public MediatorLiveData<Person> getPerson() {
        return person;
    }

    private void findFavorite(int personId) {
        new Thread(() -> {
            boolean favorite = favoritePreviewMediaDao.isFavorite(personId, Constants.ACTOR) != null;
            new Handler(Looper.getMainLooper()).post(() -> isFavorite.setValue(favorite));
        }).start();
    }

    public void addFavorite(FavoritePreviewMedia favoritePreviewMedia) {
        new Thread(() -> {
            favoritePreviewMediaDao.insertFavorite(favoritePreviewMedia);
            new Handler(Looper.getMainLooper()).post(() -> isFavorite.setValue(true));
        }).start();
    }
    public void removeFavorite(int personId) {
        new Thread(() -> {
            favoritePreviewMediaDao.removeFavoriteByQuery(personId, Constants.ACTOR);
            new Handler(Looper.getMainLooper()).post(() -> isFavorite.setValue(false));
        }).start();
    }

    private synchronized void onComplete() {
        count++;
        if (count == 3) {
            isLoading.setValue(false);
        }
    }

    public void start(int personId) {
        isLoading.setValue(true);
        getDetails(personId);
        getShows(personId);
        getMovies(personId);
        findFavorite(personId);
    }

    private void getDetails(int personId) {
        Disposable disposable = castRepository
                .getDetails(personId)
                .subscribe(personResponse -> {
                    person.setValue(personResponse);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void getShows(int personId) {
        Disposable disposable = castRepository
                .getTVCastsOfPerson(personId)
                .subscribe(personResponse -> {
                    shows.setValue(personResponse.getCast());
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void getMovies(int personId) {
        Disposable disposable = castRepository
                .getMovieCastsOfPerson(personId)
                .subscribe(personResponse -> {
                    movies.setValue(personResponse.getCast());
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
