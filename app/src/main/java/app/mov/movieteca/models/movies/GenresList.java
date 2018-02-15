package app.mov.movieteca.models.movies;

import java.util.List;

/**
 * Created by Catalin on 12/20/2017.
 */

public class GenresList {

    private List<Genres> genres;

    public GenresList(List<Genres> genres) {
        this.genres = genres;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }
}
