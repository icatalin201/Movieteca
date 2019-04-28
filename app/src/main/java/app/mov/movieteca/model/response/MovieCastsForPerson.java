package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Catalin on 12/23/2017.
 */

public class MovieCastsForPerson {

    @SerializedName("character")
    private String character;

    @SerializedName("creditId")
    private String creditId;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("title")
    private String title;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("id")
    private Integer id;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    public MovieCastsForPerson(String character, String creditId, String releaseDate,
                               Integer voteCount, Double voteAverage, String title,
                               List<Integer> genreIds, String originalTitle, Double popularity,
                               Integer id, String backdropPath, String overview, String posterPath) {
        this.character = character;
        this.creditId = creditId;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.title = title;
        this.genreIds = genreIds;
        this.originalTitle = originalTitle;
        this.popularity = popularity;
        this.id = id;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
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

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
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
}
