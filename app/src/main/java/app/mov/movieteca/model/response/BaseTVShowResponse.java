package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.mov.movieteca.model.PreviewTVShow;

public class BaseTVShowMediaResponse {

    @SerializedName("total_results")
    private Integer totalResults;

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<PreviewMovie> results;
}
