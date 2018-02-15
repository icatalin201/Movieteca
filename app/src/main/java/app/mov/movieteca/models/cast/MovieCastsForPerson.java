package app.mov.movieteca.models.cast;

import java.util.List;

/**
 * Created by Catalin on 12/23/2017.
 */

public class MovieCastsForPerson {

    private String character;
    private String credit_id;
    private String release_date;
    private Integer vote_count;
    private Boolean video;
    private Boolean adult;
    private Double vote_average;
    private String title;
    private List<Integer> genre_ids;
    private String original_language;
    private String original_title;
    private Double popularity;
    private Integer id;
    private String backdrop_path;
    private String overview;
    private String poster_path;

    public MovieCastsForPerson(String character, String credit_id, String release_date, Integer vote_count,
                               Boolean video, Boolean adult, Double vote_average, String title,
                               List<Integer> genre_ids, String original_language, String original_title,
                               Double popularity, Integer id, String backdrop_path, String overview, String poster_path) {
        this.character = character;
        this.credit_id = credit_id;
        this.release_date = release_date;
        this.vote_count = vote_count;
        this.video = video;
        this.adult = adult;
        this.vote_average = vote_average;
        this.title = title;
        this.genre_ids = genre_ids;
        this.original_language = original_language;
        this.original_title = original_title;
        this.popularity = popularity;
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.poster_path = poster_path;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
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
}
