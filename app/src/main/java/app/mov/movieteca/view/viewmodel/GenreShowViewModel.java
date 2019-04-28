package app.mov.movieteca.view.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.response.BaseTVShowResponse;
import app.mov.movieteca.model.response.PreviewTVShow;
import app.mov.movieteca.repository.ShowRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class GenreShowViewModel extends ViewModel {

    private final MediatorLiveData<List<PreviewTVShow>> data = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject ShowRepository showRepository;

    public GenreShowViewModel() {
        Movieteca.getApplicationComponent().inject(this);
    }

    private boolean filterList(List<PreviewTVShow> previewTVShows) {
        ListIterator<PreviewTVShow> previewMovieListIterator = previewTVShows.listIterator();
        while (previewMovieListIterator.hasNext()) {
            PreviewTVShow previewTVShow = previewMovieListIterator.next();
            if (previewTVShow.getPosterPath() == null) {
                previewMovieListIterator.remove();
            }
        }
        return true;
    }

    public void find(Integer page, String genres) {
        Disposable disposable = showRepository
                .findShowWithGenres(page, genres)
                .doOnSubscribe(d -> isLoading.setValue(true))
                .map(BaseTVShowResponse::getResults)
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

    public MediatorLiveData<List<PreviewTVShow>> getData() {
        return data;
    }
}
