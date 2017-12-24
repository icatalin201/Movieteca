package app.mov.movieteca.models.tvshows;

import java.util.List;

/**
 * Created by Catalin on 12/24/2017.
 */

public class TVShowShort {

    private Integer id;
    private String backdrop_path;
    private Double vote_average;
    private List<Integer> genre_ids;
    private String original_name;

    public TVShowShort(Integer id, String backdrop_path, Double vote_average, List<Integer> genre_ids, String original_name) {
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.genre_ids = genre_ids;
        this.original_name = original_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }
}
