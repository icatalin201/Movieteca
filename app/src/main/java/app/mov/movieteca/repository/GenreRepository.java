package app.mov.movieteca.repository;

import javax.inject.Inject;

import app.mov.movieteca.api.GenreService;
import app.mov.movieteca.model.response.GenreResponse;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GenreRepository extends BaseRepository {

    private final GenreService genreService;

    @Inject
    public GenreRepository(GenreService genreService) {
        this.genreService = genreService;
    }

    public Single<GenreResponse> findGenresForMovies() {
        return genreService
                .getMovieGenresList(APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<GenreResponse> findGenresForSeries() {
        return genreService
                .getTVShowGenresList(APIKEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
