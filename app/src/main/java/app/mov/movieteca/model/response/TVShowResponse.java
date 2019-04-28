package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Catalin on 12/20/2017.
 */

public class TVShow extends Media {

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("genres")
    private List<Genres> genres;

    @SerializedName("id")
    private Integer id;

    @SerializedName("type")
    private String tvShowType;

    @SerializedName("original_name")
    private String originalName;

    @SerializedName("number_of_episodes")
    private Integer numberOfEpisodes;

    @SerializedName("number_of_seasons")
    private Integer numberOfSeasons;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_average")
    private Double voteAverage;

    public TVShow(String backdropPath, String firstAirDate, List<Genres> genres, Integer id,
                  String type, String originalName, Integer numberOfEpisodes,
                  Integer numberOfSeasons, String overview, String posterPath, Double voteAverage) {
        this.backdropPath = backdropPath;
        this.firstAirDate = firstAirDate;
        this.genres = genres;
        this.id = id;
        this.tvShowType = type;
        this.originalName = originalName;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
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

    public String getTvShowType() {
        return tvShowType;
    }

    public void setTvShowType(String type) {
        this.tvShowType = type;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
