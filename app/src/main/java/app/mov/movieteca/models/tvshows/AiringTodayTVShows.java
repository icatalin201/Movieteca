package app.mov.movieteca.models.tvshows;

import java.util.List;

/**
 * Created by Catalin on 12/20/2017.
 */

public class AiringTodayTVShows {

    private Integer page;
    private List<TVShowShort> results;
    private Integer total_results;
    private Integer total_pages;

    public AiringTodayTVShows(Integer page, List<TVShowShort> results, Integer total_results, Integer total_pages) {
        this.page = page;
        this.results = results;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<TVShowShort> getResults() {
        return results;
    }

    public void setResults(List<TVShowShort> results) {
        this.results = results;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }
}
