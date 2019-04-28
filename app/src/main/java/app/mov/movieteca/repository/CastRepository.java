package app.mov.movieteca.repository;

import javax.inject.Inject;

import app.mov.movieteca.api.CastService;
import app.mov.movieteca.model.response.MovieCastsDetails;
import app.mov.movieteca.model.response.Person;
import app.mov.movieteca.model.response.PersonResponse;
import app.mov.movieteca.model.response.ShowCastsDetails;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CastRepository extends BaseRepository {

    private final CastService castService;

    @Inject
    public CastRepository(CastService castService) {
        this.castService = castService;
    }

    public Single<MovieCastsDetails> getMovieCastsOfPerson(int personId) {
        return castService
                .getMovieCastsOfPerson(personId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<ShowCastsDetails> getTVCastsOfPerson(int personId) {
        return castService
                .getTVCastsOfPerson(personId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<Person> getDetails(int personId) {
        return castService
                .getPersonDetails(personId, APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<PersonResponse> getPopulars(int page) {
        return castService
                .getPopulars(APIKEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
