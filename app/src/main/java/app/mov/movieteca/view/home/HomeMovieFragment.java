package app.mov.movieteca.view.home;

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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.Genre;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.view.adapter.GenreAdapter;
import app.mov.movieteca.view.adapter.PreviewMovieAdapter;
import app.mov.movieteca.view.movie.FullMovieListActivity;
import app.mov.movieteca.view.movie.GenreMovieActivity;
import app.mov.movieteca.view.movie.MovieActivity;
import app.mov.movieteca.view.viewmodel.MoviesViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeMovieFragment extends Fragment
        implements PreviewMovieAdapter.OnItemClickedListener, GenreAdapter.OnItemClickListener {

    private Unbinder unbinder;
    private Context context;

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
    @BindView(R.id.recycler_genre)
    RecyclerView genreRecycler;

    private int mainItemId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        View view = inflater.inflate(R.layout.fragment_home_movies, container, false);
        unbinder = ButterKnife.bind(this, view);
        MoviesViewModel moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesViewModel.getNowPlaying().observe(this, this::setupNowPlaying);
        moviesViewModel.getPopular().observe(this, this::setupPopular);
        moviesViewModel.getTopRated().observe(this, this::setupTopRated);
        moviesViewModel.getUpcoming().observe(this, this::setupUpcoming);
        moviesViewModel.getMainItem().observe(this, this::setupMainItem);
        moviesViewModel.getGenres().observe(this, this::setupGenres);
        moviesViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                containerView.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                containerView.setVisibility(View.VISIBLE);
            }
        });
        moviesViewModel.start(1, "US");
        return view;
    }

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    private LayoutAnimationController getLayoutAnimationController() {
        return AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_down);
    }

    private void setupNowPlaying(List<PreviewMovie> previewMovieList) {
        PreviewMovieAdapter nowPlayingAdapter = new
                PreviewMovieAdapter(this, context, false);
        nowPlayingRecycler.setLayoutManager(getLinearLayoutManager());
        nowPlayingRecycler.setHasFixedSize(true);
        nowPlayingRecycler.setLayoutAnimation(getLayoutAnimationController());
        nowPlayingRecycler.setAdapter(nowPlayingAdapter);
        nowPlayingAdapter.add(previewMovieList);
    }

    private void setupPopular(List<PreviewMovie> previewMovieList) {
        PreviewMovieAdapter popularAdapter =
                new PreviewMovieAdapter(this, context, false);
        popularRecycler.setLayoutManager(getLinearLayoutManager());
        popularRecycler.setHasFixedSize(true);
        popularRecycler.setLayoutAnimation(getLayoutAnimationController());
        popularRecycler.setAdapter(popularAdapter);
        popularAdapter.add(previewMovieList);
    }

    private void setupTopRated(List<PreviewMovie> previewMovieList) {
        PreviewMovieAdapter topRatedAdapter =
                new PreviewMovieAdapter(this, context, false);
        topRatedRecycler.setLayoutManager(getLinearLayoutManager());
        topRatedRecycler.setHasFixedSize(true);
        topRatedRecycler.setLayoutAnimation(getLayoutAnimationController());
        topRatedRecycler.setAdapter(topRatedAdapter);
        topRatedAdapter.add(previewMovieList);
    }

    private void setupUpcoming(List<PreviewMovie> previewMovieList) {
        PreviewMovieAdapter upcomingAdapter =
                new PreviewMovieAdapter(this, context, false);
        upcomingRecycler.setLayoutManager(getLinearLayoutManager());
        upcomingRecycler.setHasFixedSize(true);
        upcomingRecycler.setLayoutAnimation(getLayoutAnimationController());
        upcomingRecycler.setAdapter(upcomingAdapter);
        upcomingAdapter.add(previewMovieList);
    }

    private void setupMainItem(PreviewMovie previewMovie) {
        Glide.with(mainImage)
                .load(Constants.IMAGE_LOADING_BASE_URL_1000
                        .concat(previewMovie.getBackdropPath()))
                .apply(RequestOptions.centerCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mainImage);
        mainTitle.setText(previewMovie.getOriginalTitle());
        mainItemId = previewMovie.getId();
    }

    private void setupGenres(List<Genre> genreList) {
        GenreAdapter genreAdapter = new GenreAdapter(this);
        genreRecycler.setLayoutManager(getLinearLayoutManager());
        genreRecycler.setHasFixedSize(true);
        genreRecycler.setLayoutAnimation(getLayoutAnimationController());
        genreRecycler.setAdapter(genreAdapter);
        genreAdapter.add(genreList);
    }

    @Override
    public void onClick(PreviewMovie previewMovie) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra("id", previewMovie.getId());
        startActivity(intent);
    }

    @OnClick(R.id.main_item)
    void seeMainItem() {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra("id", mainItemId);
        startActivity(intent);
    }

    @OnClick(R.id.popular_view_all)
    void seePopular() {
        Intent intent = new Intent(context, FullMovieListActivity.class);
        intent.putExtra("title", getString(R.string.popular_movies));
        intent.putExtra("type", Constants.POPULAR);
        startActivity(intent);
    }

    @OnClick(R.id.upcoming_view_all)
    void seeUpcoming() {
        Intent intent = new Intent(context, FullMovieListActivity.class);
        intent.putExtra("title",  getString(R.string.upcoming_movies));
        intent.putExtra("type", Constants.UPCOMING);
        startActivity(intent);
    }

    @OnClick(R.id.top_rated_view_all)
    void seeTopRated() {
        Intent intent = new Intent(context, FullMovieListActivity.class);
        intent.putExtra("title",  getString(R.string.top_rated_movies));
        intent.putExtra("type", Constants.TOP_RATED);
        startActivity(intent);
    }

    @OnClick(R.id.now_playing_view_all)
    void seeNowPlaying() {
        Intent intent = new Intent(context, FullMovieListActivity.class);
        intent.putExtra("title",  getString(R.string.now_in_theaters_movies));
        intent.putExtra("type", Constants.NOW_PLAYING);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Genre genre) {
        String genres = genre.getId().toString();
        Intent intent = new Intent(context, GenreMovieActivity.class);
        intent.putExtra("title",  genre.getName());
        intent.putExtra("genre", genres);
        startActivity(intent);
    }
}
