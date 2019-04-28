package app.mov.movieteca.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoritePreviewMedia {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "res_id")
    private Integer resId;

    @ColumnInfo(name = "res_type")
    private String resType;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "poster")
    private String poster;

    @Ignore
    public FavoritePreviewMedia() { }

    public FavoritePreviewMedia(long id, Integer resId, String resType,
                                String name, String poster) {
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
