package app.mov.movieteca.database;

import android.provider.BaseColumns;

/**
 * Created by Catalin on 10/28/2017.
 */

public class SqlStructure {

    private SqlStructure(){}

    public static class SqlData implements BaseColumns {
        public static final String FAV_MOVIES = "fav_movies";
        public static final String FAV_TVSERIES = "fav_tvseries";
        public static final String FAV_CASTS = "fav_casts";

        public static final String SEEN_MOVIES = "seen_movies";
        public static final String SEEN_TVSERIES = "seen_tvseries";

        public static final String movie_id = "movie_id";
        public static final String cast_id = "cast_id";
        public static final String tv_series_id = "tv_series_id";

        public static final String name = "name";
        public static final String poster = "poster";
    }

}
