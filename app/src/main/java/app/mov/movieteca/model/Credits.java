package app.mov.movieteca.model;

import java.util.List;

public class Credits {

    private Integer id;
    private List<MovieCast> cast;
    private List<MovieCrew> crew;

    public Credits() { }

    public Credits(Integer id, List<MovieCast> cast, List<MovieCrew> crew) {
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
