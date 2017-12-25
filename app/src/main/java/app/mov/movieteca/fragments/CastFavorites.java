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
import app.mov.movieteca.adapters.CastAdapter;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.models.cast.Person;

/**
 * Created by Catalin on 12/24/2017.
 */

public class CastFavorites extends Fragment {

    private CastAdapter adapter;
    private RecyclerView favRecycler;
    private LinearLayout empty;
    private List<Person> personList;

    public CastFavorites() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_cast_fragment, container, false);
        empty = (LinearLayout)rootView.findViewById(R.id.fav_casts_empty);
        favRecycler = (RecyclerView)rootView.findViewById(R.id.recycler_view_fav_casts);
        personList = new ArrayList<>();
        adapter = new CastAdapter(getContext(), personList);
        favRecycler.setAdapter(adapter);
        favRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        loadData();
        return rootView;
    }

    public void loadData(){
        List<Person> casts = Handler.getCastFavoritesList(getContext());
        if (casts == null || casts.isEmpty()){
            empty.setVisibility(View.VISIBLE);
            return;
        }
        for (Person cast : casts){
            personList.add(cast);
        }
        adapter.notifyDataSetChanged();
    }

}
