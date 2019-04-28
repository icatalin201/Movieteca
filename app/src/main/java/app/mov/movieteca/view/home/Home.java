package app.mov.movieteca.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.PreviewMovie;
import app.mov.movieteca.model.PreviewTVShow;
import app.mov.movieteca.model.entity.MovieLiveData;
import app.mov.movieteca.util.Util;
import app.mov.movieteca.view.activity.FullMediaActivity;
import app.mov.movieteca.view.activity.PreviewFullListActivity;
import app.mov.movieteca.view.adapter.PreviewMovieAdapter;
import app.mov.movieteca.viewmodel.HomeMovieViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Home extends Fragment implements PreviewMovieAdapter.OnItemClickedListener {

    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.container)
    NestedScrollView containerView;
    @BindView(R.id.main_image)
    ImageView mainImage;
    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.recycler_now_showing)
    RecyclerView nowPlayingRecycler;
    @BindView(R.id.recycler_popular)
    RecyclerView popularRecycler;
    @BindView(R.id.recycler_top_rated)
    RecyclerView topRatedRecycler;
    @BindView(R.id.recycler_upcoming)
    RecyclerView upcomingRecycler;

    private PreviewMovieAdapter nowPlayingAdapter;
    private PreviewMovieAdapter popularAdapter;
    private PreviewMovieAdapter upcomingAdapter;
    private PreviewMovieAdapter topRatedAdapter;

    private AppCompatActivity appCompatActivity;
    private int mainItemId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(appCompatActivity,
                LinearLayoutManager.HORIZONTAL, false);
    }

    private LayoutAnimationController getLayoutAnimationController() {
        return AnimationUtils.loadLayoutAnimation(appCompatActivity, R.anim.layout_animation_down);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        HomeMovieViewModel homeMovieViewModel = ViewModelProviders.of(this).get(HomeMovieViewModel.class);
        homeMovieViewModel.getResult().observe(this, this::consumeLiveData);
        appCompatActivity.setSupportActionBar(toolbar);
        popularAdapter = new PreviewMovieAdapter(appCompatActivity, new ArrayList<>(), false, this);
        nowPlayingAdapter = new PreviewMovieAdapter(appCompatActivity, new ArrayList<>(), false, this);
        upcomingAdapter = new PreviewMovieAdapter(appCompatActivity, new ArrayList<>(), false, this);
        topRatedAdapter = new PreviewMovieAdapter(appCompatActivity, new ArrayList<>(), false, this);
        nowPlayingRecycler.setAdapter(nowPlayingAdapter);
        popularRecycler.setAdapter(popularAdapter);
        topRatedRecycler.setAdapter(topRatedAdapter);
        upcomingRecycler.setAdapter(upcomingAdapter);
        nowPlayingRecycler.setLayoutManager(getLinearLayoutManager());
        popularRecycler.setLayoutManager(getLinearLayoutManager());
        topRatedRecycler.setLayoutManager(getLinearLayoutManager());
        upcomingRecycler.setLayoutManager(getLinearLayoutManager());
        nowPlayingRecycler.setHasFixedSize(true);
        popularRecycler.setHasFixedSize(true);
        topRatedRecycler.setHasFixedSize(true);
        upcomingRecycler.setHasFixedSize(true);
        nowPlayingRecycler.setLayoutAnimation(getLayoutAnimationController());
        popularRecycler.setLayoutAnimation(getLayoutAnimationController());
        topRatedRecycler.setLayoutAnimation(getLayoutAnimationController());
        upcomingRecycler.setLayoutAnimation(getLayoutAnimationController());
        homeMovieViewModel.findNowPlayingMovies(1, "US");
        homeMovieViewModel.findPopularMovies(1, "US");
        homeMovieViewModel.findTopRatedMovies(1, "US");
        homeMovieViewModel.findUpcomingMovies(1, "US");
        return view;
    }

    private void consumeLiveData(MovieLiveData movieLiveData) {
        if (movieLiveData.isLoading()) {
            progressBar.setVisibility(View.VISIBLE);
            containerView.setVisibility(View.GONE);
        } else {
            if (movieLiveData.getNowPlaying() != null &&
                    movieLiveData.getPopular() != null &&
                    movieLiveData.getTopRated() != null &&
                    movieLiveData.getUpcoming() != null) {
                nowPlayingAdapter.add(movieLiveData.getNowPlaying());
                popularAdapter.add(movieLiveData.getPopular());
                topRatedAdapter.add(movieLiveData.getTopRated());
                upcomingAdapter.add(movieLiveData.getUpcoming());
                mainItemId = movieLiveData.getMainItem().getId();
                Glide.with(mainImage)
                        .load(Util.Constants.IMAGE_LOADING_BASE_URL_1000
                                .concat(movieLiveData.getMainItem().getBackdropPath()))
                        .apply(RequestOptions.centerCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mainImage);
                mainTitle.setText(movieLiveData.getMainItem().getOriginalTitle());
                progressBar.setVisibility(View.GONE);
                containerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.movies)
    void switchToMovies() {

    }

    @OnClick(R.id.tv_shows)
    void switchToSeries() {

    }

    @OnClick(R.id.main_item)
    void seeMainItem() {
        Intent intent = new Intent(appCompatActivity, FullMediaActivity.class);
        intent.putExtra("id", mainItemId);
        startActivity(intent);
    }

    @OnClick(R.id.popular_view_all)
    void seePopular() {
        Intent intent = new Intent(appCompatActivity, PreviewFullListActivity.class);
        intent.putExtra("internal_type", getString(R.string.popular_movies));
        intent.putExtra("extra", "popular");
        startActivity(intent);
    }

    @OnClick(R.id.upcoming_view_all)
    void seeUpcoming() {
        Intent intent = new Intent(appCompatActivity, PreviewFullListActivity.class);
        intent.putExtra("internal_type",  getString(R.string.upcoming_movies));
        intent.putExtra("extra", "upcoming");
        startActivity(intent);
    }

    @OnClick(R.id.top_rated_view_all)
    void seeTopRated() {
        Intent intent = new Intent(appCompatActivity, PreviewFullListActivity.class);
        intent.putExtra("internal_type",  getString(R.string.top_rated_movies));
        intent.putExtra("extra", "top_rated");
        startActivity(intent);
    }

    @OnClick(R.id.now_playing_view_all)
    void seeNowPlaying() {
        Intent intent = new Intent(appCompatActivity, PreviewFullListActivity.class);
        intent.putExtra("internal_type",  getString(R.string.now_in_theaters_movies));
        intent.putExtra("extra", "now_playing");
        startActivity(intent);
    }

    @Override
    public void onClick(PreviewMovie previewMovie) {

    }

}
