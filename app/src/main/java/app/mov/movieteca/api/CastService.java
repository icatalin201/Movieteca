package app.mov.movieteca.api;

import app.mov.movieteca.model.response.MovieCastsDetails;
import app.mov.movieteca.model.response.Person;
import app.mov.movieteca.model.response.PersonResponse;
import app.mov.movieteca.model.response.ShowCastsDetails;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CastService {

    @GET("person/{id}")
    Single<Person> getPersonDetails(@Path("id") int personId,
                                    @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Single<MovieCastsDetails> getMovieCastsOfPerson(@Path("id") int personId,
                                                    @Query("api_key") String apiKey);

    @GET("person/{id}/tv_credits")
    Single<ShowCastsDetails> getTVCastsOfPerson(@Path("id") int personId,
                                                @Query("api_key") String apiKey);

    @GET("person/popular")
    Single<PersonResponse> getPopulars(@Query("api_key") String apiKey, @Query("page") int page);
}
