package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Catalin on 12/24/2017.
 */

public class SearchResults {

    @SerializedName("id")
    private Integer id;

    @SerializedName("poster")
    private String poster;

    @SerializedName("name")
    private String name;

    @SerializedName("media_type")
    private String mediaType;

    public SearchResults() {}

    public SearchResults(Integer id, String poster, String name, String mediaType) {
        this.id = id;
        this.poster = poster;
        this.name = name;
        this.mediaType = mediaType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
