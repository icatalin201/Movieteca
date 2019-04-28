package app.mov.movieteca.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("DELETE FROM favorites")
    void delete();
}
