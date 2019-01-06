package app.mov.movieteca.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.model.FavoritePreviewMedia;

/**
 * Created by Catalin on 12/23/2017.
 */

public class Handler {

    public static FavoritePreviewMedia insertFavorite(Context context, FavoritePreviewMedia favoritePreviewMedia) {
        SQLiteDatabase sqLiteDatabase = new SqlHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlStructure.SqlData.RES_ID, favoritePreviewMedia.getResId());
        contentValues.put(SqlStructure.SqlData.RES_TYPE, favoritePreviewMedia.getResType());
        contentValues.put(SqlStructure.SqlData.NAME, favoritePreviewMedia.getName());
        contentValues.put(SqlStructure.SqlData.POSTER, favoritePreviewMedia.getPoster());
        long id = sqLiteDatabase.insert(SqlStructure.SqlData.FAVORITES, null, contentValues);
        sqLiteDatabase.close();
        favoritePreviewMedia.setId(id);
        return favoritePreviewMedia;
    }

    public static void removeFavorite(Context context, Integer id, String type) {
        SQLiteDatabase sqLiteDatabase = new SqlHelper(context).getWritableDatabase();
        sqLiteDatabase.delete(SqlStructure.SqlData.FAVORITES,
                SqlStructure.SqlData.RES_ID + " = ? AND " +
                        SqlStructure.SqlData.RES_TYPE + " = ?",
                new String[] { String.valueOf(id), type });
        sqLiteDatabase.close();
    }

    public static boolean isFavorite(Context context, long resId, String resType) {
        boolean exists = false;
        SQLiteDatabase sqLiteDatabase = new SqlHelper(context).getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(SqlStructure.SqlData.FAVORITES, null,
                SqlStructure.SqlData.RES_ID + " = ? AND " +
                        SqlStructure.SqlData.RES_TYPE + " = ? ",
                new String[] {String.valueOf(resId), resType}, null, null,
                null);
        exists = cursor.getCount() > 0;
        cursor.close();
        sqLiteDatabase.close();
        return exists;
    }

    public static List<FavoritePreviewMedia> getFavorites(Context context, String type) {
        List<FavoritePreviewMedia> favoritePreviewMedia = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = new SqlHelper(context).getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(SqlStructure.SqlData.FAVORITES, null,
                SqlStructure.SqlData.RES_TYPE + " = ?",
                new String[]{type}, null, null, null);
        while (cursor.moveToNext()) {
            FavoritePreviewMedia favoritePreviewMedia1 = new FavoritePreviewMedia();
            favoritePreviewMedia1.setId(cursor.getLong(cursor.getColumnIndex(SqlStructure.SqlData._ID)));
            favoritePreviewMedia1.setName(cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.NAME)));
            favoritePreviewMedia1.setPoster(cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.POSTER)));
            favoritePreviewMedia1.setResId(cursor.getInt(cursor.getColumnIndex(SqlStructure.SqlData.RES_ID)));
            favoritePreviewMedia1.setResType(cursor.getString(cursor.getColumnIndex(SqlStructure.SqlData.RES_TYPE)));
            favoritePreviewMedia.add(favoritePreviewMedia1);
        }
        cursor.close();
        sqLiteDatabase.close();
        return favoritePreviewMedia;
    }

}
