package app.mov.movieteca.api;

import app.mov.movieteca.model.request.CreateSessionRequest;
import app.mov.movieteca.model.request.DeleteSessionRequest;
import app.mov.movieteca.model.request.ValidateTokenRequest;
import app.mov.movieteca.model.response.Account;
import app.mov.movieteca.model.response.BaseMovieResponse;
import app.mov.movieteca.model.response.BaseTVShowResponse;
import app.mov.movieteca.model.response.DeleteSessionResponse;
import app.mov.movieteca.model.response.GuestSessionResponse;
import app.mov.movieteca.model.response.SessionResponse;
import app.mov.movieteca.model.response.TokenResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("authentication/guest_session/new")
    Single<GuestSessionResponse> authenticateGuestSession(@Query(value = "api_key") String apiKey);

    @GET("authentication/token/new")
    Single<TokenResponse> generateNewToken(@Query(value = "api_key") String apiKey);

    @POST("authentication/session/new")
    Single<SessionResponse> createNewSession(@Query(value = "api_key") String apiKey,
                                             @Body CreateSessionRequest createSessionRequest);

    @POST("authentication/token/validate_with_login")
    Single<TokenResponse> validateToken(@Query("api_key") String apiKey,
                                        @Body ValidateTokenRequest validateTokenRequest);

    @DELETE("authentication/session")
    Single<DeleteSessionResponse> deleteSession(@Query("api_key") String apiKey,
                                                @Body DeleteSessionRequest deleteSessionRequest);

    @GET("account/{account_id}/rated/movies")
    Single<BaseMovieResponse> findRatedMovies(@Path("account_id") int accountId,
                                              @Query("api_key") String apiKey,
                                              @Query("session_id") String sessionId,
                                              @Query("page") int page);

    @GET("account/{account_id}/rated/tv")
    Single<BaseTVShowResponse> findRatedShows(@Path("account_id") int accountId,
                                              @Query("api_key") String apiKey,
                                              @Query("session_id") String sessionId,
                                              @Query("page") int page);

    @GET("account")
    Single<Account> getAccountDetails(@Query("api_key") String apiKey,
                                      @Query("session_id") String sessionId);

    @GET("account/{account_id}/favorite/movies")
    Single<BaseMovieResponse> getFavoriteMovies(@Path("account_id") int accountId,
                                                @Query("api_key") String apiKey,
                                                @Query("session_id") String sessionId,
                                                @Query("page") int page);

    @GET("account/{account_id}/favorite/tv")
    Single<BaseTVShowResponse> getFavoriteShows(@Path("account_id") int accountId,
                                                @Query("api_key") String apiKey,
                                                @Query("session_id") String sessionId,
                                                @Query("page") int page);
}
