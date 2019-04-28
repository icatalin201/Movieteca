package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseMovieResponse {

    @SerializedName("total_results")
    private Integer totalResults;

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<PreviewMovie> results;

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<PreviewMovie> getResults() {
        return results;
    }

    public void setResults(List<PreviewMovie> results) {
        this.results = results;
    }
}
