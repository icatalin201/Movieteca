package app.mov.movieteca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.mov.movieteca.R;

/**
 * Created by Catalin on 12/23/2017.
 */

public class ShowsFavorites extends Fragment {

    public ShowsFavorites() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_shows_fragment, container, false);
        return rootView;
    }
}
