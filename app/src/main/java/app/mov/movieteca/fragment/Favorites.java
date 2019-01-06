package app.mov.movieteca.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import app.mov.movieteca.R;
import app.mov.movieteca.adapter.FavoriteAdapter;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.utils.Util;

public class Favorites extends Fragment implements SearchView.OnQueryTextListener {

    private final String[] favOptions = new String[] {
            "Favorite Movies", "Favorite TV Shows", "Favorite Actors"
    };

    public static final Comparator<FavoritePreviewMedia> ALPHABETICAL_COMPARATOR =
            new Comparator<FavoritePreviewMedia>() {

        @Override
        public int compare(FavoritePreviewMedia a, FavoritePreviewMedia b) {
            return a.getName().compareTo(b.getName());
        }
    };

    private int position = 0;
    private TextView noContent;
    private FavoriteAdapter favoriteAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<FavoritePreviewMedia> favoritePreviewMedia = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
        }
        toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(activity).setSupportActionBar(toolbar);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.favorites);
        noContent = view.findViewById(R.id.no_content);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerView.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(activity, R.anim.layout_animation_down));
        favoriteAdapter = new FavoriteAdapter(activity, ALPHABETICAL_COMPARATOR);
        recyclerView.setAdapter(favoriteAdapter);
        new LoadData().execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(favOptions[position]);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        List<FavoritePreviewMedia> filteredModelList = filter(favoritePreviewMedia, s);
        favoriteAdapter.replaceAll(filteredModelList);
        recyclerView.scrollToPosition(0);
        return true;
    }

    private static List<FavoritePreviewMedia> filter(List<FavoritePreviewMedia> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();
        final List<FavoritePreviewMedia> filteredModelList = new ArrayList<>();
        for (FavoritePreviewMedia model : models) {
            String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<Void, Void, List<FavoritePreviewMedia>> {

        @Override
        protected List<FavoritePreviewMedia> doInBackground(Void... voids) {
            String type;
            if (position == 0) {
                type = Util.Constants.MOVIE;
            } else if (position == 1) {
                type = Util.Constants.TV_SHOW;
            } else {
                type = Util.Constants.ACTOR;
            }
            return Handler.getFavorites(getActivity(), type);
        }

        @Override
        protected void onPostExecute(List<FavoritePreviewMedia> favoritePreviewMediaList) {
            super.onPostExecute(favoritePreviewMediaList);
            favoritePreviewMedia = favoritePreviewMediaList;
            favoriteAdapter.add(favoritePreviewMedia);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (favoritePreviewMediaList.size() == 0) {
                noContent.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favorites_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("position", position);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                AlertDialog.Builder sortBuilder = new AlertDialog
                        .Builder(Objects.requireNonNull(getActivity()));
                sortBuilder.setTitle("Favorite type");
                sortBuilder.setItems(favOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        position = i;
                        Objects.requireNonNull(getActivity()).recreate();
                    }
                });
                sortBuilder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
