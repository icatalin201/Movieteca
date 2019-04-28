package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Catalin on 12/23/2017.
 */

public class ShowCastsDetails {

    @SerializedName("cast")
    private List<ShowCastsForPerson> cast;

    @SerializedName("id")
    private Integer id;

    public ShowCastsDetails(List<ShowCastsForPerson> cast, Integer id) {
        this.cast = cast;
        this.id = id;
    }

    public List<ShowCastsForPerson> getCast() {
        return cast;
    }

    public void setCast(List<ShowCastsForPerson> cast) {
        this.cast = cast;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
