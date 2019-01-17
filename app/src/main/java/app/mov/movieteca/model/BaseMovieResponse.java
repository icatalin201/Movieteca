package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseMovieResponse extends BaseResponse {

    @SerializedName("results")
    private List<PreviewMovie> results;

    public BaseMovieResponse() { }

    public BaseMovieResponse(List<PreviewMovie> results, Integer totalPages,
                             Integer totalResults, Integer page) {
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
        this.page = page;
    }

    public List<PreviewMovie> getResults() {
        return results;
    }

    public void setResults(List<PreviewMovie> results) {
        this.results = results;
    }
}
