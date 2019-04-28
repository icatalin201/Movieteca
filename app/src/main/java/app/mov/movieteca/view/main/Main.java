package app.mov.movieteca.view.main;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.Person;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.view.adapter.CastAdapter;
import app.mov.movieteca.view.adapter.PeopleAdapter;
import app.mov.movieteca.view.cast.CastActivity;
import app.mov.movieteca.view.movie.MovieActivity;
import app.mov.movieteca.view.tvshow.ShowActivity;
import app.mov.movieteca.view.viewmodel.MainViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Main extends Fragment implements PeopleAdapter.OnItemClickListener {

    private Unbinder unbinder;
    private AppCompatActivity appCompatActivity;
    private MainViewModel mainViewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movie_image)
    ImageView movieImage;
    @BindView(R.id.show_image)
    ImageView showImage;
    @BindView(R.id.movie_title)
    TextView movieTitle;
    @BindView(R.id.show_title)
    TextView showTitle;
    @BindView(R.id.movie_overview)
    TextView movieOverview;
    @BindView(R.id.show_overview)
    TextView showOverview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layout)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recycler_popular)
    RecyclerView populars;

    private PeopleAdapter castAdapter;
    private int movieId, showId;

    private int presentPage = 1;
    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.app_name);
        appCompatActivity.setSupportActionBar(toolbar);
        castAdapter = new PeopleAdapter(this, appCompatActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(appCompatActivity,
                LinearLayoutManager.HORIZONTAL, false);
        populars.setLayoutManager(linearLayoutManager);
        populars.setHasFixedSize(true);
        populars.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(appCompatActivity, R.anim.layout_animation_down));
        populars.setAdapter(castAdapter);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
            }
        });
        mainViewModel.getMovie().observe(this, movieResponse -> {
            movieId = movieResponse.getId();
            if (movieResponse.getPosterPath() != null) {
                Glide.with(appCompatActivity)
                        .load(Constants.IMAGE_LOADING_BASE_URL_1000
                                .concat(movieResponse.getPosterPath()))
                        .apply(RequestOptions.centerCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(movieImage);
            } else {
                Glide.with(appCompatActivity)
                        .load(R.drawable.ic_baseline_movie_creation_24px)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(movieImage);
            }
            movieTitle.setText(movieResponse.getOriginalTitle());
            movieOverview.setText(movieResponse.getOverview());
        });
        mainViewModel.getShow().observe(this, tvShowResponse -> {
            showId = tvShowResponse.getId();
            if (tvShowResponse.getPosterPath() != null) {
                Glide.with(appCompatActivity)
                        .load(Constants.IMAGE_LOADING_BASE_URL_1000
                                .concat(tvShowResponse.getPosterPath()))
                        .apply(RequestOptions.centerCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(showImage);
            } else {
                Glide.with(appCompatActivity)
                        .load(R.drawable.ic_baseline_movie_creation_24px)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(showImage);
            }
            showTitle.setText(tvShowResponse.getOriginalName());
            showOverview.setText(tvShowResponse.getOverview());
        });
        mainViewModel.getPersons().observe(this, people -> {
            castAdapter.add(people);
            presentPage++;
        });
        populars.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    mainViewModel.loadPopulars(presentPage);
                    loading = true;
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(Person person) {
        Intent intent = new Intent(appCompatActivity, CastActivity.class);
        intent.putExtra("id", person.getId());
        startActivity(intent);
    }

    @OnClick(R.id.movie)
    void onMovieClick() {
        Intent intent = new Intent(appCompatActivity, MovieActivity.class);
        intent.putExtra("id", movieId);
        startActivity(intent);
    }

    @OnClick(R.id.show)
    void onShowClick() {
        Intent intent = new Intent(appCompatActivity, ShowActivity.class);
        intent.putExtra("id", showId);
        startActivity(intent);
    }
}
