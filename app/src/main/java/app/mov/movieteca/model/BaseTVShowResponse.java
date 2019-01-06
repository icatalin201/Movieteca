package app.mov.movieteca.model;

import java.util.List;

public class BaseTVShowResponse extends BaseResponse {

    private List<PreviewTVShow> results;

    public BaseTVShowResponse(Integer page, List<PreviewTVShow> results,
                              Integer total_results, Integer total_pages) {
        this.page = page;
        this.results = results;
        this.total_results = total_results;
        this.total_pages = total_pages;
    }

    public List<PreviewTVShow> getResults() {
        return results;
    }

    public void setResults(List<PreviewTVShow> results) {
        this.results = results;
    }
}
