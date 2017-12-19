package app.mov.movieteca.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.mov.movieteca.database.SqlStructure;

/**
 * Created by Catalin on 10/28/2017.
 */

public class SqlHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLHELPER";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    private static final String SQL_CREATE_FAVMOVIES =
            "CREATE TABLE " + SqlStructure.SqlData.FAV_MOVIES + " (" +
                    SqlStructure.SqlData._ID + " INTEGER PRIMARY KEY," +
                    SqlStructure.SqlData.movie_id + " INTEGER," +
                    SqlStructure.SqlData.name + " TEXT," +
                    SqlStructure.SqlData.poster + " TEXT)";
    private static final String SQL_CREATE_FAVTVSERIES =
            "CREATE TABLE " + SqlStructure.SqlData.FAV_TVSERIES + " (" +
                    SqlStructure.SqlData._ID + " INTEGER PRIMARY KEY," +
                    SqlStructure.SqlData.tv_series_id + " INTEGER," +
                    SqlStructure.SqlData.name + " TEXT," +
                    SqlStructure.SqlData.poster + " TEXT)";

    private static final String SQL_DELETE_FAVMOVIES =
            "DROP TABLE IF EXISTS " + SqlStructure.SqlData.FAV_MOVIES;
    private static final String SQL_DELETE_FAVTVSERIES =
            "DROP TABLE IF EXISTS " + SqlStructure.SqlData.FAV_TVSERIES;

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVMOVIES);
        db.execSQL(SQL_CREATE_FAVTVSERIES);
        Log.i(TAG, "onCreate");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVMOVIES);
        db.execSQL(SQL_DELETE_FAVTVSERIES);
        onCreate(db);
        Log.i(TAG, "onUpgrade");
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        Log.i(TAG, "onDowngrade");
    }
}
