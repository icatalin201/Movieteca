package app.mov.movieteca.view.favorite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Comparator;

import app.mov.movieteca.R;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.view.adapter.SectionPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Favorites extends Fragment {

    public static final Comparator<FavoritePreviewMedia> ALPHABETICAL_COMPARATOR =
            (a, b) -> a.getName().compareTo(b.getName());

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    private AppCompatActivity appCompatActivity;
    private Unbinder unbinder;

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
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.favorites);
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        FavoriteMovies favoriteMovies = new FavoriteMovies();
        FavoriteCasts favoriteCasts = new FavoriteCasts();
        FavoriteShows favoriteShows = new FavoriteShows();
        sectionPagerAdapter.addFragment(favoriteMovies, favoriteShows, favoriteCasts);
        sectionPagerAdapter.addTitle("Movies", "TV Shows", "Actors");
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
