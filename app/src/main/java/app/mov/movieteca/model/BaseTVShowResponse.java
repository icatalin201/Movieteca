package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseTVShowResponse extends BaseResponse {

    @SerializedName("results")
    private List<PreviewTVShow> results;

    public BaseTVShowResponse() { }

    public BaseTVShowResponse(List<PreviewTVShow> results, Integer totalPages,
                              Integer totalResults, Integer page) {
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
        this.page = page;
    }

    public List<PreviewTVShow> getResults() {
        return results;
    }

    public void setResults(List<PreviewTVShow> results) {
        this.results = results;
    }
}
