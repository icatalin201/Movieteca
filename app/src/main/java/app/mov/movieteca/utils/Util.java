package app.mov.movieteca.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.mov.movieteca.R;

import static android.content.Context.MODE_PRIVATE;

public class Util {
    
    public interface Constants {
        String API_KEY = "01a0f811358384a096f01f4ec867116b";
        String MOVIE_ID = "movie_id";
        String TV_SHOW_ID = "tv_show_id";
        String CAST_ID = "cast_id";
        String SEARCH = "search";
        String RATING_SYMBOL = "\u2605";
        String IMAGE_LOADING_BASE_URL_1000 = "https://image.tmdb.org/t/p/original/";
        String IMAGE_LOADING_BASE_URL_342 = "https://image.tmdb.org/t/p/w342/";
        String IMAGE_LOADING_BASE_URL_780 = "https://image.tmdb.org/t/p/w780/";
        String YOUTUBE_WATCH_BASE_URL = "https://www.youtube.com/watch?v=";
        String YOUTUBE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";
        String YOUTUBE_THUMBNAIL_IMAGE_QUALITY = "/hqdefault.jpg";
        String IMDB_BASE_URL = "http://www.imdb.com/title/";
        String MOVIE = "movie";
        String TV_SHOW = "tv_show";
        String ACTOR = "actor";
    }

    public interface DateFormats {
        String STANDARD_FORMAT = "dd-MM-yyyy";
        String EXTENDED_STANDARD_FORMAT = "dd-MMM-yyyy";
        String FULL_EXTENDED_STANDARD_FORMAT = "dd-MMMM-yyyy";
        String INVERSE_FORMAT = "yyyy-MM-dd";
        String EXTENDED_INVERSE_FORMAT = "yyyy-MMM-dd";
        String FULL_EXTENDED_INVERSE_FORMAT = "yyyy-MMMM-dd";
        String YEAR_FORMAT = "yyyy";
    }

    public static void customAnimation(Context context, View view, int duration, int anim) {
        Animation animation = AnimationUtils.loadAnimation(context, anim);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void changeFragment(Context context, Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context)
                .getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.FragmentContainer, fragment).commit();
    }

    public static void changeFragmentWithArguments(Context context, Fragment fragment, Bundle args) {
        FragmentManager fragmentManager = ((AppCompatActivity) context)
                .getSupportFragmentManager();
        fragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.FragmentContainer, fragment).commit();
    }

    public static void putInSharedPreferences(String pref_name, Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pref_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getFromSharedPreferences(String pref_name, Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(pref_name, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
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

    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static Date convertStringToDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDateFromFormatToFormat(String date, String initialFormat, String targetFormat) {
        Date date1 = convertStringToDate(date, initialFormat);
        return convertDateToString(date1, targetFormat);
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
