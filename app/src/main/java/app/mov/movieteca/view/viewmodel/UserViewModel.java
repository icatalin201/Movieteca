package app.mov.movieteca.view.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.FavoritePreviewMediaDao;
import app.mov.movieteca.model.request.CreateSessionRequest;
import app.mov.movieteca.model.request.DeleteSessionRequest;
import app.mov.movieteca.model.request.ValidateTokenRequest;
import app.mov.movieteca.model.response.Account;
import app.mov.movieteca.model.response.AccountAvatar;
import app.mov.movieteca.model.response.AccountGravatar;
import app.mov.movieteca.model.response.BaseMovieResponse;
import app.mov.movieteca.model.response.BaseTVShowResponse;
import app.mov.movieteca.model.response.GuestSessionResponse;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.model.response.PreviewTVShow;
import app.mov.movieteca.model.response.SessionResponse;
import app.mov.movieteca.model.response.TokenResponse;
import app.mov.movieteca.repository.UserRepository;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.util.DateUtils;
import app.mov.movieteca.util.Shared;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<TokenResponse> token = new MutableLiveData<>();
    private final MutableLiveData<GuestSessionResponse> guest = new MutableLiveData<>();
    private final MutableLiveData<SessionResponse> session = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Account> account = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Shared shared = Shared.getInstance();
    private String username = "test";
    private String password = "test";
    private int accountId;
    private String sessionId;

    @Inject
    UserRepository userRepository;
    @Inject
    FavoritePreviewMediaDao favoritePreviewMediaDao;

    public boolean hasValidSession() {
        String sessionType = shared.getString(Shared.SESSION_TYPE, null);
        if (sessionType == null) return false;
        if (sessionType.equals(Shared.SESSION_KEY)) return true;
        if (sessionType.equals(Shared.GUEST_KEY)) {
            GuestSessionResponse guestSessionResponse = shared.getObject(Shared.GUEST_KEY,
                    null, GuestSessionResponse.class);
            if (guestSessionResponse == null) return false;
            String expiresAt = guestSessionResponse.getExpiresAt();
            Date date = DateUtils.convertStringToDate(expiresAt, DateUtils.STANDARD_DATETIME_FORMAT);
            if (date == null) return false;
            Calendar now = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return now.before(calendar);
        }
        return false;
    }

    public MutableLiveData<Account> getAccount() {
        return account;
    }

    public UserViewModel() {
        Movieteca.getApplicationComponent().inject(this);
    }

    public LiveData<TokenResponse> getToken() {
        return token;
    }

    public LiveData<GuestSessionResponse> getGuest() {
        return guest;
    }

    public LiveData<SessionResponse> getSession() {
        return session;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void authenticateGuest() {
        Disposable disposable = userRepository.authenticateGuest()
                .doOnSubscribe(d -> isLoading.setValue(true))
                .subscribe(guestSessionResponse -> {
                    if (guestSessionResponse.isSuccess()) {
                        shared.storeObject(Shared.GUEST_KEY, guestSessionResponse);
                        shared.storeString(Shared.SESSION_TYPE, Shared.GUEST_KEY);
                        guest.setValue(guestSessionResponse);
                        Account account = new Account();
                        account.setId(0);
                        AccountAvatar accountAvatar = new AccountAvatar();
                        AccountGravatar accountGravatar = new AccountGravatar();
                        accountGravatar.setHash("00000000000000000000000000000000");
                        accountAvatar.setGravatar(accountGravatar);
                        account.setAvatar(accountAvatar);
                        account.setName("Guest");
                        shared.storeObject(Shared.ACCOUNT_KEY, account);
                        this.account.setValue(account);
                    } else {
                        message.setValue("An error has occured. Try again!");
                    }
                    isLoading.setValue(false);
                }, throwable -> {
                    throwable.printStackTrace();
                    message.setValue(throwable.getMessage());
                    isLoading.setValue(false);
                });
        compositeDisposable.add(disposable);
    }

    public void generateToken() {
        Disposable disposable = userRepository.generateToken()
                .doOnSubscribe(d -> isLoading.setValue(true))
                .subscribe(tokenResponse -> {
                    if (tokenResponse.isSuccess()) {
                        shared.storeObject(Shared.TOKEN_KEY, tokenResponse);
                        tokenResponse.setExpiresAt(null);
                        token.setValue(tokenResponse);
                    } else {
                        message.setValue("An error has occured. Try again!");
                    }
                }, throwable -> {
                    message.setValue(throwable.getMessage());
                    isLoading.setValue(false);
                });
        compositeDisposable.add(disposable);
    }

    public void validateToken(String t) {
        ValidateTokenRequest validateTokenRequest = new ValidateTokenRequest();
        validateTokenRequest.setPassword(password);
        validateTokenRequest.setUsername(username);
        validateTokenRequest.setRequestToken(t);
        Disposable disposable = userRepository.validateToken(validateTokenRequest)
                .subscribe(tokenResponse -> {
                    if (tokenResponse.isSuccess()) {
                        shared.storeObject(Shared.TOKEN_KEY, tokenResponse);
                        token.setValue(tokenResponse);
                    } else {
                        message.setValue("Check your credentials!");
                    }
                }, throwable -> {
                    message.setValue("Check your credentials!");
                    isLoading.setValue(false);
                });
        compositeDisposable.add(disposable);
    }

    public void createSession(String t) {
        CreateSessionRequest createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setRequestToken(t);
        Disposable disposable = userRepository.createSession(createSessionRequest)
                .doOnSubscribe(d -> isLoading.setValue(true))
                .subscribe(sessionResponse -> {
                    if (sessionResponse.isSuccess()) {
                        shared.storeObject(Shared.SESSION_KEY, sessionResponse);
                        shared.storeString(Shared.SESSION_TYPE, Shared.SESSION_KEY);
                        session.setValue(sessionResponse);
                        sessionId = sessionResponse.getSessionId();
                    } else {
                        message.setValue("Check your credentials!");
                        isLoading.setValue(false);
                    }
                }, throwable -> {
                    message.setValue(throwable.getMessage());
                    isLoading.setValue(false);
                });
        compositeDisposable.add(disposable);
    }

    public void deleteSession() {
        DeleteSessionRequest deleteSessionRequest = new DeleteSessionRequest();
        Disposable disposable = userRepository.deleteSession(deleteSessionRequest)
                .doOnSubscribe(d -> isLoading.setValue(true))
                .subscribe(deleteSessionResponse -> {
                    if (deleteSessionResponse.isSuccess()) {
                        shared.clearObject(Shared.SESSION_KEY);
                    }
                    isLoading.setValue(false);
                }, throwable -> isLoading.setValue(false));
        compositeDisposable.add(disposable);
    }

    public void accessAccount(String sessionId) {
        Disposable disposable = userRepository
                .getAccount(sessionId)
                .subscribe(accountResponse -> {
                    shared.storeObject(Shared.ACCOUNT_KEY, accountResponse);
                    isLoading.setValue(false);
                    account.setValue(accountResponse);
                    accountId = accountResponse.getId();
                }, throwable -> {
                    isLoading.setValue(false);
                });
        compositeDisposable.add(disposable);
    }

    public void loadFavoriteMovies() {
        favoritePreviewMediaDao.delete();
        Disposable d = userRepository
                .getFavoriteMovies(accountId, sessionId, 1)
                .subscribe(baseMovieResponse -> consumeList(baseMovieResponse, 1));
    }

    public void loadFavoriteShows() {
        Disposable d = userRepository
                .getFavoriteShows(accountId, sessionId, 1)
                .subscribe(baseTVShowResponse -> consumeList(baseTVShowResponse, 1));
    }

    private void loadFavoriteMoviesv2(int page) {
        Disposable d = userRepository
                .getFavoriteMovies(accountId, sessionId, page)
                .subscribe(baseMovieResponse -> consumeList(baseMovieResponse, page));
    }

    private void loadFavoriteShowsv2(int page) {
        Disposable d = userRepository
                .getFavoriteShows(accountId, sessionId, page)
                .subscribe(baseTVShowResponse -> consumeList(baseTVShowResponse, page));
    }

    private void consumeList(BaseTVShowResponse baseMovieResponse, int page) {
        List<PreviewTVShow> previewTVShows = baseMovieResponse.getResults();
        for (PreviewTVShow previewMovie : previewTVShows) {
            if (previewMovie.getPosterPath() == null) {
                continue;
            }
            FavoritePreviewMedia favoritePreviewMedia = new FavoritePreviewMedia();
            favoritePreviewMedia.setName(previewMovie.getOriginalName());
            favoritePreviewMedia.setPoster(previewMovie.getPosterPath());
            favoritePreviewMedia.setResId(previewMovie.getId());
            favoritePreviewMedia.setResType(Constants.TV_SHOW);
            new Thread(() -> favoritePreviewMediaDao.insertFavorite(favoritePreviewMedia)).start();
        }
        if (page == 1) {
            int pages = baseMovieResponse.getTotalPages();
            if (pages > 1) {
                for (int i = 1; i < pages; i++) {
                    loadFavoriteShowsv2(i+1);
                }
            }
        }
    }

    private void consumeList(BaseMovieResponse baseMovieResponse, int page) {
        List<PreviewMovie> previewMovieList = baseMovieResponse.getResults();
        for (PreviewMovie previewMovie : previewMovieList) {
            if (previewMovie.getPosterPath() == null) {
                continue;
            }
            FavoritePreviewMedia favoritePreviewMedia = new FavoritePreviewMedia();
            favoritePreviewMedia.setName(previewMovie.getOriginalTitle());
            favoritePreviewMedia.setPoster(previewMovie.getPosterPath());
            favoritePreviewMedia.setResId(previewMovie.getId());
            favoritePreviewMedia.setResType(Constants.MOVIE);
            new Thread(() -> favoritePreviewMediaDao.insertFavorite(favoritePreviewMedia)).start();
        }
        if (page == 1) {
            int pages = baseMovieResponse.getTotalPages();
            if (pages > 1) {
                for (int i = 1; i < pages; i++) {
                    loadFavoriteMoviesv2(i+1);
                }
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public void onUsernameChanged(CharSequence username) {
        this.username = username.toString();
    }

    public void onPasswordChanged(CharSequence password) {
        this.password = password.toString();
    }

}
