package app.mov.movieteca.api;

import app.mov.movieteca.model.request.FavoriteRequest;
import app.mov.movieteca.model.response.BaseMovieResponse;
import app.mov.movieteca.model.response.CreditsResponse;
import app.mov.movieteca.model.response.FavoriteResponse;
import app.mov.movieteca.model.response.MovieResponse;
import app.mov.movieteca.model.response.ReviewResponse;
import app.mov.movieteca.model.response.VideoResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/now_playing")
    Single<BaseMovieResponse> findNowPlayingMovies(@Query("api_key") String apiKey,
                                                   @Query("page") int page,
                                                   @Query("region") String region);

    @GET("movie/popular")
    Single<BaseMovieResponse> findPopularMovies(@Query("api_key") String apiKey,
                                                @Query("page") int page,
                                                @Query("region") String region);

    @GET("movie/upcoming")
    Single<BaseMovieResponse> findUpcomingMovies(@Query("api_key") String apiKey,
                                                 @Query("page") int page,
                                                 @Query("region") String region);

    @GET("movie/top_rated")
    Single<BaseMovieResponse> findTopRatedMovies(@Query("api_key") String apiKey,
                                                 @Query("page") int page,
                                                 @Query("region") String region);

    @GET("movie/{id}/similar")
    Single<BaseMovieResponse> findSimilarMovies(@Path("id") int movieId,
                                                @Query("api_key") String apiKey,
                                                @Query("page") int page);

    @GET("discover/movie")
    Single<BaseMovieResponse> findMoviesByGenres(@Query("api_key") String apiKey,
                                                 @Query("page") int page,
                                                 @Query("with_genres") String genres);

    @GET("movie/{id}")
    Single<MovieResponse> findMovieDetails(@Path("id") int movieId,
                                           @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Single<VideoResponse> findMovieVideos(@Path("id") int movieId,
                                          @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Single<CreditsResponse> findMovieCredits(@Path("id") int movieId,
                                             @Query("api_key") String apiKey);

    @GET("movie/latest")
    Single<MovieResponse> findLatestMovie(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Single<ReviewResponse> findMovieReviews(@Path("movie_id") int movieId,
                                            @Query("api_key") String apiKey,
                                            @Query("page") int page);

    @GET("search/movie")
    Single<BaseMovieResponse> findMoviesBySearch(@Query("api_key") String apiKey,
                                                 @Query("query") String query,
                                                 @Query("page") int page);

    @POST("account/{account_id}/favorite")
    Single<FavoriteResponse> toggleFavorite(@Path("account_id") int accountId,
                                            @Query("api_key") String apiKey,
                                            @Query("session_id") String sessionId,
                                            @Body FavoriteRequest favoriteRequest);

}
