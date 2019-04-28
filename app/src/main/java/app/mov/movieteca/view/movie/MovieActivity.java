package app.mov.movieteca.view.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.response.CreditsResponse;
import app.mov.movieteca.model.response.MovieCast;
import app.mov.movieteca.model.response.MovieResponse;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.model.response.Review;
import app.mov.movieteca.model.response.VideoInfo;
import app.mov.movieteca.model.response.VideoResponse;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.util.DateUtils;
import app.mov.movieteca.util.Utils;
import app.mov.movieteca.view.adapter.CastAdapter;
import app.mov.movieteca.view.adapter.PreviewMovieAdapter;
import app.mov.movieteca.view.adapter.ReviewAdapter;
import app.mov.movieteca.view.adapter.TrailersAdapter;
import app.mov.movieteca.view.cast.CastActivity;
import app.mov.movieteca.view.viewmodel.MovieViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieActivity extends AppCompatActivity
        implements CastAdapter.OnItemClickListener,
        PreviewMovieAdapter.OnItemClickedListener,
        TrailersAdapter.OnItemClickListener, ReviewAdapter.OnItemClickListener {

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
    @BindView(R.id.reviews)
    TextView reviewsLabel;
    @BindView(R.id.recycler_reviews)
    RecyclerView reviews;

    private String path;
    private int movieId;
    private String name;
    private MovieViewModel movieViewModel;
    private ReviewAdapter reviewAdapter;

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
        setContentView(R.layout.activity_movie);
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
        reviewAdapter = new ReviewAdapter(this);
        reviews.setLayoutManager(getLinearLayoutManager());
        reviews.setLayoutAnimation(getLayoutAnimationController());
        reviews.setHasFixedSize(true);
        reviews.setAdapter(reviewAdapter);
        movieId = getIntent().getIntExtra("id", 0);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.start(movieId);

        movieViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });

        movieViewModel.getCredits().observe(this, this::setupCredits);
        movieViewModel.getMovie().observe(this, this::setupMovie);
        movieViewModel.getMovies().observe(this, this::setupMovies);
        movieViewModel.getVideos().observe(this, this::setupVideos);
        movieViewModel.getIsFavorite().observe(this, isFavorite -> {
            if (isFavorite) {
                fav.setImageResource(R.drawable.ic_baseline_favorite_24px);
                fav.setTag("1");
            } else {
                fav.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
                fav.setTag("0");
            }
        });
        movieViewModel.getReviews().observe(this, reviewList -> {
            if (reviewList.size() > 0) {
                reviewAdapter.add(reviewList);
                reviews.setVisibility(View.VISIBLE);
                reviewsLabel.setVisibility(View.VISIBLE);
            } else {
                reviews.setVisibility(View.GONE);
                reviewsLabel.setVisibility(View.GONE);
            }
        });
    }

    private void setupCredits(CreditsResponse creditsResponse) {
        CastAdapter castAdapter = new CastAdapter(this, this);
        casts.setLayoutManager(getLinearLayoutManager());
        casts.setLayoutAnimation(getLayoutAnimationController());
        casts.setHasFixedSize(true);
        casts.setAdapter(castAdapter);
        castAdapter.add(creditsResponse.getCast());
        if (creditsResponse.getCast().size() > 0) {
            castLabel.setVisibility(View.VISIBLE);
            casts.setVisibility(View.VISIBLE);
        } else {
            castLabel.setVisibility(View.GONE);
            casts.setVisibility(View.GONE);
        }
    }
    private void setupMovie(MovieResponse movieResponse) {
        name = movieResponse.getOriginalTitle();
        path = movieResponse.getPosterPath();
        if (movieResponse.getPosterPath() != null) {
            Glide.with(MovieActivity.this)
                    .load(Constants.IMAGE_LOADING_BASE_URL_780.concat(movieResponse.getPosterPath()))
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imagePoster);
        } else {
            Glide.with(MovieActivity.this)
                    .load(R.drawable.ic_baseline_movie_creation_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageBackdrop);
        }
        if (movieResponse.getBackdropPath() != null) {
            Glide.with(MovieActivity.this)
                    .load(Constants.IMAGE_LOADING_BASE_URL_1000.concat(movieResponse.getBackdropPath()))
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageBackdrop);
        } else {
            Glide.with(MovieActivity.this)
                    .load(R.drawable.ic_baseline_movie_creation_24px)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageBackdrop);
        }
        title.setText(movieResponse.getTitle());
        name = movieResponse.getTitle();
        if (movieResponse.getBudget() != null && movieResponse.getBudget() > 0){
            budget.setText(String.format("Budget: %s USD",
                    Utils.formatValue(movieResponse.getBudget())));
            budget.setVisibility(View.VISIBLE);
        } else {
            budget.setVisibility(View.GONE);
        }
        if (movieResponse.getRuntime() != null && movieResponse.getRuntime() > 0){
            runtime.setText(String.format("Runtime: %s minutes",
                    movieResponse.getRuntime()));
            runtime.setVisibility(View.VISIBLE);
        } else {
            runtime.setVisibility(View.GONE);
        }
        if (movieResponse.getRevenue() != null && movieResponse.getRevenue() > 0) {
            revenue.setText(String.format("Revenue: %s USD",
                    Utils.formatValue(movieResponse.getRevenue())));
            revenue.setVisibility(View.VISIBLE);
        } else {
            revenue.setVisibility(View.GONE);
        }
        if (movieResponse.getReleaseDate() != null && !movieResponse.getReleaseDate().equals("")){
            releaseDate.setText(String.format("Release date: %s",
                    DateUtils.convertDateFromFormatToFormat(movieResponse.getReleaseDate(),
                            DateUtils.STANDARD_DATE_FORMAT, DateUtils.INVERSE_DATE_FORMAT)));
            releaseDate.setVisibility(View.VISIBLE);
        } else {
            releaseDate.setVisibility(View.GONE);
        }
        rating.setText(String.valueOf(movieResponse.getVoteAverage()));
        if (movieResponse.getTagline() != null) {
            tagline.setText(movieResponse.getTagline());
            tagline.setVisibility(View.VISIBLE);
        } else {
            tagline.setVisibility(View.GONE);
        }
        overview.setText(movieResponse.getOverview());
        String genre = "";
        if (movieResponse.getGenres() != null) {
            for (int i = 0; i < movieResponse.getGenres().size(); i++) {
                if (movieResponse.getGenres().get(i) == null) continue;
                if (i == movieResponse.getGenres().size() - 1) {
                    genre = genre.concat(movieResponse.getGenres().get(i).getName());
                } else {
                    genre = genre.concat(movieResponse.getGenres().get(i).getName() + ", ");
                }
            }
        }
        genres.setText(genre);
    }
    private void setupMovies(List<PreviewMovie> previewMovieList) {
        PreviewMovieAdapter similarMoviesAdapter =
                new PreviewMovieAdapter(this, this, false);
        similars.setLayoutManager(getLinearLayoutManager());
        similars.setLayoutAnimation(getLayoutAnimationController());
        similars.setHasFixedSize(true);
        similars.setAdapter(similarMoviesAdapter);
        similarMoviesAdapter.add(previewMovieList);
        if (previewMovieList.size() > 0) {
            similarsLabel.setVisibility(View.VISIBLE);
            similars.setVisibility(View.VISIBLE);
        } else {
            similarsLabel.setVisibility(View.GONE);
            similars.setVisibility(View.GONE);
        }
    }
    private void setupVideos(VideoResponse videoResponse) {
        TrailersAdapter trailersAdapter = new TrailersAdapter(this, this);
        trailers.setLayoutManager(getLinearLayoutManager());
        trailers.setLayoutAnimation(getLayoutAnimationController());
        trailers.setHasFixedSize(true);
        trailers.setAdapter(trailersAdapter);
        trailersAdapter.add(videoResponse.getResults());
        if (videoResponse.getResults().size() > 0) {
            trailersLabel.setVisibility(View.VISIBLE);
            trailers.setVisibility(View.VISIBLE);
        } else {
            trailersLabel.setVisibility(View.GONE);
            trailers.setVisibility(View.GONE);
        }
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
            movieViewModel.removeFavorite(movieId);
            Utils.notify(coordinatorLayout, title.getText().toString()
                    .concat(" has been deleted from favorite collection."));
        }
        else if (fav.getTag().equals("0")){
            FavoritePreviewMedia favoritePreviewMedia = new FavoritePreviewMedia();
            favoritePreviewMedia.setResType(Constants.MOVIE);
            favoritePreviewMedia.setResId(movieId);
            favoritePreviewMedia.setPoster(path);
            favoritePreviewMedia.setName(title.getText().toString());
            movieViewModel.addFavorite(favoritePreviewMedia);
            Utils.notify(coordinatorLayout, title.getText().toString()
                    .concat(" has been added to favorite collection."));
        }
    }

    @Override
    public void onClick(MovieCast movieCast) {
        Intent intent = new Intent(this, CastActivity.class);
        intent.putExtra("id", movieCast.getId());
        startActivity(intent);
    }

    @Override
    public void onClick(PreviewMovie previewMovie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("id", previewMovie.getId());
        startActivity(intent);
    }

    @Override
    public void onClick(VideoInfo videoInfo) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(Constants.YOUTUBE_WATCH_BASE_URL + videoInfo.getKey()));
        startActivity(intent);
    }

    @Override
    public void onClick(Review review) {
        if (review.getUrl() != null && !review.getUrl().equals("")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
            startActivity(intent);
        }
    }
}
