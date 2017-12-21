package app.mov.movieteca.utils;

import java.util.HashMap;
import java.util.List;

import app.mov.movieteca.models.movies.Genres;

/**
 * Created by Catalin on 12/21/2017.
 */

public class GenresUtil {

    private static HashMap<Integer, String> genresMap;

    public static boolean isGenresListLoaded() {
        return (genresMap != null);
    }

    public static void loadGenresList(List<Genres> genres) {
        if (genres == null) return;
        genresMap = new HashMap<>();
        for (Genres genre : genres) {
            genresMap.put(genre.getId(), genre.getName());
        }
    }

    public static String getGenreName(Integer genreId) {
        if (genreId == null) return null;
        return genresMap.get(genreId);
    }

}
