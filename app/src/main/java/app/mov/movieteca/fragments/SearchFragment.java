package app.mov.movieteca.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.SearchAdapter;
import app.mov.movieteca.models.search.Search;
import app.mov.movieteca.models.search.SearchResults;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.SearchAsyncTaskLoader;

/**
 * Created by Catalin on 12/24/2017.
 */

public class SearchFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<SearchResults> resultsList;
    private TextView empty;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private String keywords;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_search);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.setMax(100);
        empty = (TextView)view.findViewById(R.id.empty);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("search", Context.MODE_PRIVATE);
        keywords = sharedPreferences.getString(Constants.search, "");
        resultsList = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(), resultsList);
        recyclerView.setAdapter(searchAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loadSearchResults();
                    loading = true;
                }

            }
        });
        loadSearchResults();
        return view;
    }

    private void loadSearchResults() {
        if (pagesOver) return;

        getLoaderManager().initLoader(presentPage, null, new LoaderManager.LoaderCallbacks<Search>() {

            @Override
            public Loader<Search> onCreateLoader(int i, Bundle bundle) {
                return new SearchAsyncTaskLoader(getContext(), keywords, String.valueOf(presentPage));
            }

            @Override
            public void onLoadFinished(Loader<Search> loader, Search searchResponse) {

                if (searchResponse == null) return;
                if (searchResponse.getResults() == null) return;

                for (SearchResults searchResult : searchResponse.getResults()) {
                    if (searchResult != null)
                        resultsList.add(searchResult);
                }
                searchAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (resultsList.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                }
                recyclerView.setVisibility(View.VISIBLE);
                if (searchResponse.getPage() == searchResponse.getTotal_pages())
                    pagesOver = true;
                else
                    presentPage++;

            }

            @Override
            public void onLoaderReset(Loader<Search> loader) {

            }
        }).forceLoad();

    }

}
