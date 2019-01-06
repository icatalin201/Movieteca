package app.mov.movieteca.database;

import android.provider.BaseColumns;

/**
 * Created by Catalin on 10/28/2017.
 */

public class SqlStructure {

    private SqlStructure(){}

    public static class SqlData implements BaseColumns {

        public static final String FAVORITES = "favorites";
        public static final String RES_ID = "resource_id";
        public static final String RES_TYPE = "resource_type";
        public static final String NAME = "name";
        public static final String POSTER = "poster";
    }

}
