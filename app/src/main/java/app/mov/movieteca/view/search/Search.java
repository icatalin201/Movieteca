package app.mov.movieteca.view.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.Genre;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.view.adapter.GenreAdapter;
import app.mov.movieteca.view.adapter.PreviewMovieAdapter;
import app.mov.movieteca.view.movie.MovieActivity;
import app.mov.movieteca.view.viewmodel.SearchViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Search extends Fragment
        implements SearchView.OnQueryTextListener, GenreAdapter.OnItemClickListener,
        PreviewMovieAdapter.OnItemClickedListener {

    @BindView(R.id.recycler)
    RecyclerView genreRecycler;
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SearchView searchView;
    private GenreAdapter genreAdapter;
    private Unbinder unbinder;
    private AppCompatActivity appCompatActivity;
    private SearchViewModel searchViewModel;
    private PreviewMovieAdapter previewMovieAdapter;

    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private String query, genre;
    private boolean searchMode = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.subscribeGenres();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        appCompatActivity.setSupportActionBar(toolbar);
        genreAdapter = new GenreAdapter(this);
        previewMovieAdapter = new PreviewMovieAdapter(this, appCompatActivity, true);
        genreRecycler.setLayoutManager(new LinearLayoutManager(appCompatActivity,
                LinearLayoutManager.HORIZONTAL, false));
        genreRecycler.setHasFixedSize(true);
        genreRecycler.setAdapter(genreAdapter);
        genreRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(appCompatActivity, R.anim.layout_animation_down));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(appCompatActivity, 3);
        searchRecycler.setLayoutManager(gridLayoutManager);
        searchRecycler.setHasFixedSize(true);
        searchRecycler.setAdapter(previewMovieAdapter);
        searchRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(appCompatActivity, R.anim.layout_animation_down));
        toolbar.setTitle(R.string.search);
        searchViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
        searchViewModel.getMoviesGenre().observe(this, genres -> {
            genreAdapter.add(genres);
        });
        searchViewModel.getSearch().observe(this, previewMovies -> {
            previewMovieAdapter.add(previewMovies);
            presentPage++;
        });
        searchRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    if (searchMode) {
                        searchViewModel.findMovies(presentPage, query);
                    } else {
                        searchViewModel.find(presentPage, genre);
                    }
                    loading = true;
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        previewMovieAdapter.clear();
        searchMode = true;
        query = s;
        searchViewModel.findMovies(1, query);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }

    @Override
    public void onItemClick(Genre genre) {
//        String genres = genre.getId().toString();
//        Intent intent = new Intent(appCompatActivity, GenreMovieActivity.class);
//        intent.putExtra("title",  genre.getName());
//        intent.putExtra("genre", genres);
//        startActivity(intent);
        previewMovieAdapter.clear();
        searchMode = false;
        this.genre = genre.getId().toString();
        searchViewModel.find(1, this.genre);
    }

    @Override
    public void onClick(PreviewMovie previewMovie) {
        Intent intent = new Intent(appCompatActivity, MovieActivity.class);
        intent.putExtra("id", previewMovie.getId());
        startActivity(intent);
    }


//    @SuppressLint("StaticFieldLeak")
//    private class LoadData extends AsyncTask<Void, Void, app.mov.movieteca.model.Search> {
//
//        @Override
//        protected app.mov.movieteca.model.Search doInBackground(Void... voids) {
//            try {
//                String urlString = NetworkClient.BASE_URL + "search/multi"
//                        + "?"
//                        + "api_key=" + Util.Constants.API_KEY
//                        + "&"
//                        + "query=" + query
//                        + "&"
//                        + "page=" + presentPage;
//
//                URL url = new URL(urlString);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.connect();
//                if (httpURLConnection.getResponseCode() != 200) return null;
//                InputStream inputStream = httpURLConnection.getInputStream();
//                Scanner scanner = new Scanner(inputStream);
//                StringBuilder jsonString = new StringBuilder();
//                while (scanner.hasNext()) {
//                    jsonString.append(scanner.nextLine());
//                }
//                JSONObject searchJsonObject = new JSONObject(jsonString.toString());
//                app.mov.movieteca.model.Search searchResponse = new app.mov.movieteca.model.Search();
//                searchResponse.setPage(searchJsonObject.getInt("page"));
//                searchResponse.setTotalPages(searchJsonObject.getInt("total_pages"));
//                JSONArray resultsJsonArray = searchJsonObject.getJSONArray("results");
//                List<SearchResults> searchResults = new ArrayList<>();
//                for (int i = 0; i < resultsJsonArray.length(); i++) {
//                    JSONObject result = (JSONObject) resultsJsonArray.get(i);
//                    SearchResults searchResult = new SearchResults();
//                    switch (result.getString("media_type")) {
//                        case "movie":
//                            searchResult.setId(result.getInt("id"));
//                            searchResult.setPoster(result.getString("poster_path"));
//                            searchResult.setName(result.getString("title"));
//                            searchResult.setMediaType(Util.Constants.MOVIE);
//                            break;
//                        case "tv":
//                            searchResult.setId(result.getInt("id"));
//                            searchResult.setPoster(result.getString("poster_path"));
//                            searchResult.setName(result.getString("name"));
//                            searchResult.setMediaType(Util.Constants.TV_SHOW);
//                            break;
//                        case "person":
//                            searchResult.setId(result.getInt("id"));
//                            searchResult.setPoster(result.getString("profile_path"));
//                            searchResult.setName(result.getString("name"));
//                            searchResult.setMediaType(Util.Constants.ACTOR);
//                            break;
//                    }
//                    if (searchResult.getPoster() == null ||
//                            searchResult.getPoster().equals("")) continue;
//                    searchResults.add(searchResult);
//                }
//                searchResponse.setResults(searchResults);
//                return searchResponse;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(app.mov.movieteca.model.Search search) {
//            super.onPostExecute(search);
//            if (search == null) return;
//            if (search.getResults() == null) return;
//            if (presentPage == 1) searchAdapter.clear();
//            searchAdapter.add(search.getResults());
//            progressBar.setVisibility(View.GONE);
//            recyclerView.setAlpha(1.0f);
//            recyclerView.setVisibility(View.VISIBLE);
//            if (search.getPage().equals(search.getTotalPages()))
//                pagesOver = true;
//            else
//                presentPage++;
//        }
//    }
}
