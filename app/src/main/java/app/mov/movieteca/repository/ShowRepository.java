package app.mov.movieteca.repository;

import javax.inject.Inject;

import app.mov.movieteca.api.TvSeriesService;
import app.mov.movieteca.model.request.FavoriteRequest;
import app.mov.movieteca.model.response.BaseTVShowResponse;
import app.mov.movieteca.model.response.CreditsResponse;
import app.mov.movieteca.model.response.FavoriteResponse;
import app.mov.movieteca.model.response.ReviewResponse;
import app.mov.movieteca.model.response.TVShowResponse;
import app.mov.movieteca.model.response.VideoResponse;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShowRepository extends BaseRepository {

    @Inject TvSeriesService tvSeriesService;

    @Inject
    public ShowRepository(TvSeriesService tvSeriesService) {
        this.tvSeriesService = tvSeriesService;
    }

    public Single<BaseTVShowResponse> findNowPlayingShows(int page) {
        return tvSeriesService
                .getAiringTodayTVShows(APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseTVShowResponse> findPopularShows(int page) {
        return tvSeriesService
                .getPopularTVShows(APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseTVShowResponse> findUpcomingShows(int page) {
        return tvSeriesService
                .getOnTheAirTVShows(APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseTVShowResponse> findTopRatedShows(int page) {
        return tvSeriesService
                .getTopRatedTVShows(APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseTVShowResponse> findSimilarShows(int page, int movieId) {
        return tvSeriesService
                .getSimilarTVShows(movieId, APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<TVShowResponse> findShowDetails(int movieId) {
        return tvSeriesService
                .getTVShowDetails(movieId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<VideoResponse> findShowVideos(int movieId) {
        return tvSeriesService
                .getTVShowVideos(movieId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<CreditsResponse> findShowCredits(int movieId) {
        return tvSeriesService
                .getTVShowCredits(movieId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseTVShowResponse> findShowWithGenres(int page, String genres) {
        return tvSeriesService
                .findShowsByGenres(APIKEY, page, genres)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<FavoriteResponse> toggleFavorite(int accountId, String sessionId,
                                                   FavoriteRequest favoriteRequest) {
        return tvSeriesService
                .toggleFavorite(accountId, APIKEY, sessionId, favoriteRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<ReviewResponse> findMovieReviews(int page, int movieId) {
        return tvSeriesService
                .findShowReviews(movieId, APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<TVShowResponse> findLatest() {
        return tvSeriesService
                .findLatestShow(APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
