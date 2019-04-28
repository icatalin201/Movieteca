package app.mov.movieteca.repository;

import javax.inject.Inject;

import app.mov.movieteca.api.UserService;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserRepository extends BaseRepository {

    private final UserService userService;

    @Inject
    public UserRepository(UserService userService) {
        this.userService = userService;
    }

    public Single<GuestSessionResponse> authenticateGuest() {
        return userService
                .authenticateGuestSession(APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<TokenResponse> generateToken() {
        return userService
                .generateNewToken(APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<SessionResponse> createSession(CreateSessionRequest createSessionRequest) {
        return userService
                .createNewSession(APIKEY, createSessionRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<TokenResponse> validateToken(ValidateTokenRequest validateTokenRequest) {
        return userService
                .validateToken(APIKEY, validateTokenRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<DeleteSessionResponse> deleteSession(DeleteSessionRequest deleteSessionRequest) {
        return userService
                .deleteSession(APIKEY, deleteSessionRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<BaseMovieResponse> getRatedMovies(int accountId, String sessionId, int page) {
        return userService
                .findRatedMovies(accountId, APIKEY, sessionId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<BaseTVShowResponse> getRatedShows(int accountId, String sessionId, int page) {
        return userService
                .findRatedShows(accountId, APIKEY, sessionId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Account> getAccount(String sessionId) {
        return userService
                .getAccountDetails(APIKEY, sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<BaseMovieResponse> getFavoriteMovies(int accountId, String sessionId, int page) {
        return userService
                .getFavoriteMovies(accountId, APIKEY, sessionId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<BaseTVShowResponse> getFavoriteShows(int accountId, String sessionId, int page) {
        return userService
                .getFavoriteShows(accountId, APIKEY, sessionId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
