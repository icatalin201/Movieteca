package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Catalin on 12/24/2017.
 */

public class Search {

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<SearchResults> results;

    @SerializedName("total_pages")
    private Integer totalPages;

    public Search() {}

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<SearchResults> getResults() {
        return results;
    }

    public void setResults(List<SearchResults> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
