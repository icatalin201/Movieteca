package app.mov.movieteca.api;

import app.mov.movieteca.model.request.FavoriteRequest;
import app.mov.movieteca.model.response.BaseTVShowResponse;
import app.mov.movieteca.model.response.CreditsResponse;
import app.mov.movieteca.model.response.FavoriteResponse;
import app.mov.movieteca.model.response.ReviewResponse;
import app.mov.movieteca.model.response.TVShowResponse;
import app.mov.movieteca.model.response.VideoResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TvSeriesService {

    @GET("tv/airing_today")
    Single<BaseTVShowResponse> getAiringTodayTVShows(@Query("api_key") String apiKey,
                                                     @Query("page") int page);

    @GET("tv/on_the_air")
    Single<BaseTVShowResponse> getOnTheAirTVShows(@Query("api_key") String apiKey,
                                                  @Query("page") int page);

    @GET("tv/popular")
    Single<BaseTVShowResponse> getPopularTVShows(@Query("api_key") String apiKey,
                                                 @Query("page") int page);

    @GET("tv/top_rated")
    Single<BaseTVShowResponse> getTopRatedTVShows(@Query("api_key") String apiKey,
                                                  @Query("page") int page);

    @GET("tv/{id}/similar")
    Single<BaseTVShowResponse> getSimilarTVShows(@Path("id") int movieId,
                                                 @Query("api_key") String apiKey,
                                                 @Query("page") int page);

    @GET("tv/{id}")
    Single<TVShowResponse> getTVShowDetails(@Path("id") int tvShowId,
                                            @Query("api_key") String apiKey);

    @GET("tv/{id}/videos")
    Single<VideoResponse> getTVShowVideos(@Path("id") int showId,
                                          @Query("api_key") String apiKey);

    @GET("tv/{id}/credits")
    Single<CreditsResponse> getTVShowCredits(@Path("id") int showId,
                                             @Query("api_key") String apiKey);

    @GET("discover/tv")
    Single<BaseTVShowResponse> findShowsByGenres(@Query("api_key") String apiKey,
                                                @Query("page") int page,
                                                @Query("with_genres") String genres);

    @GET("tv/latest")
    Single<TVShowResponse> findLatestShow(@Query("api_key") String apiKey);

    @GET("tv/{tv_id}/reviews")
    Single<ReviewResponse> findShowReviews(@Path("tv_id") int movieId,
                                           @Query("api_key") String apiKey,
                                           @Query("page") int page);

    @POST("account/{account_id}/favorite")
    Single<FavoriteResponse> toggleFavorite(@Path("account_id") int accountId,
                                            @Query("api_key") String apiKey,
                                            @Query("session_id") String sessionId,
                                            @Body FavoriteRequest favoriteRequest);
}
