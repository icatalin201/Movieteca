package app.mov.movieteca.api;

import app.mov.movieteca.model.response.GenreResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenreService {

    @GET("genre/movie/list")
    Single<GenreResponse> getMovieGenresList(@Query("api_key") String apiKey);

    @GET("genre/tv/list")
    Single<GenreResponse> getTVShowGenresList(@Query("api_key") String apiKey);
}
