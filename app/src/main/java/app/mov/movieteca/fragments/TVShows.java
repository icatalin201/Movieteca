package app.mov.movieteca.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.TVShortAdapter;
import app.mov.movieteca.models.movies.GenresList;
import app.mov.movieteca.models.tvshows.AiringTodayTVShows;
import app.mov.movieteca.models.tvshows.OnTheAirTVShows;
import app.mov.movieteca.models.tvshows.PopularTVShows;
import app.mov.movieteca.models.tvshows.TVShowShort;
import app.mov.movieteca.models.tvshows.TopRatedTVShows;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Catalin on 12/24/2017.
 */

public class TVShows extends Fragment {

    private RecyclerView nowPlayingRecycler;
    private List<TVShowShort> nowPlayingMovies;
    private TVShortAdapter nowPlayingAdapter;
    private RecyclerView popularRecycler;
    private List<TVShowShort> popularMovies;
    private TVShortAdapter popularAdapter;
    private RecyclerView topRatedRecycler;
    private List<TVShowShort> topRatedMovies;
    private TVShortAdapter topRatedAdapter;
    private RecyclerView upcomingRecycler;
    private List<TVShowShort> upcomingMovies;
    private TVShortAdapter upcomingAdapter;

    private TextView nowPlayingViewAll;
    private TextView topRatedViewAll;
    private TextView upcomingViewAll;
    private TextView popularViewAll;

    private ScrollView layout;

    private boolean[] isJobDone;
    private ProgressBar progressBar;

    private Call<GenresList> genreListCall;
    private Call<AiringTodayTVShows> nowPlayingCall;
    private Call<PopularTVShows> popularMoviesCall;
    private Call<TopRatedTVShows> topRatedMoviesCall;
    private Call<OnTheAirTVShows> upcomingMoviesCall;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_shows, container, false);
        nowPlayingRecycler = (RecyclerView)view.findViewById(R.id.recycler_airing_today);
        popularRecycler = (RecyclerView)view.findViewById(R.id.recycler_popular);
        topRatedRecycler = (RecyclerView)view.findViewById(R.id.recycler_top_rated);
        upcomingRecycler = (RecyclerView)view.findViewById(R.id.recycler_on_the_air);
        layout = (ScrollView)view.findViewById(R.id.entire_layout);
        isJobDone = new boolean[]{false, false, false, false};
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.setMax(100);
        nowPlayingViewAll = (TextView)view.findViewById(R.id.text_view_view_all);
        topRatedViewAll = (TextView)view.findViewById(R.id.top_rated_view_all);
        upcomingViewAll = (TextView)view.findViewById(R.id.upcoming_view_all) ;
        popularViewAll = (TextView)view.findViewById(R.id.popular_view_all);

        (new LinearSnapHelper()).attachToRecyclerView(nowPlayingRecycler);
        (new LinearSnapHelper()).attachToRecyclerView(popularRecycler);
        (new LinearSnapHelper()).attachToRecyclerView(topRatedRecycler);
        (new LinearSnapHelper()).attachToRecyclerView(upcomingRecycler);

        nowPlayingMovies = new ArrayList<>();
        nowPlayingAdapter = new TVShortAdapter(getContext(), nowPlayingMovies);
        nowPlayingRecycler.setAdapter(nowPlayingAdapter);
        nowPlayingRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        popularMovies = new ArrayList<>();
        popularAdapter = new TVShortAdapter(getContext(), popularMovies);
        popularRecycler.setAdapter(popularAdapter);
        popularRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        topRatedMovies = new ArrayList<>();
        topRatedAdapter = new TVShortAdapter(getContext(), topRatedMovies);
        topRatedRecycler.setAdapter(topRatedAdapter);
        topRatedRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        upcomingMovies = new ArrayList<>();
        upcomingAdapter = new TVShortAdapter(getContext(), upcomingMovies);
        upcomingRecycler.setAdapter(upcomingAdapter);
        upcomingRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        nowPlayingViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.movieType = "now_playing";
                Helper.changeFragment(getContext(), new FullListTVShows());
            }
        });

        popularViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.movieType = "popular";
                Helper.changeFragment(getContext(), new FullListTVShows());
            }
        });

        topRatedViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.movieType = "top_rated";
                Helper.changeFragment(getContext(), new FullListTVShows());
            }
        });

        upcomingViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.movieType = "upcoming";
                Helper.changeFragment(getContext(), new FullListTVShows());
            }
        });

        loadEverything();
        return view;
    }

    private void checkJobDone(){
        boolean check = false;
        for (boolean flag : isJobDone){
            if (flag){
                check = true;
            }
            else {
                check = false;
            }
        }
        if (check){
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
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

    private void loadEverything() {
        loadNowShowingMovies();
        loadPopularMovies();
        loadTopRatedMovies();
        loadUpcomingMovies();
    }

    private void loadUpcomingMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        upcomingMoviesCall = service.getOnTheAirTVShows(Constants.api_key, 1);
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

                for (TVShowShort movieShort : response.body().getResults()){
                    if (movieShort != null && movieShort.getBackdrop_path() != null){
                        upcomingMovies.add(movieShort);
                    }
                }
                isJobDone[0] = true;
                upcomingAdapter.notifyDataSetChanged();
                checkJobDone();
            }

            @Override
            public void onFailure(Call<OnTheAirTVShows> call, Throwable t) {

            }
        });
    }

    private void loadTopRatedMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        topRatedMoviesCall = service.getTopRatedTVShows(Constants.api_key, 1);
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

                for (TVShowShort movieShort : response.body().getResults()){
                    if (movieShort != null && movieShort.getBackdrop_path() != null){
                        topRatedMovies.add(movieShort);
                    }
                }
                isJobDone[1] = true;
                topRatedAdapter.notifyDataSetChanged();
                checkJobDone();
            }

            @Override
            public void onFailure(Call<TopRatedTVShows> call, Throwable t) {

            }
        });
    }

    private void loadPopularMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        popularMoviesCall = service.getPopularTVShows(Constants.api_key, 1);
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

                for (TVShowShort movieShort : response.body().getResults()){
                    if (movieShort != null && movieShort.getBackdrop_path() != null){
                        popularMovies.add(movieShort);
                    }
                }
                isJobDone[2] = true;
                popularAdapter.notifyDataSetChanged();
                checkJobDone();
            }

            @Override
            public void onFailure(Call<PopularTVShows> call, Throwable t) {

            }
        });
    }

    private void loadNowShowingMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        nowPlayingCall = service.getAiringTodayTVShows(Constants.api_key, 1);
        nowPlayingCall.enqueue(new Callback<AiringTodayTVShows>() {
            @Override
            public void onResponse(Call<AiringTodayTVShows> call, Response<AiringTodayTVShows> response) {

                if (!response.isSuccessful()){
                    nowPlayingCall = call.clone();
                    nowPlayingCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                for (TVShowShort movieShort : response.body().getResults()) {
                    if (movieShort != null && movieShort.getBackdrop_path() != null) {
                        nowPlayingMovies.add(movieShort);
                    }
                }
                isJobDone[3] = true;
                nowPlayingAdapter.notifyDataSetChanged();
                checkJobDone();
            }

            @Override
            public void onFailure(Call<AiringTodayTVShows> call, Throwable t) {

            }
        });

    }

}
