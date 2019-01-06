package app.mov.movieteca.retronetwork;

import java.util.List;

import app.mov.movieteca.model.BaseMovieResponse;
import app.mov.movieteca.model.BaseTVShowResponse;
import app.mov.movieteca.model.Credits;
import app.mov.movieteca.model.Genres;
import app.mov.movieteca.model.Movie;
import app.mov.movieteca.model.MovieCastsDetails;
import app.mov.movieteca.model.Person;
import app.mov.movieteca.model.ShowCastsDetails;
import app.mov.movieteca.model.TVShow;
import app.mov.movieteca.model.Videos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Catalin on 12/7/2017.
 */

public interface NetworkService {

    /* For movies */

    @GET("movie/now_playing")
    Call<BaseMovieResponse> getNowShowingMovies(@Query("api_key") String apiKey,
                                                @Query("page") Integer page,
                                                @Query("region") String region);

    @GET("movie/popular")
    Call<BaseMovieResponse> getPopularMovies(@Query("api_key") String apiKey,
                                         @Query("page") Integer page,
                                         @Query("region") String region);

    @GET("movie/upcoming")
    Call<BaseMovieResponse> getUpcomingMovies(@Query("api_key") String apiKey,
                                           @Query("page") Integer page,
                                           @Query("region") String region);

    @GET("movie/top_rated")
    Call<BaseMovieResponse> getTopRatedMovies(@Query("api_key") String apiKey,
                                           @Query("page") Integer page,
                                           @Query("region") String region);

    @GET("movie/{id}/similar")
    Call<BaseMovieResponse> getSimilarMovies(@Path("id") Integer movieId,
                                             @Query("api_key") String apiKey,
                                             @Query("page") Integer page);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") Integer movieId,
                                @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Videos> getMovieVideos(@Path("id") Integer movieId,
                                @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<Credits> getMovieCredits(@Path("id") Integer movieId,
                                  @Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<List<Genres>> getMovieGenresList(@Query("api_key") String apiKey);


    /* For tv shows */

    @GET("tv/airing_today")
    Call<BaseTVShowResponse> getAiringTodayTVShows(@Query("api_key") String apiKey,
                                                   @Query("page") Integer page);

    @GET("tv/on_the_air")
    Call<BaseTVShowResponse> getOnTheAirTVShows(@Query("api_key") String apiKey,
                                             @Query("page") Integer page);

    @GET("tv/popular")
    Call<BaseTVShowResponse> getPopularTVShows(@Query("api_key") String apiKey,
                                           @Query("page") Integer page);

    @GET("tv/top_rated")
    Call<BaseTVShowResponse> getTopRatedTVShows(@Query("api_key") String apiKey,
                                             @Query("page") Integer page);

    @GET("tv/{id}/similar")
    Call<BaseTVShowResponse> getSimilarTVShows(@Path("id") Integer movieId,
                                               @Query("api_key") String apiKey,
                                               @Query("page") Integer page);

    @GET("tv/{id}")
    Call<TVShow> getTVShowDetails(@Path("id") Integer tvShowId,
                                  @Query("api_key") String apiKey);

    @GET("tv/{id}/videos")
    Call<Videos> getTVShowVideos(@Path("id") Integer movieId,
                                 @Query("api_key") String apiKey);

    @GET("tv/{id}/credits")
    Call<Credits> getTVShowCredits(@Path("id") Integer movieId,
                                         @Query("api_key") String apiKey);

    @GET("genre/tv/list")
    Call<List<Genres>> getTVShowGenresList(@Query("api_key") String apiKey);

    /* For Casts */
    @GET("person/{id}")
    Call<Person> getPersonDetails(@Path("id") Integer personId,
                                  @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Call<MovieCastsDetails> getMovieCastsOfPerson(@Path("id") Integer personId,
                                                  @Query("api_key") String apiKey);

    @GET("person/{id}/tv_credits")
    Call<ShowCastsDetails> getTVCastsOfPerson(@Path("id") Integer personId,
                                              @Query("api_key") String apiKey);

}
