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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.MovieShortAdapter;
import app.mov.movieteca.models.movies.GenresList;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.models.movies.NowPlayingMovies;
import app.mov.movieteca.models.movies.PopularMovies;
import app.mov.movieteca.models.movies.TopRatedMovies;
import app.mov.movieteca.models.movies.UpcomingMovies;
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

    private FrameLayout popularLayout;
    private RecyclerView popularRecycler;
    private List<MovieShort> popularMovies;
    private MovieShortAdapter popularAdapter;

    private FrameLayout topRatedLayout;
    private RecyclerView topRatedRecycler;
    private List<MovieShort> topRatedMovies;
    private MovieShortAdapter topRatedAdapter;

    private FrameLayout upcomingLayout;
    private RecyclerView upcomingRecycler;
    private List<MovieShort> upcomingMovies;
    private MovieShortAdapter upcomingAdapter;

    private TextView nowPlayingViewAll;
    private TextView topRatedViewAll;
    private TextView upcomingViewAll;
    private TextView popularViewAll;

    private List<Boolean> isJobDone;
    private ProgressBar progressBar;

    private Call<GenresList> genreListCall;
    private Call<NowPlayingMovies> nowPlayingCall;
    private Call<PopularMovies> popularMoviesCall;
    private Call<TopRatedMovies> topRatedMoviesCall;
    private Call<UpcomingMovies> upcomingMoviesCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nowPlayingLayout = (FrameLayout)view.findViewById(R.id.layout_now_showing);
        nowPlayingRecycler = (RecyclerView)view.findViewById(R.id.recycler_now_showing);
        popularLayout = (FrameLayout)view.findViewById(R.id.layout_popular);
        popularRecycler = (RecyclerView)view.findViewById(R.id.recycler_popular);
        topRatedLayout = (FrameLayout)view.findViewById(R.id.layout_top_rated);
        topRatedRecycler = (RecyclerView)view.findViewById(R.id.recycler_top_rated);
        upcomingLayout = (FrameLayout)view.findViewById(R.id.layout_upcoming);
        upcomingRecycler = (RecyclerView)view.findViewById(R.id.recycler_upcoming);
        isJobDone = new ArrayList<>(4);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.setMax(100);

        nowPlayingViewAll = (TextView)view.findViewById(R.id.text_view_view_all);
        topRatedViewAll = (TextView)view.findViewById(R.id.top_rated_view_all);
        upcomingViewAll = (TextView)view.findViewById(R.id.upcoming_view_all) ;
        popularViewAll = (TextView)view.findViewById(R.id.popular_view_all);

        nowPlayingMovies = new ArrayList<>();
        nowPlayingAdapter = new MovieShortAdapter(getContext(), nowPlayingMovies);
        nowPlayingRecycler.setAdapter(nowPlayingAdapter);
        nowPlayingRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        popularMovies = new ArrayList<>();
        popularAdapter = new MovieShortAdapter(getContext(), popularMovies);
        popularRecycler.setAdapter(popularAdapter);
        popularRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        topRatedMovies = new ArrayList<>();
        topRatedAdapter = new MovieShortAdapter(getContext(), topRatedMovies);
        topRatedRecycler.setAdapter(topRatedAdapter);
        topRatedRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        upcomingMovies = new ArrayList<>();
        upcomingAdapter = new MovieShortAdapter(getContext(), upcomingMovies);
        upcomingRecycler.setAdapter(upcomingAdapter);
        upcomingRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        nowPlayingViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("check", "pressed");
            }
        });

        popularViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        topRatedViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        upcomingViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadEverything();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        nowPlayingAdapter.notifyDataSetChanged();
        popularAdapter.notifyDataSetChanged();
        topRatedAdapter.notifyDataSetChanged();
        upcomingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (nowPlayingCall != null) nowPlayingCall.cancel();
        if (popularMoviesCall != null) popularMoviesCall.cancel();
        if (topRatedMoviesCall != null) topRatedMoviesCall.cancel();
        if (upcomingMoviesCall != null) upcomingMoviesCall.cancel();
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
            popularRecycler.setVisibility(View.VISIBLE);
            popularLayout.setVisibility(View.VISIBLE);
            topRatedLayout.setVisibility(View.VISIBLE);
            topRatedRecycler.setVisibility(View.VISIBLE);
            upcomingLayout.setVisibility(View.VISIBLE);
            upcomingRecycler.setVisibility(View.VISIBLE);
        }
    }

    private void loadUpcomingMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        upcomingMoviesCall = service.getUpcomingMovies(Constants.api_key, 1, "US");
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

                for (MovieShort movieShort : response.body().getResults()){
                    if (movieShort != null && movieShort.getBackdrop_path() != null){
                        upcomingMovies.add(movieShort);
                    }
                }
                isJobDone.add(true);
                checkJobDone();
                upcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UpcomingMovies> call, Throwable t) {

            }
        });
    }

    private void loadTopRatedMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        topRatedMoviesCall = service.getTopRatedMovies(Constants.api_key, 1, "US");
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

                for (MovieShort movieShort : response.body().getResults()){
                    if (movieShort != null && movieShort.getBackdrop_path() != null){
                        topRatedMovies.add(movieShort);
                    }
                }
                isJobDone.add(true);
                checkJobDone();
                topRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {

            }
        });
    }

    private void loadPopularMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        popularMoviesCall = service.getPopularMovies(Constants.api_key, 1, "US");
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

                for (MovieShort movieShort : response.body().getResults()){
                    if (movieShort != null && movieShort.getBackdrop_path() != null){
                        popularMovies.add(movieShort);
                    }
                }
                isJobDone.add(true);
                checkJobDone();
                popularAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {

            }
        });
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
            loadPopularMovies();
            loadTopRatedMovies();
            loadUpcomingMovies();
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
                    loadPopularMovies();
                    loadTopRatedMovies();
                    loadUpcomingMovies();
                }

                @Override
                public void onFailure(Call<GenresList> call, Throwable t) {

                }
            });
        }

    }
}
