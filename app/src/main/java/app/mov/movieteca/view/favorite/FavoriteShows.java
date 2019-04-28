package app.mov.movieteca.view.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.mov.movieteca.R;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.view.adapter.FavoriteAdapter;
import app.mov.movieteca.view.viewmodel.FavoriteViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteShows extends Fragment {

    @BindView(R.id.no_content)
    TextView noContent;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.favorites)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private FavoriteAdapter favoriteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
        unbinder = ButterKnife.bind(this, view);
        favoriteAdapter = new FavoriteAdapter(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(getContext(), R.anim.layout_animation_down));
        FavoriteViewModel favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        favoriteViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        favoriteViewModel.getData().observe(this, favoritePreviewMedia -> {
            favoriteAdapter.add(favoritePreviewMedia);
            if (favoritePreviewMedia.size() == 0) {
                noContent.setVisibility(View.VISIBLE);
            } else {
                noContent.setVisibility(View.GONE);
            }
        });
        favoriteViewModel.getFavorites(Constants.TV_SHOW);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
