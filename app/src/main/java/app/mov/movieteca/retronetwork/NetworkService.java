package app.mov.movieteca.retronetwork;

import app.mov.movieteca.models.cast.MovieCastsDetails;
import app.mov.movieteca.models.cast.Person;
import app.mov.movieteca.models.cast.ShowCastsDetails;
import app.mov.movieteca.models.cast.TVCastsDetails;
import app.mov.movieteca.models.movies.Genres;
import app.mov.movieteca.models.movies.GenresList;
import app.mov.movieteca.models.movies.Movie;
import app.mov.movieteca.models.movies.MovieCredits;
import app.mov.movieteca.models.movies.NowPlayingMovies;
import app.mov.movieteca.models.movies.PopularMovies;
import app.mov.movieteca.models.movies.SimilarMovies;
import app.mov.movieteca.models.movies.TopRatedMovies;
import app.mov.movieteca.models.movies.UpcomingMovies;
import app.mov.movieteca.models.movies.Videos;
import app.mov.movieteca.models.tvshows.AiringTodayTVShows;
import app.mov.movieteca.models.tvshows.OnTheAirTVShows;
import app.mov.movieteca.models.tvshows.PopularTVShows;
import app.mov.movieteca.models.tvshows.SimilarTVShows;
import app.mov.movieteca.models.tvshows.TVShow;
import app.mov.movieteca.models.tvshows.TVShowCredits;
import app.mov.movieteca.models.tvshows.TopRatedTVShows;
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
    Call<NowPlayingMovies> getNowShowingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/popular")
    Call<PopularMovies> getPopularMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/upcoming")
    Call<UpcomingMovies> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/top_rated")
    Call<TopRatedMovies> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Videos> getMovieVideos(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<MovieCredits> getMovieCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/similar")
    Call<SimilarMovies> getSimilarMovies(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("genre/movie/list")
    Call<GenresList> getMovieGenresList(@Query("api_key") String apiKey);


    /* For tv shows */

    @GET("tv/airing_today")
    Call<AiringTodayTVShows> getAiringTodayTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/on_the_air")
    Call<OnTheAirTVShows> getOnTheAirTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/popular")
    Call<PopularTVShows> getPopularTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/top_rated")
    Call<TopRatedTVShows> getTopRatedTVShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/{id}")
    Call<TVShow> getTVShowDetails(@Path("id") Integer tvShowId, @Query("api_key") String apiKey);

    @GET("tv/{id}/videos")
    Call<Videos> getTVShowVideos(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}/credits")
    Call<TVShowCredits> getTVShowCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}/similar")
    Call<SimilarTVShows> getSimilarTVShows(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("genre/tv/list")
    Call<Genres> getTVShowGenresList(@Query("api_key") String apiKey);

    /* For Casts */
    @GET("person/{id}")
    Call<Person> getPersonDetails(@Path("id") Integer personId, @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Call<MovieCastsDetails> getMovieCastsOfPerson(@Path("id") Integer personId, @Query("api_key") String apiKey);

    @GET("person/{id}/tv_credits")
    Call<ShowCastsDetails> getTVCastsOfPerson(@Path("id") Integer personId, @Query("api_key") String apiKey);

}
