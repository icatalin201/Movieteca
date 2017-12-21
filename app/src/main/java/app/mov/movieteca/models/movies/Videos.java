package app.mov.movieteca.models.movies;

import java.util.List;

/**
 * Created by Catalin on 12/20/2017.
 */

public class Videos {

    private Integer id;
    private List<VideoInfo> results;

    public Videos(Integer id, List<VideoInfo> results) {
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
