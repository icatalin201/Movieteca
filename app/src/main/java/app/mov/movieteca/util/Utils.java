package app.mov.movieteca.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.model.response.PreviewTVShow;

public class Utils {

    public static PreviewMovie getRandomMovie(List<PreviewMovie> previewMovieList) {
        PreviewMovie previewMovie = previewMovieList.get(new Random().nextInt(previewMovieList.size()));
        if (previewMovie.getBackdropPath() == null) {
            return getRandomMovie(previewMovieList);
        }
        return previewMovie;
    }

    public static PreviewTVShow getRandomShow(List<PreviewTVShow> previewTVShowList) {
        PreviewTVShow previewTVShow = previewTVShowList.get(new Random().nextInt(previewTVShowList.size()));
        if (previewTVShow.getBackdropPath() == null) {
            return getRandomShow(previewTVShowList);
        }
        return previewTVShow;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String formatValue(Integer value){
        int length = value.toString().length();
        String result;
        if (length < 7 || length > 12){
            result = value.toString();
        }
        else if (length == 7){
            result = value.toString().substring(0, 1) + " millions";
        }
        else if (length == 8){
            result = value.toString().substring(0, 2) + " millions";
        }
        else if (length == 9){
            result = value.toString().substring(0, 3) + " millions";
        }
        else if (length == 10){
            result = value.toString().substring(0, 1) + " billions";
        }
        else if (length == 11){
            result = value.toString().substring(0, 2) + " billions";
        }
        else {
            result = value.toString().substring(0, 3) + " billions";
        }
        return result;
    }

    public static int convertMillisToDays(long millis) {
        return (int) (millis / (1000 * 60 * 60 * 24));
    }

    public static int getDifferenceInDays(Calendar current, Calendar target) {
        long millis = target.getTimeInMillis() - current.getTimeInMillis();
        return convertMillisToDays(millis);
    }

    public static int getDifferenceInDays(Date current, Date target) {
        long millis = target.getTime() - current.getTime();
        return convertMillisToDays(millis);
    }

    public static void notify(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
