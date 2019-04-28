package app.mov.movieteca.view.tvshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.PreviewTVShow;
import app.mov.movieteca.view.adapter.PreviewShowAdapter;
import app.mov.movieteca.view.viewmodel.GenreShowViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreShowActivity extends AppCompatActivity implements PreviewShowAdapter.OnItemClickedListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.full_list_recycler)
    RecyclerView fullListRecycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    private PreviewShowAdapter previewShowAdapter;
    private GenreShowViewModel genreShowViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_show);
        ButterKnife.bind(this);
        String genre = getIntent().getStringExtra("genre");
        String title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24px);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        genreShowViewModel = ViewModelProviders.of(this).get(GenreShowViewModel.class);
        previewShowAdapter = new PreviewShowAdapter(this, this, true);
        genreShowViewModel.find(presentPage, genre);
        fullListRecycler.setHasFixedSize(true);
        fullListRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_down));
        fullListRecycler.setAdapter(previewShowAdapter);
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
                    genreShowViewModel.find(presentPage, genre);
                    loading = true;
                }
            }
        });
        genreShowViewModel.getData().observe(this, previewMovies -> {
            previewShowAdapter.add(previewMovies);
            presentPage++;
        });
        genreShowViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                fullListRecycler.setAlpha(0.3f);
            } else {
                progressBar.setVisibility(View.GONE);
                fullListRecycler.setAlpha(1.0f);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(PreviewTVShow previewTVShow) {
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra("id", previewTVShow.getId());
        startActivity(intent);
    }
}
