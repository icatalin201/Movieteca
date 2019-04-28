package app.mov.movieteca.view.movie;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import app.mov.movieteca.R;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.util.Util;
import app.mov.movieteca.view.adapter.CastAdapter;
import app.mov.movieteca.view.adapter.PreviewMovieAdapter;
import app.mov.movieteca.view.adapter.TrailersAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FullMediaActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.entire_layout)
    NestedScrollView scrollView;
    @BindView(R.id.image_backdrop)
    ImageView imageBackdrop;
    @BindView(R.id.image_poster)
    ImageView imagePoster;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.genre)
    TextView genres;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.tagline)
    TextView tagline;
    @BindView(R.id.overview)
    TextView overview;
    @BindView(R.id.release_date)
    TextView releaseDate;
    @BindView(R.id.runtime)
    TextView runtime;
    @BindView(R.id.budget)
    TextView budget;
    @BindView(R.id.revenue)
    TextView revenue;
    @BindView(R.id.trailers)
    TextView trailersLabel;
    @BindView(R.id.cast)
    TextView castLabel;
    @BindView(R.id.similar)
    TextView similarsLabel;
    @BindView(R.id.recycler_trailers)
    RecyclerView trailers;
    @BindView(R.id.recycler_similar)
    RecyclerView similars;
    @BindView(R.id.recycler_cast)
    RecyclerView casts;
    @BindView(R.id.favorite)
    ImageButton fav;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    private PreviewMovieAdapter similarMoviesAdapter;
    private TrailersAdapter trailersAdapter;
    private CastAdapter castAdapter;

    private String type;
    private String path;
    private int movieId;
    private String name;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }

    private LayoutAnimationController getLayoutAnimationController() {
        return AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_media);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (appBarLayout.getTotalScrollRange() + verticalOffset == 0) {
                collapsingToolbarLayout.setTitle(name);
            } else {
                collapsingToolbarLayout.setTitle("");
            }
        });
        movieId = getIntent().getIntExtra("id", 0);
        type = getIntent().getStringExtra("type");
//        similarMoviesAdapter = new PreviewMovieAdapter();
        trailersAdapter = new TrailersAdapter(this, new ArrayList<>());
        castAdapter = new CastAdapter(this, new ArrayList<>());
        trailers.setLayoutManager(getLinearLayoutManager());
        similars.setLayoutManager(getLinearLayoutManager());
        casts.setLayoutManager(getLinearLayoutManager());
        trailers.setLayoutAnimation(getLayoutAnimationController());
        similars.setLayoutAnimation(getLayoutAnimationController());
        casts.setLayoutAnimation(getLayoutAnimationController());
        trailers.setHasFixedSize(true);
        similars.setHasFixedSize(true);
        casts.setHasFixedSize(true);
        trailers.setAdapter(trailersAdapter);
        similars.setAdapter(similarMoviesAdapter);
        casts.setAdapter(castAdapter);
    }

    @OnClick(R.id.share)
    public void share() {
        Intent movieShareIntent = new Intent(Intent.ACTION_SEND);
        movieShareIntent.setType("text/plain");
        String extraText = "";
        if (name != null) extraText += name + "\n";
        extraText += tagline.getText().toString();
        movieShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        startActivity(movieShareIntent);
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.favorite)
    public void toggleFavorite() {
        if (fav.getTag().equals("1")) {
            Util.notify(coordinatorLayout, title.getText().toString()
                    .concat(" has been deleted from favorite collection."));
            fav.setTag("0");
            fav.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
        }
        else if (fav.getTag().equals("0")){
            FavoritePreviewMedia favoritePreviewMedia = new FavoritePreviewMedia();
            favoritePreviewMedia.setResType(type);
            favoritePreviewMedia.setResId(movieId);
            favoritePreviewMedia.setPoster(path);
            favoritePreviewMedia.setName(title.getText().toString());
            Util.notify(coordinatorLayout, title.getText().toString()
                    .concat(" has been added to favorite collection."));
            fav.setTag("1");
            fav.setImageResource(R.drawable.ic_baseline_favorite_24px);
        }
    }

