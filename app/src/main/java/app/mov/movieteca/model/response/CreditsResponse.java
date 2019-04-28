package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditsResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("cast")
    private List<MovieCast> cast;

    @SerializedName("crew")
    private List<MovieCrew> crew;

    public CreditsResponse() { }

    public CreditsResponse(Integer id, List<MovieCast> cast, List<MovieCrew> crew) {
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
