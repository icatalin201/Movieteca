package app.mov.movieteca.view.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import app.mov.movieteca.R;
import app.mov.movieteca.model.response.PreviewMovie;
import app.mov.movieteca.model.response.PreviewTVShow;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.view.LoginActivity;
import app.mov.movieteca.view.adapter.PreviewMovieAdapter;
import app.mov.movieteca.view.adapter.PreviewShowAdapter;
import app.mov.movieteca.view.viewmodel.ProfileViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Profile extends Fragment
        implements PreviewMovieAdapter.OnItemClickedListener, PreviewShowAdapter.OnItemClickedListener {

    private Unbinder unbinder;
    private AppCompatActivity appCompatActivity;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rated_movies)
    RecyclerView ratedMovies;
    @BindView(R.id.rated_shows)
    RecyclerView ratedShows;
    @BindView(R.id.rated_movies_label)
    TextView ratedMoviesLabel;
    @BindView(R.id.rated_shows_label)
    TextView ratedShowsLabel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.log)
    Button button;
    @BindView(R.id.layout)
    NestedScrollView nestedScrollView;

    private ProfileViewModel profileViewModel;
    private int accountId;
    private PreviewMovieAdapter previewMovieAdapter;
    private PreviewShowAdapter previewShowAdapter;

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

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
    }

    private LayoutAnimationController getLayoutAnimationController() {
        return AnimationUtils.loadLayoutAnimation(appCompatActivity, R.anim.layout_animation_down);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.setTitle("");
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (appBarLayout.getTotalScrollRange() + verticalOffset == 0) {
                collapsingToolbarLayout.setTitle(name.getText().toString());
            } else {
                collapsingToolbarLayout.setTitle("");
            }
        });
        previewMovieAdapter = new PreviewMovieAdapter(this, appCompatActivity, true);
        previewShowAdapter = new PreviewShowAdapter(this, appCompatActivity, true);
        ratedMovies.setHasFixedSize(true);
        ratedMovies.setLayoutAnimation(getLayoutAnimationController());
        ratedMovies.setLayoutManager(getLinearLayoutManager());
        ratedShows.setHasFixedSize(true);
        ratedShows.setLayoutAnimation(getLayoutAnimationController());
        ratedShows.setLayoutManager(getLinearLayoutManager());
        ratedMovies.setAdapter(previewMovieAdapter);
        ratedShows.setAdapter(previewShowAdapter);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getAccount().observe(this, account -> {
           button.setVisibility(View.VISIBLE);
           Glide.with(appCompatActivity)
                .load(String.format(Constants.GRAVATAR, account.getAvatar().getGravatar().getHash()))
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);
            name.setText(account.getName());
            username.setText(String.format("@%s", account.getUsername()));
        });
        profileViewModel.getRatedMovies().observe(this, previewMovies -> {
            if (previewMovies == null || previewMovies.size() == 0) {
                ratedMovies.setVisibility(View.GONE);
                ratedMoviesLabel.setVisibility(View.GONE);
            } else {
                previewMovieAdapter.add(previewMovies);
                ratedMovies.setVisibility(View.VISIBLE);
                ratedMoviesLabel.setVisibility(View.VISIBLE);
            }
        });
        profileViewModel.getRatedShows().observe(this, previewTVShowList -> {
            if (previewTVShowList == null || previewTVShowList.size() == 0) {
                ratedShows.setVisibility(View.GONE);
                ratedShowsLabel.setVisibility(View.GONE);
            } else {
                previewShowAdapter.add(previewTVShowList);
                ratedShows.setVisibility(View.VISIBLE);
                ratedShowsLabel.setVisibility(View.VISIBLE);
            }
        });
        profileViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    @OnClick(R.id.log)
    void logClick() {
        profileViewModel.logout();
        startActivity(new Intent(appCompatActivity, LoginActivity.class));
        appCompatActivity.finish();
    }

    @Override
    public void onClick(PreviewMovie previewMovie) {

    }

    @Override
    public void onClick(PreviewTVShow previewTVShow) {

    }
}
