package app.mov.movieteca.model;

public class FavoritePreviewMedia {

    private long id;
    private Integer resId;
    private String resType;
    private String name;
    private String poster;

    public FavoritePreviewMedia() { }

    public FavoritePreviewMedia(long id, Integer resId, String resType, String name, String poster) {
        this.id = id;
        this.resId = resId;
        this.resType = resType;
        this.name = name;
        this.poster = poster;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
