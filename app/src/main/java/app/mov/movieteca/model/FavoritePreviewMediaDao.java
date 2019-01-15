package app.mov.movieteca.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoritePreviewMediaDao {

    @Insert
    void insertFavorite(FavoritePreviewMedia... favoritePreviewMedia);

    @Delete
    void deleteFavorite(FavoritePreviewMedia... favoritePreviewMedia);

    @Update
    void updateFavorite(FavoritePreviewMedia... favoritePreviewMedia);

    @Query("DELETE FROM favorites WHERE res_id = :resId AND res_type = :resType")
    void removeFavoriteByQuery(Integer resId, String resType);

    @Query("SELECT * FROM favorites WHERE res_id = :resId AND res_type = :resType LIMIT 1")
    FavoritePreviewMedia isFavorite(Integer resId, String resType);

    @Query("SELECT * FROM favorites WHERE res_type = :type")
    List<FavoritePreviewMedia> getFavorites(String type);
}
