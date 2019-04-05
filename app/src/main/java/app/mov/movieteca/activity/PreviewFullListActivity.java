package app.mov.movieteca.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.mov.movieteca.R;
import app.mov.movieteca.adapter.PreviewMediaAdapter;
import app.mov.movieteca.model.BaseMovieResponse;
import app.mov.movieteca.model.BaseTVShowResponse;
import app.mov.movieteca.model.PreviewMedia;
import app.mov.movieteca.model.PreviewMovie;
import app.mov.movieteca.model.PreviewTVShow;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewFullListActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView fullListRecycler;
    private PreviewMediaAdapter previewMediaAdapter;
    private String type;
    private String internalType;

    private boolean pagesOver = false;
    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_full_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        type = getIntent().getStringExtra("type");
        String extra = getIntent().getStringExtra("internal_type");
        internalType = getIntent().getStringExtra("extra");
        setTitle(extra);
        Objects.requireNonNull(getSupportActionBar())
                .setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24px);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar);
        fullListRecycler = findViewById(R.id.full_list_recycler);
        previewMediaAdapter = new PreviewMediaAdapter(this, new ArrayList<PreviewMedia>(), true);
        fullListRecycler.setHasFixedSize(true);
        fullListRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(this, R.anim.layout_animation_down));
        fullListRecycler.setAdapter(previewMediaAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        fullListRecycler.setLayoutManager(gridLayoutManager);
        fullListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

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
                    if (type.equals(Util.Constants.MOVIE)) {
                        loadMovies();
                    } else {
                        loadTV();
                    }
                    loading = true;
                }
            }
        });
        if (type.equals(Util.Constants.MOVIE)) {
            loadMovies();
        } else {
            loadTV();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void loadTV() {
        if (pagesOver) return;
        progressBar.setVisibility(View.VISIBLE);
        fullListRecycler.setAlpha(0.3f);
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        Call<BaseTVShowResponse> call = null;
        switch (internalType) {
            case "now_playing":
                call = service.getAiringTodayTVShows(Util.Constants.API_KEY, presentPage);
                break;
            case "top_rated":
                call = service.getTopRatedTVShows(Util.Constants.API_KEY, presentPage);
                break;
            case "popular":
                call = service.getPopularTVShows(Util.Constants.API_KEY, presentPage);
                break;
            case "upcoming":
                call = service.getOnTheAirTVShows(Util.Constants.API_KEY, presentPage);
                break;
        }
        if (call != null) {
            call.enqueue(new Callback<BaseTVShowResponse>() {
                @Override
                public void onResponse(Call<BaseTVShowResponse> call, Response<BaseTVShowResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewTVShow> previewMovieList = response.body().getResults();
                        List<PreviewTVShow> previewMovieList1 = new ArrayList<>();
                        for (PreviewTVShow previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewMovieList1.add(previewMovie);
                        }
                        previewMediaAdapter.add(previewMovieList1);
                    }
                    if (response.body().getTotalPages().equals(response.body().getPage())){
                        pagesOver = true;
                    }
                    else {
                        presentPage++;
                    }
                    progressBar.setVisibility(View.GONE);
                    fullListRecycler.setVisibility(View.VISIBLE);
                    fullListRecycler.setAlpha(1.0f);
                }

                @Override
                public void onFailure(Call<BaseTVShowResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    fullListRecycler.setVisibility(View.VISIBLE);
                    fullListRecycler.setAlpha(1.0f);
                }
            });
        }
    }

    public void loadMovies(){
        if (pagesOver) return;
        progressBar.setVisibility(View.VISIBLE);
        fullListRecycler.setAlpha(0.3f);
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        Call<BaseMovieResponse> call = null;
        switch (internalType) {
            case "now_playing":
                call = service.getNowShowingMovies(Util.Constants.API_KEY, presentPage, "US");
                break;
            case "top_rated":
                call = service.getTopRatedMovies(Util.Constants.API_KEY, presentPage, "US");
                break;
            case "popular":
                call = service.getPopularMovies(Util.Constants.API_KEY, presentPage, "US");
                break;
            case "upcoming":
                call = service.getUpcomingMovies(Util.Constants.API_KEY, presentPage, "US");
                break;
        }
        if (call != null) {
            call.enqueue(new Callback<BaseMovieResponse>() {
                @Override
                public void onResponse(Call<BaseMovieResponse> call, Response<BaseMovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewMovie> previewMovieList = response.body().getResults();
                        List<PreviewMovie> previewMovieList1 = new ArrayList<>();
                        for (PreviewMovie previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewMovieList1.add(previewMovie);
                        }
                        previewMediaAdapter.add(previewMovieList1);
                    }
                    if (response.body().getTotalPages().equals(response.body().getPage())){
                        pagesOver = true;
                    }
                    else {
                        presentPage++;
                    }
                    progressBar.setVisibility(View.GONE);
                    fullListRecycler.setVisibility(View.VISIBLE);
                    fullListRecycler.setAlpha(1.0f);
                }

                @Override
                public void onFailure(Call<BaseMovieResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    fullListRecycler.setVisibility(View.VISIBLE);
                    fullListRecycler.setAlpha(1.0f);
                }
            });
        }
    }
}
