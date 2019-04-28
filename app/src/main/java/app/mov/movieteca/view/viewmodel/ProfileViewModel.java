package app.mov.movieteca.view.viewmodel;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import app.mov.movieteca.application.Movieteca;
import app.mov.movieteca.model.response.Account;
import app.mov.movieteca.model.response.BaseMovieResponse;
import app.mov.movieteca.model.response.BaseTVShowResponse;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.model.response.PreviewTVShow;
import app.mov.movieteca.model.response.SessionResponse;
import app.mov.movieteca.repository.UserRepository;
import app.mov.movieteca.util.Shared;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ProfileViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MediatorLiveData<List<PreviewMovie>> ratedMovies = new MediatorLiveData<>();
    private final MediatorLiveData<List<PreviewTVShow>> ratedShows = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();
    private final MediatorLiveData<Account> account = new MediatorLiveData<>();
    private String sessionId;
    private int accountId;

    @Inject UserRepository userRepository;
    private Shared shared = Shared.getInstance();

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<Account> getAccount() {
        return account;
    }

    public MediatorLiveData<List<PreviewMovie>> getRatedMovies() {
        return ratedMovies;
    }

    public MediatorLiveData<List<PreviewTVShow>> getRatedShows() {
        return ratedShows;
    }

    public ProfileViewModel() {
        Movieteca.getApplicationComponent().inject(this);
        isLoading.setValue(true);
        checkAccount();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    private boolean filterMovies(List<PreviewMovie> previewMovies) {
        ListIterator<PreviewMovie> previewMovieListIterator = previewMovies.listIterator();
        while (previewMovieListIterator.hasNext()) {
            PreviewMovie previewMovie = previewMovieListIterator.next();
            if (previewMovie.getBackdropPath() == null) {
                previewMovieListIterator.remove();
            }
        }
        return true;
    }

    private boolean filterShows(List<PreviewTVShow> previewTVShows) {
        ListIterator<PreviewTVShow> previewMovieListIterator = previewTVShows.listIterator();
        while (previewMovieListIterator.hasNext()) {
            PreviewTVShow previewTVShow = previewMovieListIterator.next();
            if (previewTVShow.getBackdropPath() == null) {
                previewMovieListIterator.remove();
            }
        }
        return true;
    }

    private void checkAccount() {
        Account account = shared.getObject(Shared.ACCOUNT_KEY, null, Account.class);
        this.account.setValue(account);
        accountId = account.getId();
        if (account.getId() != 0) {
            sessionId = shared.getObject(Shared.SESSION_KEY, null, SessionResponse.class).getSessionId();
            accessRatedMovies(1);
            accessRatedShows(1);
        } else {
            isLoading.setValue(false);
        }
    }

    public void accessRatedMovies(int page) {
        Disposable disposable = userRepository
                .getRatedMovies(accountId, sessionId, page)
                .map(BaseMovieResponse::getResults)
                .filter(this::filterMovies)
                .subscribe(movies -> {
                    ratedMovies.setValue(movies);
                    isLoading.setValue(false);
                });
        compositeDisposable.add(disposable);
    }
    public void accessRatedShows(int page) {
        Disposable disposable = userRepository
                .getRatedShows(accountId, sessionId, page)
                .map(BaseTVShowResponse::getResults)
                .filter(this::filterShows)
                .subscribe(shows -> {
                    ratedShows.setValue(shows);
                    isLoading.setValue(false);
                });
        compositeDisposable.add(disposable);
    }

    public void logout() {
        shared.clearObject(Shared.GUEST_KEY);
        shared.clearObject(Shared.SESSION_KEY);
    }

}
