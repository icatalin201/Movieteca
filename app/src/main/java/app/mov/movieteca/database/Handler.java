package app.mov.movieteca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.models.cast.Person;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.models.tvshows.TVShowShort;

/**
 * Created by Catalin on 12/23/2017.
 */

public class Handler {

    public static void addToFavorites(Context context, String movieType, Integer id, String title, String imagePath) {
        if (movieType.isEmpty() || movieType == null) return;
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch (movieType){
            case "movie":
                if (!isFav(context, movieType, id)) {
                    contentValues.put(SqlStructure.SqlData.movie_id, id);
                    contentValues.put(SqlStructure.SqlData.name, title);
                    contentValues.put(SqlStructure.SqlData.poster, imagePath);
                    database.insert(SqlStructure.SqlData.FAV_MOVIES, null, contentValues);
                }
                break;
            case "tv_show":
                if (!isFav(context, movieType, id)) {
                    contentValues.put(SqlStructure.SqlData.tv_series_id, id);
                    contentValues.put(SqlStructure.SqlData.name, title);
                    contentValues.put(SqlStructure.SqlData.poster, imagePath);
                    database.insert(SqlStructure.SqlData.FAV_TVSERIES, null, contentValues);
                }
                break;
            case "cast":
                if (!isFav(context, movieType, id)) {
                    contentValues.put(SqlStructure.SqlData.cast_id, id);
                    contentValues.put(SqlStructure.SqlData.name, title);
                    contentValues.put(SqlStructure.SqlData.poster, imagePath);
                    database.insert(SqlStructure.SqlData.FAV_CASTS, null, contentValues);
                }
                break;
        }
        database.close();
    }

    public static void removeFromFavorites(Context context, String movieType, Integer id) {
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        switch (movieType){
            case "movie":
                database.delete(SqlStructure.SqlData.FAV_MOVIES, SqlStructure.SqlData.movie_id + " = " + id, null);
                break;
            case "tv_show":
                database.delete(SqlStructure.SqlData.FAV_TVSERIES, SqlStructure.SqlData.tv_series_id + " = " + id, null);
                break;
            case "cast":
                database.delete(SqlStructure.SqlData.FAV_CASTS, SqlStructure.SqlData.cast_id + " = " + id, null);
                break;
        }
        database.close();
    }

    public static boolean isFav(Context context, String movieType, Integer id) {
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
            case "cast":
                cursor = database.query(SqlStructure.SqlData.FAV_CASTS, null,
                        SqlStructure.SqlData.cast_id + " = " + id, null, null, null, null);
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
                null, null, SqlStructure.SqlData._ID + " DESC");
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

    public static List<Person> getCastFavoritesList(Context context){
        List<Person> favCasts = new ArrayList<>();
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(SqlStructure.SqlData.FAV_CASTS,null, null, null,
                null, null, SqlStructure.SqlData._ID + " DESC");
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SqlStructure.SqlData.cast_id));
            String title = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.name));
            String image = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.poster));
            favCasts.add(new Person(null, null, null, null, id, null, title, null, image));
        }
        cursor.close();
        database.close();
        return favCasts;
    }

    public static void addToSeen(Context context, String movieType, Integer id, String title, String imagePath){
        if (movieType.isEmpty() || movieType == null) return;
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch (movieType){
            case "movie":
                if (!isSeen(context, movieType, id)) {
                    contentValues.put(SqlStructure.SqlData.movie_id, id);
                    contentValues.put(SqlStructure.SqlData.name, title);
                    contentValues.put(SqlStructure.SqlData.poster, imagePath);
                    database.insert(SqlStructure.SqlData.SEEN_MOVIES, null, contentValues);
                }
                break;
            case "tv_show":
                if (!isSeen(context, movieType, id)) {
                    contentValues.put(SqlStructure.SqlData.tv_series_id, id);
                    contentValues.put(SqlStructure.SqlData.name, title);
                    contentValues.put(SqlStructure.SqlData.poster, imagePath);
                    database.insert(SqlStructure.SqlData.SEEN_TVSERIES, null, contentValues);
                }
                break;
        }
        database.close();
    }

    public static void removeFromSeen(Context context, String movieType, Integer id){
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        switch (movieType){
            case "movie":
                database.delete(SqlStructure.SqlData.SEEN_MOVIES, SqlStructure.SqlData.movie_id + " = " + id, null);
                break;
            case "tv_show":
                database.delete(SqlStructure.SqlData.SEEN_TVSERIES, SqlStructure.SqlData.tv_series_id + " = " + id, null);
                break;
        }
        database.close();
    }

    public static boolean isSeen(Context context, String movieType, Integer id){
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        boolean seen = false;
        Cursor cursor = null;
        switch (movieType){
            case "movie":
                cursor = database.query(SqlStructure.SqlData.SEEN_MOVIES, null,
                        SqlStructure.SqlData.movie_id + " = " + id, null, null, null, null);
                break;
            case "tv_show":
                cursor = database.query(SqlStructure.SqlData.SEEN_TVSERIES, null,
                        SqlStructure.SqlData.tv_series_id + " = " + id, null, null, null, null);
                break;
        }
        if (cursor.getCount() == 1) seen = true;
        cursor.close();
        database.close();
        return seen;
    }

    public static List<MovieShort> getSeenMovieList(Context context){
        List<MovieShort> seenMovies = new ArrayList<>();
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(SqlStructure.SqlData.SEEN_MOVIES,null, null, null,
                null, null, SqlStructure.SqlData._ID + " DESC");
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SqlStructure.SqlData.movie_id));
            String title = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.name));
            String image = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.poster));
            seenMovies.add(new MovieShort(null, id, null, null, null, null, null,
                    null, title, null, image, null, null, null));
        }
        cursor.close();
        database.close();
        return seenMovies;
    }

    public static List<TVShowShort> getSeenShowsList(Context context){
        List<TVShowShort> seenShows = new ArrayList<>();
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(SqlStructure.SqlData.SEEN_TVSERIES,null, null, null,
                null, null, SqlStructure.SqlData._ID + " DESC");
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(SqlStructure.SqlData.tv_series_id));
            String title = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.name));
            String image = cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.poster));
            seenShows.add(new TVShowShort(id, image, null, null, title));
        }
        cursor.close();
        database.close();
        return seenShows;
    }
}
