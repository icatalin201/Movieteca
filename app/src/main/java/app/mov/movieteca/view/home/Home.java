package app.mov.movieteca.view.home;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import app.mov.movieteca.R;
import app.mov.movieteca.view.adapter.SectionPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Home extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.container)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movies)
    TextView movies;
    @BindView(R.id.tv_shows)
    TextView tvShows;

    private AppCompatActivity appCompatActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
        sectionPagerAdapter.addFragment(new HomeMovieFragment(), new HomeSeriesFragment());
        viewPager.setAdapter(sectionPagerAdapter);
        appCompatActivity.setSupportActionBar(toolbar);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    movies.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    movies.setTypeface(null, Typeface.BOLD);
                    tvShows.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    tvShows.setTypeface(null, Typeface.NORMAL);
                } else {
                    movies.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    movies.setTypeface(null, Typeface.NORMAL);
                    tvShows.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    tvShows.setTypeface(null, Typeface.BOLD);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @OnClick(R.id.movies)
    void switchToMovies() {
        movies.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        movies.setTypeface(null, Typeface.BOLD);
        tvShows.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tvShows.setTypeface(null, Typeface.NORMAL);
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.tv_shows)
    void switchToSeries() {
        movies.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        movies.setTypeface(null, Typeface.NORMAL);
        tvShows.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvShows.setTypeface(null, Typeface.BOLD);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
