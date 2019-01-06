package app.mov.movieteca.model;

import java.util.List;

public class BaseMovieResponse extends BaseResponse {

    private List<PreviewMovie> results;

    public BaseMovieResponse() { }

    public BaseMovieResponse(Integer page, Integer total_pages,
                             Integer total_results, List<PreviewMovie> results) {
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
        this.results = results;
    }

    public List<PreviewMovie> getResults() {
        return results;
    }

    public void setResults(List<PreviewMovie> results) {
        this.results = results;
    }
}
