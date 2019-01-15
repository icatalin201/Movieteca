package app.mov.movieteca.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Objects;

import app.mov.movieteca.R;

public class More extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ImageButton share = view.findViewById(R.id.share);
        ImageButton rating = view.findViewById(R.id.rating);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = Objects.requireNonNull(getActivity()).getPackageName();
                Intent appShareIntent = new Intent(Intent.ACTION_SEND);
                appShareIntent.setType("text/plain");
                String extraText = String.format("Hi there! Try this new awesome app %s. \n",
                        getResources().getString(R.string.app_name));
                extraText += "https://play.google.com/store/apps/details?id=" + packageName;
                appShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
                startActivity(appShareIntent);
            }
        });
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName1 = Objects.requireNonNull(getActivity()).getPackageName();
                String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName1;
                Intent app = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
                startActivity(app);
            }
        });
        return view;
    }
}
