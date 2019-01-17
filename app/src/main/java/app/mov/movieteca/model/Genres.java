package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Catalin on 12/7/2017.
 */

public class Genres {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    public Genres(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
