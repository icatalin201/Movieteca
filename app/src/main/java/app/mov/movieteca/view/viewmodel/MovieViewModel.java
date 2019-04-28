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
import app.mov.movieteca.model.response.BaseMovieResponse;
import app.mov.movieteca.model.response.CreditsResponse;
import app.mov.movieteca.model.response.MovieResponse;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.model.response.Review;
import app.mov.movieteca.model.response.SessionResponse;
import app.mov.movieteca.model.response.VideoResponse;
import app.mov.movieteca.repository.MovieRepository;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.util.Shared;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MovieViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MediatorLiveData<MovieResponse> movie = new MediatorLiveData<>();
    private final MediatorLiveData<VideoResponse> videos = new MediatorLiveData<>();
    private final MediatorLiveData<CreditsResponse> credits = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewMovie>> movies = new MediatorLiveData<>();
    private final MediatorLiveData<List<Review>> reviews = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isFavorite = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private int count = 0;
    private int accountId;
    private String sessionId;

    public MovieViewModel() {
        Movieteca.getApplicationComponent().inject(this);
        Account account = shared.getObject(Shared.ACCOUNT_KEY, null, Account.class);
        accountId = account.getId();
        SessionResponse sessionResponse = shared.getObject(Shared.SESSION_KEY, null, SessionResponse.class);
        sessionId = sessionResponse.getSessionId();
    }

    public MediatorLiveData<List<Review>> getReviews() {
        return reviews;
    }

    public MediatorLiveData<List<PreviewMovie>> getMovies() {
        return movies;
    }

    public MediatorLiveData<VideoResponse> getVideos() {
        return videos;
    }

    public MediatorLiveData<CreditsResponse> getCredits() {
        return credits;
    }

    public MediatorLiveData<MovieResponse> getMovie() {
        return movie;
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }

    @Inject
    MovieRepository movieRepository;
    @Inject
    FavoritePreviewMediaDao favoritePreviewMediaDao;

    private Shared shared = Shared.getInstance();

    public void start(int movieId) {
        getReviews(1, movieId);
        isLoading.setValue(true);
        getDetails(movieId);
        getVideos(movieId);
        getCredits(movieId);
        getSimilarMovies(movieId);
        findFavorite(movieId);
    }

    private void findFavorite(int movieId) {
        new Thread(() -> {
            boolean favorite = favoritePreviewMediaDao.isFavorite(movieId, Constants.MOVIE) != null;
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
    public void removeFavorite(int movieId) {
        new Thread(() -> {
            favoritePreviewMediaDao.removeFavoriteByQuery(movieId, Constants.MOVIE);
            new Handler(Looper.getMainLooper()).post(() -> isFavorite.setValue(false));
            if (accountId != 0) toggleFavorite(false, movieId);
        }).start();
    }

    private synchronized void onComplete() {
        count++;
        if (count == 4) {
            isLoading.setValue(false);
        }
    }

    private void getDetails(int movieId) {
        Disposable disposable = movieRepository
                .findMovieDetails(movieId)
                .subscribe(movieResponse -> {
                    movie.setValue(movieResponse);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void getVideos(int movieId) {
        Disposable disposable = movieRepository
                .findMovieVideos(movieId)
                .subscribe(videoResponse -> {
                    videos.setValue(videoResponse);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void getCredits(int movieId) {
        Disposable disposable = movieRepository
                .findMovieCredits(movieId)
                .subscribe(creditsResponse -> {
                    credits.setValue(creditsResponse);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void getSimilarMovies(int movieId) {
        Disposable disposable = movieRepository
                .findSimilarMovies(1, movieId)
                .map(BaseMovieResponse::getResults)
                .filter(this::filterList)
                .subscribe(previewMovies -> {
                    movies.setValue(previewMovies);
                    onComplete();
                }, throwable -> {});
        compositeDisposable.add(disposable);
    }

    private void toggleFavorite(boolean favorite, int mediaId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setFavorite(favorite);
        favoriteRequest.setMediaId(mediaId);
        favoriteRequest.setMediaType(Constants.MOVIE);
        Disposable disposable = movieRepository
                .toggleFavorite(accountId, sessionId, favoriteRequest)
                .subscribe();
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

    private void getReviews(int page, int movieId) {
        Disposable disposable = movieRepository
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
