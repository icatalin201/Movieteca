package app.mov.movieteca.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.mov.movieteca.R;
import app.mov.movieteca.adapter.CastAdapter;
import app.mov.movieteca.adapter.PreviewMediaAdapter;
import app.mov.movieteca.adapter.TrailersAdapter;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.model.BaseMovieResponse;
import app.mov.movieteca.model.BaseTVShowResponse;
import app.mov.movieteca.model.Credits;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.Movie;
import app.mov.movieteca.model.MovieCast;
import app.mov.movieteca.model.PreviewMedia;
import app.mov.movieteca.model.PreviewMovie;
import app.mov.movieteca.model.PreviewTVShow;
import app.mov.movieteca.model.TVShow;
import app.mov.movieteca.model.VideoInfo;
import app.mov.movieteca.model.Videos;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Helper;
import app.mov.movieteca.utils.LoadHelper;
import app.mov.movieteca.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullMediaActivity extends AppCompatActivity implements LoadHelper {

    private ProgressBar progressBar;
    private ScrollView scrollView;

    private ImageView imageBackdrop;
    private ImageView imagePoster;
    private TextView title;
    private TextView genres;
    private TextView rating;
    private TextView tagline;
    private TextView overview;
    private TextView releaseDate;
    private TextView runtime;
    private TextView budget;
    private TextView revenue;

    private PreviewMediaAdapter similarMoviesAdapter;
    private TrailersAdapter trailersAdapter;
    private CastAdapter castAdapter;

    private String type;
    private int count;

    private String path;

    private TextView trailersLabel;
    private TextView castLabel;
    private TextView similarsLabel;
    private RecyclerView trailers;
    private RecyclerView similars;
    private RecyclerView casts;

    @Override
    public boolean onSupportNavigateUp() {
        setResult(1);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_media);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        count = 0;
        final int movieId = getIntent().getIntExtra("id", 0);
        type = getIntent().getStringExtra("type");
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.entire_layout);
        imageBackdrop = findViewById(R.id.image_backdrop);
        imagePoster = findViewById(R.id.image_poster);
        trailersLabel = findViewById(R.id.trailers);
        castLabel = findViewById(R.id.cast);
        similarsLabel = findViewById(R.id.similar);
        title = findViewById(R.id.title);
        genres = findViewById(R.id.genre);
        rating = findViewById(R.id.rating);
        tagline = findViewById(R.id.tagline);
        overview = findViewById(R.id.overview);
        releaseDate = findViewById(R.id.release_date);
        runtime = findViewById(R.id.runtime);
        budget = findViewById(R.id.budget);
        revenue = findViewById(R.id.revenue);
        final ImageButton fav = findViewById(R.id.favorite);
        trailers = findViewById(R.id.recycler_trailers);
        similars = findViewById(R.id.recycler_similar);
        casts = findViewById(R.id.recycler_cast);
        similarMoviesAdapter = new PreviewMediaAdapter(this, new ArrayList<PreviewMedia>(), false);
        trailersAdapter = new TrailersAdapter(this, new ArrayList<VideoInfo>());
        castAdapter = new CastAdapter(this, new ArrayList<MovieCast>());
        trailers.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        similars.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        casts.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        trailers.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(this, R.anim.layout_animation_down));
        similars.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(this, R.anim.layout_animation_down));
        casts.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(this, R.anim.layout_animation_down));
        trailers.setHasFixedSize(true);
        similars.setHasFixedSize(true);
        casts.setHasFixedSize(true);
        trailers.setAdapter(trailersAdapter);
        similars.setAdapter(similarMoviesAdapter);
        casts.setAdapter(castAdapter);
        LoadData loadData = new LoadData(this);
        if (type.equals(Util.Constants.MOVIE)) {
            loadData.loadMovieData(movieId);
        } else {
            loadData.loadTVData(movieId);
        }
        if (Handler.isFavorite(this, movieId, type)){
            fav.setImageResource(R.drawable.ic_baseline_favorite_24px);
            fav.setTag("1");
        }
        else {
            fav.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
            fav.setTag("0");
        }
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fav.getTag().equals("1")){
                    Handler.removeFavorite(FullMediaActivity.this, movieId, type);
                    Helper.notifyUser("remove", title.getText().toString(), FullMediaActivity.this);
                    fav.setTag("0");
                    fav.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
                }
                else if (fav.getTag().equals("0")){
                    FavoritePreviewMedia favoritePreviewMedia = new FavoritePreviewMedia();
                    favoritePreviewMedia.setResType(type);
                    favoritePreviewMedia.setResId(movieId);
                    favoritePreviewMedia.setPoster(path);
                    favoritePreviewMedia.setName(title.getText().toString());
                    Handler.insertFavorite(FullMediaActivity.this, favoritePreviewMedia);
                    Helper.notifyUser("add", title.getText().toString(), FullMediaActivity.this);
                    fav.setTag("1");
                    fav.setImageResource(R.drawable.ic_baseline_favorite_24px);
                }
            }
        });
    }

    @Override
    public void onLoadComplete(boolean status) {
        count++;
        if (count == 4) {
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private class LoadData {

        private LoadHelper loadHelper;

        LoadData(LoadHelper loadHelper) {
            this.loadHelper = loadHelper;
        }

        void loadMovieData(Integer movieId){
            NetworkService service = NetworkClient.getClient().create(NetworkService.class);
            Call<Movie> movieCall = service.getMovieDetails(movieId, Util.Constants.API_KEY);
            movieCall.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Movie movie = response.body();
                        path = movie.getPoster_path();
                        title.setText(movie.getTitle());
                        setTitle(movie.getTitle());
                        if (movie.getBackdrop_path() != null) {
                            Glide.with(FullMediaActivity.this)
                                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_780
                                            .concat(movie.getBackdrop_path()))
                                    .apply(RequestOptions.centerCropTransform())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(imageBackdrop);
                        }
                        if (movie.getPoster_path() != null) {
                            Glide.with(FullMediaActivity.this)
                                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_780
                                            .concat(movie.getPoster_path()))
                                    .apply(RequestOptions.centerCropTransform())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(imagePoster);
                        }
                        if (movie.getBudget() != null && movie.getBudget() > 0){
                            budget.setText(String.format("Budget: %s USD", Helper.formatValue(movie.getBudget())));
                            budget.setVisibility(View.VISIBLE);
                        }
                        if (movie.getRuntime() != null && movie.getRuntime() > 0){
                            runtime.setText(String.format("Runtime: %s minutes", movie.getRuntime()));
                            runtime.setVisibility(View.VISIBLE);
                        }
                        if (movie.getRevenue() != null && movie.getRevenue() > 0) {
                            revenue.setText(String.format("Revenue: %s USD", Helper.formatValue(movie.getRevenue())));
                            revenue.setVisibility(View.VISIBLE);
                        }
                        if (movie.getRelease_date() != null){
                            releaseDate.setText(String.format("Release date: %s", Helper.formatDate(movie.getRelease_date())));
                            releaseDate.setVisibility(View.VISIBLE);
                        }
                        rating.setText(String.valueOf(movie.getVote_average()));
                        if (movie.getTagline() != null) {
                            tagline.setText(movie.getTagline());
                            tagline.setVisibility(View.VISIBLE);
                        }
                        overview.setText(movie.getOverview());
                        String genre = "";
                        if (movie.getGenres() != null) {
                            for (int i = 0; i < movie.getGenres().size(); i++) {
                                if (movie.getGenres().get(i) == null) continue;
                                if (i == movie.getGenres().size() - 1) {
                                    genre = genre.concat(movie.getGenres().get(i).getName());
                                } else {
                                    genre = genre.concat(movie.getGenres().get(i).getName() + ", ");
                                }
                            }
                        }
                        genres.setText(genre);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<BaseMovieResponse> similarMoviesCall = service.getSimilarMovies(movieId, Util.Constants.API_KEY, 1);
            similarMoviesCall.enqueue(new Callback<BaseMovieResponse>() {
                @Override
                public void onResponse(Call<BaseMovieResponse> call, Response<BaseMovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewMovie> previewMovieList = response.body().getResults();
                        List<PreviewMovie> previewMovieList1 = new ArrayList<>();
                        for (PreviewMovie previewMovie : previewMovieList) {
                            if (previewMovie == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewMovieList1.add(previewMovie);
                        }
                        if (previewMovieList1.size() > 0) {
                            similarsLabel.setVisibility(View.VISIBLE);
                            similars.setVisibility(View.VISIBLE);
                        }
                        similarMoviesAdapter.add(previewMovieList1);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<BaseMovieResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<Credits> movieCreditsCall = service.getMovieCredits(movieId, Util.Constants.API_KEY);
            movieCreditsCall.enqueue(new Callback<Credits>() {
                @Override
                public void onResponse(Call<Credits> call, Response<Credits> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MovieCast> movieCasts = response.body().getCast();
                        if (movieCasts.size() > 0) {
                            castLabel.setVisibility(View.VISIBLE);
                            casts.setVisibility(View.VISIBLE);
                        }
                        castAdapter.add(movieCasts);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<Credits> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<Videos> videosCall = service.getMovieVideos(movieId, Util.Constants.API_KEY);
            videosCall.enqueue(new Callback<Videos>() {
                @Override
                public void onResponse(Call<Videos> call, Response<Videos> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<VideoInfo> videoInfos = response.body().getResults();
                        if (videoInfos.size() > 0) {
                            trailersLabel.setVisibility(View.VISIBLE);
                            trailers.setVisibility(View.VISIBLE);
                        }
                        trailersAdapter.add(videoInfos);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<Videos> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });

        }

        void loadTVData(Integer movieId){
            NetworkService service = NetworkClient.getClient().create(NetworkService.class);
            Call<TVShow> movieCall = service.getTVShowDetails(movieId, Util.Constants.API_KEY);
            movieCall.enqueue(new Callback<TVShow>() {
                @Override
                public void onResponse(Call<TVShow> call, Response<TVShow> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        TVShow movie = response.body();
                        path = movie.getPoster_path();
                        title.setText(movie.getOriginal_name());
                        setTitle(movie.getOriginal_name());
                        if (movie.getBackdrop_path() != null) {
                            Glide.with(FullMediaActivity.this)
                                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_780
                                            .concat(movie.getBackdrop_path()))
                                    .apply(RequestOptions.centerCropTransform())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(imageBackdrop);
                        }
                        if (movie.getPoster_path() != null) {
                            Glide.with(FullMediaActivity.this)
                                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_780
                                            .concat(movie.getPoster_path()))
                                    .apply(RequestOptions.centerCropTransform())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(imagePoster);
                        }
                        if (movie.getNumber_of_episodes() != null){
                            budget.setText(String.format("Number of episodes: %s", movie.getNumber_of_episodes()));
                            budget.setVisibility(View.VISIBLE);
                        }
                        if (movie.getNumber_of_seasons() != null){
                            runtime.setText(String.format("Number of seasons: %s", movie.getNumber_of_seasons()));
                            runtime.setVisibility(View.VISIBLE);
                        }
                        if (movie.getFirst_air_date() != null){
                            releaseDate.setText(String.format("First air date: %s", Helper.formatDate(movie.getFirst_air_date())));
                        }
                        rating.setText(String.valueOf(movie.getVote_average()));
                        overview.setText(movie.getOverview());
                        String genre = "";
                        if (movie.getGenres() != null) {
                            for (int i = 0; i < movie.getGenres().size(); i++) {
                                if (movie.getGenres().get(i) == null) continue;
                                if (i == movie.getGenres().size() - 1) {
                                    genre = genre.concat(movie.getGenres().get(i).getName());
                                } else {
                                    genre = genre.concat(movie.getGenres().get(i).getName() + ", ");
                                }
                            }
                        }
                        genres.setText(genre);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<TVShow> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<BaseTVShowResponse> similarMoviesCall = service.getSimilarTVShows(movieId, Util.Constants.API_KEY, 1);
            similarMoviesCall.enqueue(new Callback<BaseTVShowResponse>() {
                @Override
                public void onResponse(Call<BaseTVShowResponse> call, Response<BaseTVShowResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewTVShow> previewMovieList = response.body().getResults();
                        List<PreviewTVShow> previewMovieList1 = new ArrayList<>();
                        for (PreviewTVShow previewMovie : previewMovieList) {
                            if (previewMovie == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewMovieList1.add(previewMovie);
                        }
                        if (previewMovieList1.size() > 0) {
                            similarsLabel.setVisibility(View.VISIBLE);
                            similars.setVisibility(View.VISIBLE);
                        }
                        similarMoviesAdapter.add(previewMovieList1);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<BaseTVShowResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<Credits> movieCreditsCall = service.getTVShowCredits(movieId, Util.Constants.API_KEY);
            movieCreditsCall.enqueue(new Callback<Credits>() {
                @Override
                public void onResponse(Call<Credits> call, Response<Credits> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MovieCast> movieCasts = response.body().getCast();
                        if (movieCasts.size() > 0) {
                            castLabel.setVisibility(View.VISIBLE);
                            casts.setVisibility(View.VISIBLE);
                        }
                        castAdapter.add(movieCasts);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<Credits> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<Videos> videosCall = service.getTVShowVideos(movieId, Util.Constants.API_KEY);
            videosCall.enqueue(new Callback<Videos>() {
                @Override
                public void onResponse(Call<Videos> call, Response<Videos> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<VideoInfo> videoInfos = response.body().getResults();
                        if (videoInfos.size() > 0) {
                            trailersLabel.setVisibility(View.VISIBLE);
                            trailers.setVisibility(View.VISIBLE);
                        }
                        trailersAdapter.add(videoInfos);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<Videos> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });

        }

    }
}
