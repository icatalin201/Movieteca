package app.mov.movieteca.view.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.response.BaseMovieResponse;
import app.mov.movieteca.model.response.Genre;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.repository.GenreRepository;
import app.mov.movieteca.repository.MovieRepository;
import app.mov.movieteca.util.Utils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MoviesViewModel extends ViewModel {

    private final MediatorLiveData<List<PreviewMovie>> popular = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewMovie>> upcoming = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewMovie>> topRated = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewMovie>> nowPlaying = new MediatorLiveData<>();
    private final MediatorLiveData<List<Genre>> genres = new MediatorLiveData<>();
    private final MediatorLiveData<PreviewMovie> mainItem = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int count = 0;

    @Inject MovieRepository movieRepository;
    @Inject GenreRepository genreRepository;

    public LiveData<List<PreviewMovie>> getNowPlaying() {
        return nowPlaying;
    }

    public LiveData<List<PreviewMovie>> getPopular() {
        return popular;
    }

    public LiveData<List<PreviewMovie>> getTopRated() {
        return topRated;
    }

    public LiveData<List<PreviewMovie>> getUpcoming() {
        return upcoming;
    }

    public LiveData<PreviewMovie> getMainItem() {
        return mainItem;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<Genre>> getGenres() {
        return genres;
    }

    public MoviesViewModel() {
        Movieteca.getApplicationComponent().inject(this);
        isLoading.setValue(true);
    }

    public void start(Integer page, String region) {
        findNowPlayingMovies(page, region);
        findPopularMovies(page, region);
        findTopRatedMovies(page, region);
        findUpcomingMovies(page, region);
        findGenres();
    }

    private synchronized void onComplete() {
        count++;
        if (count == 5) {
            isLoading.setValue(false);
        }
    }
    private boolean filterList(List<PreviewMovie> previewMovies) {
        ListIterator<PreviewMovie> previewMovieListIterator = previewMovies.listIterator();
        while (previewMovieListIterator.hasNext()) {
            PreviewMovie previewMovie = previewMovieListIterator.next();
            if (previewMovie.getPosterPath() == null) {
                previewMovieListIterator.remove();
            }
        }
        return true;
    }

    private void findNowPlayingMovies(Integer page, String region) {
        Disposable disposable = movieRepository
                .findNowPlayingMovies(page, region)
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                        nowPlaying.setValue(previewMovies);
                        onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void findPopularMovies(Integer page, String region) {
        Disposable disposable = movieRepository
                .findPopularMovies(page, region)
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    PreviewMovie previewMovie = Utils.getRandomMovie(previewMovies);
                    mainItem.setValue(previewMovie);
                    popular.setValue(previewMovies);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void findTopRatedMovies(Integer page, String region) {
        Disposable disposable = movieRepository
                .findTopRatedMovies(page, region)
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    topRated.setValue(previewMovies);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void findUpcomingMovies(Integer page, String region) {
        Disposable disposable = movieRepository
                .findUpcomingMovies(page, region)
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    upcoming.setValue(previewMovies);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void findGenres() {
        Disposable disposable = genreRepository
                .findGenresForMovies()
                .subscribe(genres1 -> {
                    genres.setValue(genres1.getGenres());
                    onComplete();
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
