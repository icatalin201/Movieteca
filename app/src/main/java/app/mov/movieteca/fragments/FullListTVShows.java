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
import app.mov.movieteca.adapters.FullListShowAdapter;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.models.movies.NowPlayingMovies;
import app.mov.movieteca.models.movies.PopularMovies;
import app.mov.movieteca.models.movies.TopRatedMovies;
import app.mov.movieteca.models.movies.UpcomingMovies;
import app.mov.movieteca.models.tvshows.AiringTodayTVShows;
import app.mov.movieteca.models.tvshows.OnTheAirTVShows;
import app.mov.movieteca.models.tvshows.PopularTVShows;
import app.mov.movieteca.models.tvshows.TVShowShort;
import app.mov.movieteca.models.tvshows.TopRatedTVShows;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Catalin on 12/24/2017.
 */

public class FullListTVShows extends Fragment {

    private RecyclerView recyclerView;
    private FullListShowAdapter fullListMovieAdapter;
    private List<TVShowShort> movieShortList;
    private Call<AiringTodayTVShows> nowPlayingMoviesCall;
    private Call<TopRatedTVShows> topRatedMoviesCall;
    private Call<OnTheAirTVShows> upcomingMoviesCall;
    private Call<PopularTVShows> popularMoviesCall;
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
        fullListMovieAdapter = new FullListShowAdapter(getContext(), movieShortList);
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
        switch (listType){
            case "now_playing":
                getActivity().setTitle("Now in Theaters");
                break;
            case "top_rated":
                getActivity().setTitle("Top Rated");
                break;
            case "upcoming":
                getActivity().setTitle("Upcoming");
                break;
            case "popular":
                getActivity().setTitle("Popular");
                break;
        }
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
                nowPlayingMoviesCall = service.getAiringTodayTVShows(Constants.api_key, presentPage);
                nowPlayingMoviesCall.enqueue(new Callback<AiringTodayTVShows>() {
                    @Override
                    public void onResponse(Call<AiringTodayTVShows> call, Response<AiringTodayTVShows> response) {
                        if (!response.isSuccessful()){
                            nowPlayingMoviesCall = call.clone();
                            nowPlayingMoviesCall.enqueue(this);
                            return;
                        }
                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;
                        for (TVShowShort movie : response.body().getResults()){
                            if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_name() != null){
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
                    public void onFailure(Call<AiringTodayTVShows> call, Throwable t) {

                    }
                });
                break;

            case "top_rated":
                topRatedMoviesCall = service.getTopRatedTVShows(Constants.api_key, presentPage);
                topRatedMoviesCall.enqueue(new Callback<TopRatedTVShows>() {
                    @Override
                    public void onResponse(Call<TopRatedTVShows> call, Response<TopRatedTVShows> response) {
                        if (!response.isSuccessful()){
                            topRatedMoviesCall = call.clone();
                            topRatedMoviesCall.enqueue(this);
                            return;
                        }
                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;
                        for (TVShowShort movie : response.body().getResults()){
                            if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_name() != null){
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
                    public void onFailure(Call<TopRatedTVShows> call, Throwable t) {

                    }
                });
                break;

            case "popular":
                popularMoviesCall = service.getPopularTVShows(Constants.api_key, presentPage);
                popularMoviesCall.enqueue(new Callback<PopularTVShows>() {
                    @Override
                    public void onResponse(Call<PopularTVShows> call, Response<PopularTVShows> response) {
                        if (!response.isSuccessful()){
                            popularMoviesCall = call.clone();
                            popularMoviesCall.enqueue(this);
                            return;
                        }
                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;
                        for (TVShowShort movie : response.body().getResults()){
                            if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_name() != null){
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
                    public void onFailure(Call<PopularTVShows> call, Throwable t) {

                    }
                });
                break;

            case "upcoming":
                upcomingMoviesCall = service.getOnTheAirTVShows(Constants.api_key, presentPage);
                upcomingMoviesCall.enqueue(new Callback<OnTheAirTVShows>() {
                    @Override
                    public void onResponse(Call<OnTheAirTVShows> call, Response<OnTheAirTVShows> response) {
                        if (!response.isSuccessful()){
                            upcomingMoviesCall = call.clone();
                            upcomingMoviesCall.enqueue(this);
                            return;
                        }
                        if (response.body() == null) return;
                        if (response.body().getResults() == null) return;
                        for (TVShowShort movie : response.body().getResults()){
                            if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_name() != null){
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
                    public void onFailure(Call<OnTheAirTVShows> call, Throwable t) {

                    }
                });
                break;
        }
    }

}
