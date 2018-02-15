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
import app.mov.movieteca.adapters.FullListShowAdapter;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.models.tvshows.TVShowShort;

/**
 * Created by Catalin on 12/23/2017.
 */

public class ShowsFavorites extends Fragment {

    private FullListShowAdapter adapter;
    private RecyclerView favRecycler;
    private LinearLayout empty;
    private List<TVShowShort> movieShortList;

    public ShowsFavorites() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_shows_fragment, container, false);

        movieShortList = new ArrayList<>();
        favRecycler = (RecyclerView)rootView.findViewById(R.id.recycler_view_fav_shows);
        empty = (LinearLayout)rootView.findViewById(R.id.fav_shows_empty);
        adapter = new FullListShowAdapter(getContext(), movieShortList);
        favRecycler.setAdapter(adapter);
        favRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        loadFavorites();
        return rootView;
    }

    public void loadFavorites(){
        List<TVShowShort> favs = Handler.getTVShowFavoritesList(getContext());
        if (favs == null || favs.isEmpty()){
            empty.setVisibility(View.VISIBLE);
            return;
        }
        for (TVShowShort favMovie : favs){
            movieShortList.add(favMovie);
        }
        adapter.notifyDataSetChanged();
    }
}
