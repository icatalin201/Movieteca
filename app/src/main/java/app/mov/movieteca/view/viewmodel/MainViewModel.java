package app.mov.movieteca.view.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.response.MovieResponse;
import app.mov.movieteca.model.response.Person;
import app.mov.movieteca.model.response.TVShowResponse;
import app.mov.movieteca.repository.CastRepository;
import app.mov.movieteca.repository.MovieRepository;
import app.mov.movieteca.repository.ShowRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainViewModel extends ViewModel {

    private final MediatorLiveData<MovieResponse> movie = new MediatorLiveData<>();
    private final MediatorLiveData<TVShowResponse> show = new MediatorLiveData<>();
    private final MediatorLiveData<List<Person>> persons = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> loading = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject MovieRepository movieRepository;
    @Inject ShowRepository showRepository;
    @Inject CastRepository castRepository;

    private int count = 0;

    public MediatorLiveData<Boolean> getLoading() {
        return loading;
    }

    public MediatorLiveData<List<Person>> getPersons() {
        return persons;
    }

    public MediatorLiveData<MovieResponse> getMovie() {
        return movie;
    }

    public MediatorLiveData<TVShowResponse> getShow() {
        return show;
    }

    public MainViewModel() {
        Movieteca.getApplicationComponent().inject(this);
        loading.setValue(true);
        loadMovie();
        loadShow();
        loadPopulars(1);
    }

    private synchronized void onComplete() {
        count++;
        if (count == 3) {
            loading.setValue(false);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    private void loadMovie() {
        Disposable disposable = movieRepository
                .findLatest()
                .subscribe(movieResponse -> {
                    movie.setValue(movieResponse);
                    onComplete();
                });
        compositeDisposable.add(disposable);
    }

    private void loadShow() {
        Disposable disposable = showRepository
                .findLatest()
                .subscribe(tvShowResponse -> {
                    show.setValue(tvShowResponse);
                    onComplete();
                });
        compositeDisposable.add(disposable);
    }

    public void loadPopulars(int page) {
        Disposable disposable = castRepository
                .getPopulars(page)
                .subscribe(personResponse -> {
                    persons.setValue(personResponse.getPersonList());
                    if (page == 1) onComplete();
                });
        compositeDisposable.add(disposable);
    }
}
