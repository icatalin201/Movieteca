package app.mov.movieteca.view.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.response.BaseMovieResponse;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.repository.MovieRepository;
import app.mov.movieteca.util.Constants;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class FullMovieListViewModel extends ViewModel {

    private final MediatorLiveData<List<PreviewMovie>> data = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject MovieRepository movieRepository;

    public FullMovieListViewModel() {
        Movieteca.getApplicationComponent().inject(this);
    }

    public void start(String type, Integer page, String region) {
        isLoading.setValue(true);
        switch (type) {
            case Constants.POPULAR:
                findPopularMovies(page, region);
                break;
            case Constants.NOW_PLAYING:
                findNowPlayingMovies(page, region);
                break;
            case Constants.UPCOMING:
                findUpcomingMovies(page, region);
                break;
            case Constants.TOP_RATED:
                findTopRatedMovies(page, region);
                break;
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
                .doOnSubscribe(d -> isLoading.setValue(false))
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    data.setValue(previewMovies);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    private void findPopularMovies(Integer page, String region) {
        Disposable disposable = movieRepository
                .findPopularMovies(page, region)
                .doOnSubscribe(d -> isLoading.setValue(false))
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    data.setValue(previewMovies);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    private void findTopRatedMovies(Integer page, String region) {
        Disposable disposable = movieRepository
                .findTopRatedMovies(page, region)
                .doOnSubscribe(d -> isLoading.setValue(false))
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    data.setValue(previewMovies);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    private void findUpcomingMovies(Integer page, String region) {
        Disposable disposable = movieRepository
                .findUpcomingMovies(page, region)
                .doOnSubscribe(d -> isLoading.setValue(false))
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    data.setValue(previewMovies);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    public MediatorLiveData<List<PreviewMovie>> getData() {
        return data;
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
