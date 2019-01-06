package app.mov.movieteca.model;

import java.util.List;

/**
 * Created by Catalin on 12/20/2017.
 */

public class TVShow {

    private String backdrop_path;
    private String first_air_date;
    private List<Genres> genres;
    private Integer id;
    private String type;
    private String original_name;
    private Integer number_of_episodes;
    private Integer number_of_seasons;
    private String overview;
    private String poster_path;
    private Double vote_average;

    public TVShow(String backdrop_path, String first_air_date,
                  List<Genres> genres, Integer id, String type, String original_name,
                  Integer number_of_episodes, Integer number_of_seasons, String overview, String poster_path, Double vote_average) {
        this.backdrop_path = backdrop_path;
        this.first_air_date = first_air_date;
        this.genres = genres;
        this.id = id;
        this.type = type;
        this.original_name = original_name;
        this.number_of_episodes = number_of_episodes;
        this.number_of_seasons = number_of_seasons;
        this.overview = overview;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public Integer getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(Integer number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public Integer getNumber_of_seasons() {
        return number_of_seasons;
    }

    public void setNumber_of_seasons(Integer number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }
}
