package app.mov.movieteca.view.viewmodel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.FavoritePreviewMediaDao;
import app.mov.movieteca.model.request.FavoriteRequest;
import app.mov.movieteca.model.response.Account;
import app.mov.movieteca.model.response.BaseTVShowResponse;
import app.mov.movieteca.model.response.CreditsResponse;
import app.mov.movieteca.model.response.PreviewTVShow;
import app.mov.movieteca.model.response.Review;
import app.mov.movieteca.model.response.SessionResponse;
import app.mov.movieteca.model.response.TVShowResponse;
import app.mov.movieteca.model.response.VideoResponse;
import app.mov.movieteca.repository.ShowRepository;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.util.Shared;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ShowViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MediatorLiveData<TVShowResponse> show = new MediatorLiveData<>();
    private final MediatorLiveData<VideoResponse> videos = new MediatorLiveData<>();
    private final MediatorLiveData<CreditsResponse> credits = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewTVShow>> shows = new MediatorLiveData<>();
    private final MediatorLiveData<List<Review>> reviews = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isFavorite = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private int count = 0;
    private int accountId;
    private String sessionId;

    public ShowViewModel() {
        Movieteca.getApplicationComponent().inject(this);
        Account account = shared.getObject(Shared.ACCOUNT_KEY, null, Account.class);
        accountId = account.getId();
        SessionResponse sessionResponse = shared.getObject(Shared.SESSION_KEY, null, SessionResponse.class);
        sessionId = sessionResponse.getSessionId();
    }

    public MediatorLiveData<List<Review>> getReviews() {
        return reviews;
    }

    public MediatorLiveData<List<PreviewTVShow>> getShows() {
        return shows;
    }

    public MediatorLiveData<VideoResponse> getVideos() {
        return videos;
    }

    public MediatorLiveData<CreditsResponse> getCredits() {
        return credits;
    }

    public MediatorLiveData<TVShowResponse> getShow() {
        return show;
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }

    @Inject
    ShowRepository showRepository;
    @Inject
    FavoritePreviewMediaDao favoritePreviewMediaDao;

    private Shared shared = Shared.getInstance();

    public void start(int showId) {
        getReviews(1, showId);
        isLoading.setValue(true);
        getDetails(showId);
        getVideos(showId);
        getCredits(showId);
        getSimilarShows(showId);
        findFavorite(showId);
    }

    private void findFavorite(int showId) {
        new Thread(() -> {
            boolean favorite = favoritePreviewMediaDao.isFavorite(showId, Constants.TV_SHOW) != null;
            new Handler(Looper.getMainLooper()).post(() -> isFavorite.setValue(favorite));
        }).start();
    }

    public void addFavorite(FavoritePreviewMedia favoritePreviewMedia) {
        new Thread(() -> {
            favoritePreviewMediaDao.insertFavorite(favoritePreviewMedia);
            new Handler(Looper.getMainLooper()).post(() -> isFavorite.setValue(true));
            if (accountId != 0) toggleFavorite(true, favoritePreviewMedia.getResId());
        }).start();
    }
    public void removeFavorite(int showId) {
        new Thread(() -> {
            favoritePreviewMediaDao.removeFavoriteByQuery(showId, Constants.TV_SHOW);
            new Handler(Looper.getMainLooper()).post(() -> isFavorite.setValue(false));
            if (accountId != 0) toggleFavorite(false, showId);
        }).start();
    }

    private synchronized void onComplete() {
        count++;
        if (count == 4) {
            isLoading.setValue(false);
        }
    }

    private void getDetails(int showId) {
        Disposable disposable = showRepository
                .findShowDetails(showId)
                .subscribe(showResponse -> {
                    show.setValue(showResponse);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void getVideos(int showId) {
        Disposable disposable = showRepository
                .findShowVideos(showId)
                .subscribe(videoResponse -> {
                    videos.setValue(videoResponse);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void getCredits(int showId) {
        Disposable disposable = showRepository
                .findShowCredits(showId)
                .subscribe(creditsResponse -> {
                    credits.setValue(creditsResponse);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void getSimilarShows(int showId) {
        Disposable disposable = showRepository
                .findSimilarShows(1, showId)
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewShows -> {
                    shows.setValue(previewShows);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
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

    private void toggleFavorite(boolean favorite, int mediaId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setFavorite(favorite);
        favoriteRequest.setMediaId(mediaId);
        favoriteRequest.setMediaType(Constants.TV_SHOW);
        Disposable disposable = showRepository
                .toggleFavorite(accountId, sessionId, favoriteRequest)
                .subscribe();
        compositeDisposable.add(disposable);
    }

    private void getReviews(int page, int movieId) {
        Disposable disposable = showRepository
                .findMovieReviews(page, movieId)
                .subscribe(reviewResponse -> {
                    reviews.setValue(reviewResponse.getReviewList());
                    if (page < reviewResponse.getTotalPages()) {
                        getReviews(page+1, movieId);
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
