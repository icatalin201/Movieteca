package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.mov.movieteca.model.PreviewMovie;

public class BaseMovieMediaResponse {

    @SerializedName("total_results")
    private Integer totalResults;

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<PreviewMovie> results;

    @Override
    public Integer getTotalResults() {
        return totalResults;
    }

    @Override
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public Integer getTotalPages() {
        return totalPages;
    }

    @Override
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public Integer getPage() {
        return page;
    }

    @Override
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
