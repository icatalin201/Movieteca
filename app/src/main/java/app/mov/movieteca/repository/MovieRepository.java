package app.mov.movieteca.repository;

import javax.inject.Inject;

import app.mov.movieteca.api.MovieService;
import app.mov.movieteca.model.request.FavoriteRequest;
import app.mov.movieteca.model.response.BaseMovieResponse;
import app.mov.movieteca.model.response.CreditsResponse;
import app.mov.movieteca.model.response.FavoriteResponse;
import app.mov.movieteca.model.response.MovieResponse;
import app.mov.movieteca.model.response.ReviewResponse;
import app.mov.movieteca.model.response.VideoResponse;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieRepository extends BaseRepository {

    private final MovieService movieService;

    @Inject
    public MovieRepository(MovieService movieService) {
        this.movieService = movieService;
    }

    public Single<BaseMovieResponse> findNowPlayingMovies(int page, String region) {
        return movieService
                .findNowPlayingMovies(APIKEY, page, region)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseMovieResponse> findPopularMovies(int page, String region) {
        return movieService
                .findPopularMovies(APIKEY, page, region)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseMovieResponse> findUpcomingMovies(int page, String region) {
        return movieService
                .findUpcomingMovies(APIKEY, page, region)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseMovieResponse> findTopRatedMovies(int page, String region) {
        return movieService
                .findTopRatedMovies(APIKEY, page, region)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseMovieResponse> findSimilarMovies(int page, int movieId) {
        return movieService
                .findSimilarMovies(movieId, APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<MovieResponse> findMovieDetails(int movieId) {
        return movieService
                .findMovieDetails(movieId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<VideoResponse> findMovieVideos(int movieId) {
        return movieService
                .findMovieVideos(movieId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<CreditsResponse> findMovieCredits(int movieId) {
        return movieService
                .findMovieCredits(movieId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseMovieResponse> findMoviesWithGenres(int page, String genres) {
        return movieService
                .findMoviesByGenres(APIKEY, page, genres)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseMovieResponse> findMoviesBySearch(int page, String query) {
        return movieService
                .findMoviesBySearch(APIKEY, query, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<FavoriteResponse> toggleFavorite(int accountId, String sessionId,
                                                   FavoriteRequest favoriteRequest) {
        return movieService
                .toggleFavorite(accountId, APIKEY, sessionId, favoriteRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<ReviewResponse> findMovieReviews(int page, int movieId) {
        return movieService
                .findMovieReviews(movieId, APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<MovieResponse> findLatest() {
        return movieService
                .findLatestMovie(APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
