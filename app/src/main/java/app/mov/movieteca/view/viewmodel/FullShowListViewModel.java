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
import app.mov.movieteca.util.Constants;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class FullShowListViewModel extends ViewModel {

    private final MediatorLiveData<List<PreviewTVShow>> data = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Inject ShowRepository showRepository;

    public FullShowListViewModel() {
        Movieteca.getApplicationComponent().inject(this);
    }

    public void start(String type, Integer page) {
        isLoading.setValue(true);
        switch (type) {
            case Constants.POPULAR:
                findPopularShows(page);
                break;
            case Constants.NOW_PLAYING:
                findNowPlayingShows(page);
                break;
            case Constants.UPCOMING:
                findUpcomingShows(page);
                break;
            case Constants.TOP_RATED:
                findTopRatedShows(page);
                break;
        }
    }

    private boolean filterListShows(List<PreviewTVShow> previewShows) {
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
                .doOnSubscribe(d -> isLoading.setValue(false))
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterListShows)
                .subscribe(previewShows -> {
                    data.setValue(previewShows);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    private void findPopularShows(Integer page) {
        Disposable disposable = showRepository
                .findPopularShows(page)
                .doOnSubscribe(d -> isLoading.setValue(false))
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterListShows)
                .subscribe(previewShows -> {
                    data.setValue(previewShows);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    private void findTopRatedShows(Integer page) {
        Disposable disposable = showRepository
                .findTopRatedShows(page)
                .doOnSubscribe(d -> isLoading.setValue(false))
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterListShows)
                .subscribe(previewShows -> {
                    data.setValue(previewShows);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    private void findUpcomingShows(Integer page) {
        Disposable disposable = showRepository
                .findUpcomingShows(page)
                .doOnSubscribe(d -> isLoading.setValue(false))
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterListShows)
                .subscribe(previewShows -> {
                    data.setValue(previewShows);
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<List<PreviewTVShow>> getData() {
        return data;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
