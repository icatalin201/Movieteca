package app.mov.movieteca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.FullListMovieAdapter;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.models.movies.NowPlayingMovies;
import app.mov.movieteca.models.movies.PopularMovies;
import app.mov.movieteca.models.movies.TopRatedMovies;
import app.mov.movieteca.models.movies.UpcomingMovies;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Catalin on 12/22/2017.
 */

public class FullList extends Fragment {

    private RecyclerView recyclerView;
    private FullListMovieAdapter fullListMovieAdapter;
    private List<MovieShort> movieShortList;
    private Call<NowPlayingMovies> nowPlayingMoviesCall;
    private Call<TopRatedMovies> topRatedMoviesCall;
    private Call<UpcomingMovies> upcomingMoviesCall;
    private Call<PopularMovies> popularMoviesCall;
    private ProgressBar progressBar;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private boolean isJobDone = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_list_fragment, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_full_list);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
        movieShortList = new ArrayList<>();
        fullListMovieAdapter = new FullListMovieAdapter(getContext(), movieShortList);
        recyclerView.setAdapter(fullListMovieAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        final String listType = Constants.movieType;
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
                    loadMovies(listType);
                    loading = true;
                }
            }
        });
        loadMovies(listType);
        return view;
    }

    private void checkJobDone(){
        if (isJobDone){
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void loadMovies(String listType){
        if (pagesOver) return;
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        switch (listType){
            case "now_playing":
                nowPlayingMoviesCall = service.getNowShowingMovies(Constants.api_key, presentPage, "US");
                nowPlayingMoviesCall.enqueue(new Callback<NowPlayingMovies>() {
                    @Override
                    public void onResponse(Call<NowPlayingMovies> call, Response<NowPlayingMovies> response) {
                        if (!response.isSuccessful()){
                            nowPlayingMoviesCall = call.clone();
                            nowPlayingMoviesCall.enqueue(this);
                            return;
                        }
                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;
                        for (MovieShort movie : response.body().getResults()){
                            if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_title() != null){
                                movieShortList.add(movie);
                            }
                        }
                        isJobDone = true;
                        fullListMovieAdapter.notifyDataSetChanged();
                        checkJobDone();
                        if (response.body().getTotal_pages() == response.body().getPage()){
                            pagesOver = true;
                        }
                        else{
                            presentPage++;
                        }
                    }

                    @Override
                    public void onFailure(Call<NowPlayingMovies> call, Throwable t) {

                    }
                });
                break;

            case "top_rated":
                topRatedMoviesCall = service.getTopRatedMovies(Constants.api_key, presentPage, "US");
                topRatedMoviesCall.enqueue(new Callback<TopRatedMovies>() {
                    @Override
                    public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                        if (!response.isSuccessful()){
                            topRatedMoviesCall = call.clone();
                            topRatedMoviesCall.enqueue(this);
                            return;
                        }
                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;
                        for (MovieShort movie : response.body().getResults()){
                            if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_title() != null){
                                movieShortList.add(movie);
                            }
                        }
                        isJobDone = true;
                        fullListMovieAdapter.notifyDataSetChanged();
                        checkJobDone();
                        if (response.body().getPage() == response.body().getTotal_pages()){
                            pagesOver = true;
                        }
                        else {
                            presentPage++;
                        }
                    }

                    @Override
                    public void onFailure(Call<TopRatedMovies> call, Throwable t) {

                    }
                });
                break;

            case "popular":
                popularMoviesCall = service.getPopularMovies(Constants.api_key, presentPage, "US");
                popularMoviesCall.enqueue(new Callback<PopularMovies>() {
                    @Override
                    public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {
                        if (!response.isSuccessful()){
                            popularMoviesCall = call.clone();
                            popularMoviesCall.enqueue(this);
                            return;
                        }
                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;
                        for (MovieShort movie : response.body().getResults()){
                            if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_title() != null){
                                movieShortList.add(movie);
                            }
                        }
                        isJobDone = true;
                        fullListMovieAdapter.notifyDataSetChanged();
                        checkJobDone();
                        if (response.body().getPage() == response.body().getTotal_pages()){
                            pagesOver = true;
                        }
                        else {
                            presentPage++;
                        }
                    }

                    @Override
                    public void onFailure(Call<PopularMovies> call, Throwable t) {

                    }
                });
                break;

            case "upcoming":
                upcomingMoviesCall = service.getUpcomingMovies(Constants.api_key, presentPage, "US");
                upcomingMoviesCall.enqueue(new Callback<UpcomingMovies>() {
                    @Override
                    public void onResponse(Call<UpcomingMovies> call, Response<UpcomingMovies> response) {
                        if (!response.isSuccessful()){
                            upcomingMoviesCall = call.clone();
                            upcomingMoviesCall.enqueue(this);
                            return;
                        }
                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;
                        for (MovieShort movie : response.body().getResults()){
                            if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_title() != null){
                                movieShortList.add(movie);
                            }
                        }
                        isJobDone = true;
                        fullListMovieAdapter.notifyDataSetChanged();
                        checkJobDone();
                        if (response.body().getPage() == response.body().getTotal_pages()){
                            pagesOver = true;
                        }
                        else {
                            presentPage++;
                        }
                    }

                    @Override
                    public void onFailure(Call<UpcomingMovies> call, Throwable t) {

                    }
                });
                break;
        }
    }

}
