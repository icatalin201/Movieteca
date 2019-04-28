package app.mov.movieteca.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Catalin on 12/7/2017.
 */

public class Movie extends Media {

    @SerializedName("budget")
    private Integer budget;

    @SerializedName("genres")
    private List<Genres> genres;

    @SerializedName("id")
    private Integer id;

    @SerializedName("imdb_id")
    private String imdbId;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("revenue")
    private Integer revenue;

    @SerializedName("runtime")
    private Integer runtime;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("title")
    private String title;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Integer voteCount;

    public Movie(Integer budget, List<Genres> genres, Integer id, String imdbId, String originalTitle,
                 String overview, Double popularity, String backdropPath, String posterPath,
                 String releaseDate, Integer revenue, Integer runtime, String tagline, String title,
                 Double voteAverage, Integer voteCount) {
        this.budget = budget;
        this.genres = genres;
        this.id = id;
        this.imdbId = imdbId;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.tagline = tagline;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
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

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
