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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class GenreMovieViewModel extends ViewModel {

    private final MediatorLiveData<List<PreviewMovie>> data = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject
    MovieRepository movieRepository;

    public GenreMovieViewModel() {
        Movieteca.getApplicationComponent().inject(this);
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

    public void find(Integer page, String genres) {
        Disposable disposable = movieRepository
                .findMoviesWithGenres(page, genres)
                .doOnSubscribe(d -> isLoading.setValue(true))
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    data.setValue(previewMovies);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<List<PreviewMovie>> getData() {
        return data;
    }
}
