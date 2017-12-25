package app.mov.movieteca.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import app.mov.movieteca.R;

public class About extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ImageButton share = (ImageButton)view.findViewById(R.id.share);
        ImageButton rate = (ImageButton)view.findViewById(R.id.rate);
        ImageButton movieDb = (ImageButton)view.findViewById(R.id.moviedb);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = getContext().getPackageName();
                Intent appShareIntent = new Intent(Intent.ACTION_SEND);
                appShareIntent.setType("text/plain");
                String extraText = "Hey! Check out this amazing app on Play Store. \n";
                extraText += "https://play.google.com/store/apps/details?id=" + packageName;
                appShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
                startActivity(appShareIntent);
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = getContext().getPackageName();
                String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName;
                Intent appRateUsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
                startActivity(appRateUsIntent);
            }
        });

        movieDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieDbLink = "https://www.themoviedb.org/";
                Intent movieDbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieDbLink));
                startActivity(movieDbIntent);
            }
        });

        return view;
    }
}
