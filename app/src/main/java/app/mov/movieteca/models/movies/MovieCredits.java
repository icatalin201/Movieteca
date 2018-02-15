package app.mov.movieteca.models.movies;

import java.util.List;

/**
 * Created by Catalin on 12/20/2017.
 */

public class MovieCredits {

    private Integer id;
    private List<MovieCast> cast;
    private List<MovieCrew> crew;

    public MovieCredits(Integer id, List<MovieCast> cast, List<MovieCrew> crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieCast> getCast() {
        return cast;
    }

    public void setCast(List<MovieCast> cast) {
        this.cast = cast;
    }

    public List<MovieCrew> getCrew() {
        return crew;
    }

    public void setCrew(List<MovieCrew> crew) {
        this.crew = crew;
    }
}
