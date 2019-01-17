package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Catalin on 12/23/2017.
 */

public class ShowCastsForPerson extends BaseMediaForPerson {

    @SerializedName("id")
    private Integer id;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("credit_id")
    private String creditId;

    @SerializedName("original_name")
    private String originalName;

    @SerializedName("character")
    private String character;

    public ShowCastsForPerson(Integer id, String backdropPath,
                              String creditId, String originalName, String character) {
        this.id = id;
        this.backdropPath = backdropPath;
        this.creditId = creditId;
        this.originalName = originalName;
        this.character = character;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
