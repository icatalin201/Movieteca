package app.mov.movieteca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.models.tvshows.TVShow;
import app.mov.movieteca.models.tvshows.TVShowShort;

/**
 * Created by Catalin on 12/23/2017.
 */

public class FavoritesHandler {

    public static void addMovieToFavorites(Context context, String movieType, Integer id, String title, String imagePath) {
        if (movieType.isEmpty() || movieType == null) return;
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch (movieType){
            case "movie":
                if (!isMovieFav(context, movieType, id)) {
                    contentValues.put(SqlStructure.SqlData.movie_id, id);
                    contentValues.put(SqlStructure.SqlData.name, title);
                    contentValues.put(SqlStructure.SqlData.poster, imagePath);
                    database.insert(SqlStructure.SqlData.FAV_MOVIES, null, contentValues);
                }
                break;
            case "tv_show":
                if (!isMovieFav(context, movieType, id)) {
                    contentValues.put(SqlStructure.SqlData.tv_series_id, id);
                    contentValues.put(SqlStructure.SqlData.name, title);
                    contentValues.put(SqlStructure.SqlData.poster, imagePath);
                    database.insert(SqlStructure.SqlData.FAV_TVSERIES, null, contentValues);
                }
                break;
        }
        database.close();
    }

    public static void removeMovieFromFavorites(Context context, String movieType, Integer id) {
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        switch (movieType){
            case "movie":
                database.delete(SqlStructure.SqlData.FAV_MOVIES, SqlStructure.SqlData.movie_id + " = " + id, null);
                break;
            case "tv_show":
                database.delete(SqlStructure.SqlData.FAV_TVSERIES, SqlStructure.SqlData.tv_series_id + " = " + id, null);
                break;
        }
        database.close();
    }

    public static boolean isMovieFav(Context context, String movieType, Integer id) {
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        boolean fav = false;
        Cursor cursor = null;
        switch (movieType){
            case "movie":
                cursor = database.query(SqlStructure.SqlData.FAV_MOVIES, null,
                        SqlStructure.SqlData.movie_id + " = " + id, null, null, null, null);
                break;
            case "tv_show":
                cursor = database.query(SqlStructure.SqlData.FAV_TVSERIES, null,
                        SqlStructure.SqlData.tv_series_id + " = " + id, null, null, null, null);
                break;
        }
        if (cursor.getCount() == 1) fav = true;
        cursor.close();
        database.close();
        return fav;
    }

    public static List<MovieShort> getMovieFavoritesList(Context context) {
        List<MovieShort> favMovies = new ArrayList<>();
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(SqlStructure.SqlData.FAV_MOVIES,null, null, null,
                null, null, SqlStructure.SqlData._ID + " DESC");
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SqlStructure.SqlData.movie_id));
            String title = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.name));
            String image = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.poster));
            favMovies.add(new MovieShort(null, id, null, null, null, null, null,
                    null, title, null, image, null, null, null));
        }
        cursor.close();
        database.close();
        return favMovies;
    }

    public static List<TVShowShort> getTVShowFavoritesList(Context context) {
        List<TVShowShort> favShows = new ArrayList<>();
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(SqlStructure.SqlData.FAV_TVSERIES,null, null, null,
                null, null, SqlStructure.SqlData.tv_series_id + " DESC");
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SqlStructure.SqlData.tv_series_id));
            String title = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.name));
            String image = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.poster));
            favShows.add(new TVShowShort(id, image, null, null, title));
        }
        cursor.close();
        database.close();
        return favShows;
    }
}
