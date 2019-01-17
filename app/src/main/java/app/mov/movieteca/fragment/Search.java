package app.mov.movieteca.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import app.mov.movieteca.R;
import app.mov.movieteca.adapter.SearchAdapter;
import app.mov.movieteca.model.SearchResults;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.utils.Util;

public class Search extends Fragment implements SearchView.OnQueryTextListener {

    private String query;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchAdapter searchAdapter;
    private SearchView searchView;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private TextView label;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(activity).setSupportActionBar(toolbar);
        recyclerView = view.findViewById(R.id.search);
        progressBar = view.findViewById(R.id.progressBar);
        label = view.findViewById(R.id.label);
        searchAdapter = new SearchAdapter(getActivity(), new ArrayList<SearchResults>());
        recyclerView.setAdapter(searchAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(getActivity(), R.anim.layout_animation_down));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <=
                        (firstVisibleItem + visibleThreshold)) {
                    loadData();
                    loading = true;
                }
            }
        });
        toolbar.setTitle(R.string.search);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        label.setVisibility(View.GONE);
        query = s;
        pagesOver = false;
        presentPage = 1;
        loading = true;
        previousTotal = 0;
        visibleThreshold = 5;
        loadData();
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        return true;
    }

    private void loadData() {
        if (pagesOver) return;
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setAlpha(0.3f);
        new LoadData().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<Void, Void, app.mov.movieteca.model.Search> {

        @Override
        protected app.mov.movieteca.model.Search doInBackground(Void... voids) {
            try {
                String urlString = NetworkClient.BASE_URL + "search/multi"
                        + "?"
                        + "api_key=" + Util.Constants.API_KEY
                        + "&"
                        + "query=" + query
                        + "&"
                        + "page=" + presentPage;

                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() != 200) return null;
                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder jsonString = new StringBuilder();
                while (scanner.hasNext()) {
                    jsonString.append(scanner.nextLine());
                }
                JSONObject searchJsonObject = new JSONObject(jsonString.toString());
                app.mov.movieteca.model.Search searchResponse = new app.mov.movieteca.model.Search();
                searchResponse.setPage(searchJsonObject.getInt("page"));
                searchResponse.setTotalPages(searchJsonObject.getInt("total_pages"));
                JSONArray resultsJsonArray = searchJsonObject.getJSONArray("results");
                List<SearchResults> searchResults = new ArrayList<>();
                for (int i = 0; i < resultsJsonArray.length(); i++) {
                    JSONObject result = (JSONObject) resultsJsonArray.get(i);
                    SearchResults searchResult = new SearchResults();
                    switch (result.getString("media_type")) {
                        case "movie":
                            searchResult.setId(result.getInt("id"));
                            searchResult.setPoster(result.getString("poster_path"));
                            searchResult.setName(result.getString("title"));
                            searchResult.setMediaType(Util.Constants.MOVIE);
                            break;
                        case "tv":
                            searchResult.setId(result.getInt("id"));
                            searchResult.setPoster(result.getString("poster_path"));
                            searchResult.setName(result.getString("name"));
                            searchResult.setMediaType(Util.Constants.TV_SHOW);
                            break;
                        case "person":
                            searchResult.setId(result.getInt("id"));
                            searchResult.setPoster(result.getString("profile_path"));
                            searchResult.setName(result.getString("name"));
                            searchResult.setMediaType(Util.Constants.ACTOR);
                            break;
                    }
                    if (searchResult.getPoster() == null ||
                            searchResult.getPoster().equals("")) continue;
                    searchResults.add(searchResult);
                }
                searchResponse.setResults(searchResults);
                return searchResponse;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(app.mov.movieteca.model.Search search) {
            super.onPostExecute(search);
            if (search == null) return;
            if (search.getResults() == null) return;
            if (presentPage == 1) searchAdapter.clear();
            searchAdapter.add(search.getResults());
            progressBar.setVisibility(View.GONE);
            recyclerView.setAlpha(1.0f);
            recyclerView.setVisibility(View.VISIBLE);
            if (search.getPage().equals(search.getTotalPages()))
                pagesOver = true;
            else
                presentPage++;
        }
    }
}