//    @Override
//    public void onLoading() {
//        progressBar.setVisibility(View.VISIBLE);
//        scrollView.setVisibility(View.GONE);
//        appBarLayout.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onLoaded(MediaResponse mediaResponse) {
//        String backdropPath = "";
//        String posterPath = "";
//        if (mediaResponse.getType().equals(Util.Constants.MOVIE)) {
//            MovieResponse movieResponse = (MovieResponse) mediaResponse;
//            backdropPath = movieResponse.getBackdropPath();
//            posterPath = movieResponse.getPosterPath();
//            path = movieResponse.getPosterPath();
//            title.setText(movieResponse.getTitle());
//            name = movieResponse.getTitle();
//            if (movieResponse.getBudget() != null && movieResponse.getBudget() > 0){
//                budget.setText(String.format("Budget: %s USD",
//                        Util.formatValue(movieResponse.getBudget())));
//                budget.setVisibility(View.VISIBLE);
//            }
//            if (movieResponse.getRuntime() != null && movieResponse.getRuntime() > 0){
//                runtime.setText(String.format("Runtime: %s minutes",
//                        movieResponse.getRuntime()));
//                runtime.setVisibility(View.VISIBLE);
//            }
//            if (movieResponse.getRevenue() != null && movieResponse.getRevenue() > 0) {
//                revenue.setText(String.format("Revenue: %s USD",
//                        Util.formatValue(movieResponse.getRevenue())));
//                revenue.setVisibility(View.VISIBLE);
//            }
//            if (movieResponse.getReleaseDate() != null && !movieResponse.getReleaseDate().equals("")){
//                releaseDate.setText(String.format("Release date: %s",
//                        Util.convertDateFromFormatToFormat(movieResponse.getReleaseDate(),
//                                Util.DateFormats.INVERSE_FORMAT,
//                                Util.DateFormats.STANDARD_FORMAT)));
//                releaseDate.setVisibility(View.VISIBLE);
//            }
//            rating.setText(String.valueOf(movieResponse.getVoteAverage()));
//            if (movieResponse.getTagline() != null) {
//                tagline.setText(movieResponse.getTagline());
//                tagline.setVisibility(View.VISIBLE);
//            }
//            overview.setText(movieResponse.getOverview());
//            String genre = "";
//            if (movieResponse.getGenres() != null) {
//                for (int i = 0; i < movieResponse.getGenres().size(); i++) {
//                    if (movieResponse.getGenres().get(i) == null) continue;
//                    if (i == movieResponse.getGenres().size() - 1) {
//                        genre = genre.concat(movieResponse.getGenres().get(i).getName());
//                    } else {
//                        genre = genre.concat(movieResponse.getGenres().get(i).getName() + ", ");
//                    }
//                }
//            }
//            genres.setText(genre);
//        } else {
//            TVShowResponse tvShowResponse = (TVShowResponse) mediaResponse;
//            path = tvShowResponse.getPosterPath();
//            title.setText(tvShowResponse.getOriginalName());
//            name = tvShowResponse.getOriginalName();
//            backdropPath = tvShowResponse.getBackdropPath();
//            posterPath = tvShowResponse.getPosterPath();
//            if (tvShowResponse.getNumberOfEpisodes() != null){
//                budget.setText(String.format("Number of episodes: %s", tvShowResponse.getNumberOfEpisodes()));
//                budget.setVisibility(View.VISIBLE);
//            }
//            if (tvShowResponse.getNumberOfSeasons() != null){
//                runtime.setText(String.format("Number of seasons: %s", tvShowResponse.getNumberOfSeasons()));
//                runtime.setVisibility(View.VISIBLE);
//            }
//            if (tvShowResponse.getFirstAirDate() != null){
//                releaseDate.setText(String.format("First air date: %s",
//                        Util.convertDateFromFormatToFormat(tvShowResponse.getFirstAirDate(),
//                        Util.DateFormats.INVERSE_FORMAT,
//                        Util.DateFormats.STANDARD_FORMAT)));
//            }
//            rating.setText(String.valueOf(tvShowResponse.getVoteAverage()));
//            overview.setText(tvShowResponse.getOverview());
//            String genre = "";
//            if (tvShowResponse.getGenres() != null) {
//                for (int i = 0; i < tvShowResponse.getGenres().size(); i++) {
//                    if (tvShowResponse.getGenres().get(i) == null) continue;
//                    if (i == tvShowResponse.getGenres().size() - 1) {
//                        genre = genre.concat(tvShowResponse.getGenres().get(i).getName());
//                    } else {
//                        genre = genre.concat(tvShowResponse.getGenres().get(i).getName() + ", ");
//                    }
//                }
//            }
//            genres.setText(genre);
//        }
//
//        if (backdropPath != null) {
//            Glide.with(FullMediaActivity.this)
//                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_1000.concat(backdropPath))
//                    .apply(RequestOptions.centerCropTransform())
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(imageBackdrop);
//        } else {
//            Glide.with(FullMediaActivity.this)
//                    .load(R.drawable.ic_baseline_movie_creation_24px)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(imageBackdrop);
//        }
//        if (posterPath != null) {
//            Glide.with(FullMediaActivity.this)
//                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_780.concat(posterPath))
//                    .apply(RequestOptions.centerCropTransform())
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(imagePoster);
//        } else {
//            Glide.with(FullMediaActivity.this)
//                    .load(R.drawable.ic_baseline_movie_creation_24px)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(imageBackdrop);
//        }
//    }
//
//    @Override
//    public void onMediaVideosLoaded(List<VideoInfo> videoInfoList) {
//        trailersAdapter.add(videoInfoList);
//        if (videoInfoList.size() > 0) {
//            trailersLabel.setVisibility(View.VISIBLE);
//            trailers.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onMediaCreditsLoaded(List<MovieCast> movieCastList) {
//        castAdapter.add(movieCastList);
//        if (movieCastList.size() > 0) {
//            castLabel.setVisibility(View.VISIBLE);
//            casts.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onSimilarMediaLoaded(List<? extends PreviewMedia> previewMediaList) {
//        similarMoviesAdapter.add(previewMediaList);
//        if (previewMediaList.size() > 0) {
//            similarsLabel.setVisibility(View.VISIBLE);
//            similars.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onFavorite(boolean isFavorite) {
//        if (isFavorite) {
//            fav.setImageResource(R.drawable.ic_baseline_favorite_24px);
//            fav.setTag("1");
//        } else {
//            fav.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
//            fav.setTag("0");
//        }
//    }
//
//    @Override
//    public void onComplete() {
//        progressBar.setVisibility(View.GONE);
//        scrollView.setVisibility(View.VISIBLE);
//        appBarLayout.setVisibility(View.VISIBLE);
//    }

}
