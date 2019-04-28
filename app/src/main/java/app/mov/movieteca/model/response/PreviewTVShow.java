package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Catalin on 12/24/2017.
 */

public class PreviewTVShow {

    @SerializedName("id")
    private Integer id;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("overview")
    private String overview;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("name")
    private String name;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("original_name")
    private String originalName;

    public PreviewTVShow(Integer id, String backdropPath, Double voteAverage, Integer voteCount,
                         String overview, String firstAirDate, String name, String posterPath,
                         Double popularity, String originalName) {
        this.id = id;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.overview = overview;
        this.firstAirDate = firstAirDate;
        this.name = name;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.originalName = originalName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
