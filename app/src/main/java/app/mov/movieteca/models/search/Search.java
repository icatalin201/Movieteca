package app.mov.movieteca.models.search;

import java.util.List;

/**
 * Created by Catalin on 12/24/2017.
 */

public class Search {

    private Integer page;
    private List<SearchResults> results;
    private Integer total_pages;

    public Search() {}

    public Search(Integer page, List<SearchResults> results, Integer total_pages) {
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
    }

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

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }
}
