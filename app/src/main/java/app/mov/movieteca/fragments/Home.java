package app.mov.movieteca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.MovieShortAdapter;
import app.mov.movieteca.models.movies.GenresList;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.models.movies.NowPlayingMovies;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.GenresUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Catalin on 10/29/2017.
 */

public class Home extends Fragment {

    private FrameLayout nowPlayingLayout;
    private RecyclerView nowPlayingRecycler;
    private List<MovieShort> nowPlayingMovies;
    private MovieShortAdapter nowPlayingAdapter;

    private List<Boolean> isJobDone;
    private ProgressBar progressBar;

    private Call<GenresList> genreListCall;
    private Call<NowPlayingMovies> nowPlayingCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nowPlayingLayout = (FrameLayout)view.findViewById(R.id.layout_now_showing);
        nowPlayingRecycler = (RecyclerView)view.findViewById(R.id.recycler_now_showing);
        isJobDone = new ArrayList<>();
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.setMax(100);

        nowPlayingMovies = new ArrayList<>();
        nowPlayingAdapter = new MovieShortAdapter(getContext(), nowPlayingMovies);

        nowPlayingRecycler.setAdapter(nowPlayingAdapter);
        nowPlayingRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        loadEverything();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        nowPlayingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void checkJobDone(){
        Boolean check = false;
        for (Boolean flag : isJobDone){
            if (flag){
                check = true;
            }
        }
        if (check){
            progressBar.setVisibility(View.GONE);
            nowPlayingRecycler.setVisibility(View.VISIBLE);
            nowPlayingLayout.setVisibility(View.VISIBLE);
        }
    }

    private void loadNowShowingMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        nowPlayingCall = service.getNowShowingMovies(Constants.api_key, 1, "US");
        nowPlayingCall.enqueue(new Callback<NowPlayingMovies>() {
            @Override
            public void onResponse(Call<NowPlayingMovies> call, Response<NowPlayingMovies> response) {

                if (!response.isSuccessful()){
                    nowPlayingCall = call.clone();
                    nowPlayingCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                for (MovieShort movieShort : response.body().getResults()) {
                    if (movieShort != null && movieShort.getBackdrop_path() != null) {
                        nowPlayingMovies.add(movieShort);
                    }
                }
                isJobDone.add(true);
                checkJobDone();
                nowPlayingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NowPlayingMovies> call, Throwable t) {

            }
        });

    }

    private void loadEverything() {

        if (GenresUtil.isGenresListLoaded()) {
            loadNowShowingMovies();
        } else {
            NetworkService apiService = NetworkClient.getClient().create(NetworkService.class);
            genreListCall = apiService.getMovieGenresList(Constants.api_key);
            genreListCall.enqueue(new Callback<GenresList>() {
                @Override
                public void onResponse(Call<GenresList> call, Response<GenresList> response) {
                    if (!response.isSuccessful()) {
                        genreListCall = call.clone();
                        genreListCall.enqueue(this);
                        return;
                    }

                    if (response.body() == null) return;
                    if (response.body().getGenres() == null) return;

                    GenresUtil.loadGenresList(response.body().getGenres());
                    loadNowShowingMovies();
                }

                @Override
                public void onFailure(Call<GenresList> call, Throwable t) {

                }
            });
        }

    }
}
