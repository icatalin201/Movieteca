package app.mov.movieteca.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Catalin on 10/28/2017.
 */

public class SqlHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLHELPER";
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "movies.db";

    private static final String SQL_CREATE_FAVORITES =
            "CREATE TABLE " + SqlStructure.SqlData.FAVORITES + " (" +
                    SqlStructure.SqlData._ID + " INTEGER PRIMARY KEY," +
                    SqlStructure.SqlData.RES_ID + " INTEGER," +
                    SqlStructure.SqlData.RES_TYPE + " TEXT," +
                    SqlStructure.SqlData.NAME + " TEXT," +
                    SqlStructure.SqlData.POSTER + " TEXT)";

    private static final String SQL_DELETE_FAVMOVIES = "DROP TABLE IF EXISTS fav_movies";
    private static final String SQL_DELETE_FAVTVSERIES = "DROP TABLE IF EXISTS fav_tvseries";
    private static final String SQL_DELETE_FAVCASTS = "DROP TABLE IF EXISTS fav_casts";
    private static final String SQL_DELETE_SEENMOVIES = "DROP TABLE IF EXISTS seen_movies";
    private static final String SQL_DELETE_SEENTVSERIES = "DROP TABLE IF EXISTS seen_tvseries";

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITES);
        Log.i(TAG, "onCreate");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVMOVIES);
        db.execSQL(SQL_DELETE_FAVTVSERIES);
        db.execSQL(SQL_DELETE_FAVCASTS);
        db.execSQL(SQL_DELETE_SEENMOVIES);
        db.execSQL(SQL_DELETE_SEENTVSERIES);
        onCreate(db);
        Log.i(TAG, "onUpgrade");
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        Log.i(TAG, "onDowngrade");
    }
}
