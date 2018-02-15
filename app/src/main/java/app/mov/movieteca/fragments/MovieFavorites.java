package app.mov.movieteca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.FullListMovieAdapter;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.models.movies.MovieShort;

/**
 * Created by Catalin on 12/23/2017.
 */

public class MovieFavorites extends Fragment {

    private FullListMovieAdapter adapter;
    private RecyclerView favRecycler;
    private LinearLayout empty;
    private List<MovieShort> movieShortList;

    public MovieFavorites() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_movie_fragment, container, false);

        movieShortList = new ArrayList<>();
        favRecycler = (RecyclerView)rootView.findViewById(R.id.recycler_view_fav_movies);
        empty = (LinearLayout)rootView.findViewById(R.id.fav_movies_empty);
        adapter = new FullListMovieAdapter(getContext(), movieShortList);
        favRecycler.setAdapter(adapter);
        favRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        loadFavorites();
        return rootView;
    }

    public void loadFavorites(){
        List<MovieShort> favs = Handler.getMovieFavoritesList(getContext());
        if (favs == null || favs.isEmpty()){
            empty.setVisibility(View.VISIBLE);
            return;
        }
        for (MovieShort favMovie : favs){
            movieShortList.add(favMovie);
        }
        adapter.notifyDataSetChanged();
    }
}