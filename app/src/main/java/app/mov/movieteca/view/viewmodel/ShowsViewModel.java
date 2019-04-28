package app.mov.movieteca.view.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.response.BaseTVShowResponse;
import app.mov.movieteca.model.response.Genre;
import app.mov.movieteca.model.response.PreviewTVShow;
import app.mov.movieteca.repository.GenreRepository;
import app.mov.movieteca.repository.ShowRepository;
import app.mov.movieteca.util.Utils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ShowsViewModel extends ViewModel {

    private final MediatorLiveData<List<PreviewTVShow>> popular = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewTVShow>> upcoming = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewTVShow>> topRated = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewTVShow>> nowPlaying = new MediatorLiveData<>();
    private final MediatorLiveData<List<Genre>> genres = new MediatorLiveData<>();
    private final MediatorLiveData<PreviewTVShow> mainItem = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int count = 0;

    @Inject
    ShowRepository showRepository;
    @Inject
    GenreRepository genreRepository;

    public LiveData<List<PreviewTVShow>> getNowPlaying() {
        return nowPlaying;
    }

    public LiveData<List<PreviewTVShow>> getPopular() {
        return popular;
    }

    public LiveData<List<PreviewTVShow>> getTopRated() {
        return topRated;
    }

    public LiveData<List<PreviewTVShow>> getUpcoming() {
        return upcoming;
    }

    public LiveData<PreviewTVShow> getMainItem() {
        return mainItem;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<List<Genre>> getGenres() {
        return genres;
    }

    public ShowsViewModel() {
        Movieteca.getApplicationComponent().inject(this);
        isLoading.setValue(true);
    }

    public void start(Integer page) {
        findNowPlayingShows(page);
        findPopularShows(page);
        findTopRatedShows(page);
        findUpcomingShows(page);
        findGenres();
    }

    private synchronized void onComplete() {
        count++;
        if (count == 5) {
            isLoading.setValue(false);
        }
    }
    private boolean filterList(List<PreviewTVShow> previewShows) {
        ListIterator<PreviewTVShow> previewMovieListIterator = previewShows.listIterator();
        while (previewMovieListIterator.hasNext()) {
            PreviewTVShow previewMovie = previewMovieListIterator.next();
            if (previewMovie.getPosterPath() == null) {
                previewMovieListIterator.remove();
            }
        }
        return true;
    }

    private void findNowPlayingShows(Integer page) {
        Disposable disposable = showRepository
                .findNowPlayingShows(page)
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewShows -> {
                    nowPlaying.setValue(previewShows);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void findPopularShows(Integer page) {
        Disposable disposable = showRepository
                .findPopularShows(page)
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewShows -> {
                    PreviewTVShow previewMovie = Utils.getRandomShow(previewShows);
                    mainItem.setValue(previewMovie);
                    popular.setValue(previewShows);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void findTopRatedShows(Integer page) {
        Disposable disposable = showRepository
                .findTopRatedShows(page)
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewShows -> {
                    topRated.setValue(previewShows);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void findUpcomingShows(Integer page) {
        Disposable disposable = showRepository
                .findUpcomingShows(page)
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewShows -> {
                    upcoming.setValue(previewShows);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void findGenres() {
        Disposable disposable = genreRepository
                .findGenresForSeries()
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
