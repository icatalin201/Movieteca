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
import app.mov.movieteca.adapters.SeenMoviesAdapter;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.models.movies.MovieShort;

/**
 * Created by Catalin on 12/24/2017.
 */

public class SeenMoviesFragment extends Fragment {

    private SeenMoviesAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout empty;
    private List<MovieShort> movieShortList;

    public SeenMoviesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.seen_movies_fragment, container, false);

        movieShortList = new ArrayList<>();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_seen_movies);
        empty = (LinearLayout)rootView.findViewById(R.id.seen_movies_empty);
        adapter = new SeenMoviesAdapter(getContext(), movieShortList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        loadData();
        return rootView;
    }

    public void loadData(){
        List<MovieShort> favs = Handler.getSeenMovieList(getContext());
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
