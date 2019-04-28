package app.mov.movieteca.model.request;

import com.google.gson.annotations.SerializedName;

public class FavoriteRequest {

    @SerializedName("media_type")
    private String mediaType;
    @SerializedName("media_id")
    private int mediaId;
    @SerializedName("favorite")
    private boolean favorite;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
