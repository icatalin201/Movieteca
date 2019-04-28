package app.mov.movieteca.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Shared {

    private static Shared instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public static final String TOKEN_KEY = "token";
    public static final String GUEST_KEY = "guest";
    public static final String SESSION_KEY = "session";
    public static final String ACCOUNT_KEY = "account";
    public static final String SESSION_TYPE = "session_type";

    private Shared(Context context) {
        sharedPreferences = context.getSharedPreferences("movieteca", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static void init(Context context) {
        instance = new Shared(context);
    }

    public static Shared getInstance() {
        return instance;
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void storeObject(String key, Object object) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gson.toJson(object));
        editor.apply();
    }

    public void clearObject(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public <T> T getObject(String key, String defaultValue, Class<T> o) {
        String json = sharedPreferences.getString(key, defaultValue);
        return gson.fromJson(json, o);
    }

    public void storeString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key,  defaultValue);
    }
}
