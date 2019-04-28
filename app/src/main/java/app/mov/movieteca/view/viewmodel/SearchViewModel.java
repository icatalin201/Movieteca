package app.mov.movieteca.view.viewmodel;

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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class SearchViewModel extends ViewModel {

    private final MediatorLiveData<List<Genre>> moviesGenre = new MediatorLiveData<>();
    private final MediatorLiveData<List<Genre>> showsGenre = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewMovie>> search = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject GenreRepository genreRepository;
    @Inject MovieRepository movieRepository;

    public SearchViewModel() {
        Movieteca.getApplicationComponent().inject(this);
    }

    public void subscribeGenres() {
        isLoading.setValue(false);
        start1();
    }

    public void find(Integer page, String genres) {
        Disposable disposable = movieRepository
                .findMoviesWithGenres(page, genres)
                .doOnSubscribe(d -> isLoading.setValue(true))
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    search.setValue(previewMovies);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
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

    public void findMovies(Integer page, String query) {
        Disposable disposable = movieRepository
                .findMoviesBySearch(page, query)
                .doOnSubscribe(d -> isLoading.setValue(true))
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    search.setValue(previewMovies);
                    isLoading.setValue(false);
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void start1() {
        Disposable disposable = genreRepository
                .findGenresForMovies()
                .subscribe(genres1 -> {
                    moviesGenre.setValue(genres1.getGenres());
                });
        compositeDisposable.add(disposable);
    }

    private void start2() {
        Disposable disposable = genreRepository
                .findGenresForSeries()
                .subscribe(genres1 -> {
                    showsGenre.setValue(genres1.getGenres());
                });
        compositeDisposable.add(disposable);
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<List<Genre>> getMoviesGenre() {
        return moviesGenre;
    }

    public MediatorLiveData<List<Genre>> getShowsGenre() {
        return showsGenre;
    }

    public MediatorLiveData<List<PreviewMovie>> getSearch() {
        return search;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
