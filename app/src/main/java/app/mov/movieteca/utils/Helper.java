package app.mov.movieteca.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.mov.movieteca.R;

/**
 * Created by Catalin on 12/22/2017.
 */

public class Helper {

    public static String formatDate(String dateString){
        Date date = null;
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(date);
    }

    public static void changeFragment(Context context, Fragment fragment) {
        Fragment frag = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.FragmentContainer);
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction().hide(frag).add(R.id.FragmentContainer, fragment).addToBackStack(null).commit();
     }

    public static String formatValue(Integer value){
        int length = value.toString().length();
        String result = null;
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
        else if (length == 12){
            result = value.toString().substring(0, 3) + " billions";
        }
        return result;
    }

    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        private ImageView img;

        public GetImage(ImageView img){
            this.img = img;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urldisplay = strings[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            img.setImageBitmap(bitmap);
        }
    }
}
