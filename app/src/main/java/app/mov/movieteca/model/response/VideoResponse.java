package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Catalin on 12/20/2017.
 */

public class VideoResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("results")
    private List<VideoInfo> results;

    public VideoResponse(Integer id, List<VideoInfo> results) {
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<VideoInfo> getResults() {
        return results;
    }

    public void setResults(List<VideoInfo> results) {
        this.results = results;
    }
}
