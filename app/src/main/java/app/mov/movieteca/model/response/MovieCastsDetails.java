package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Catalin on 12/23/2017.
 */

public class MovieCastsDetails {

    @SerializedName("cast")
    private List<MovieCastsForPerson> cast;

    @SerializedName("id")
    private Integer id;

    public MovieCastsDetails(List<MovieCastsForPerson> cast, Integer id) {
        this.cast = cast;
        this.id = id;
    }

    public List<MovieCastsForPerson> getCast() {
        return cast;
    }

    public void setCast(List<MovieCastsForPerson> cast) {
        this.cast = cast;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
