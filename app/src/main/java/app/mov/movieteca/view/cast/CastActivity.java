package app.mov.movieteca.view.activity;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.MovieCastsForPerson;
import app.mov.movieteca.model.Person;
import app.mov.movieteca.model.ShowCastsForPerson;
import app.mov.movieteca.presenter.CastPresenter;
import app.mov.movieteca.util.Util;
import app.mov.movieteca.view.adapter.PersonMediaAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CastActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.cast_layout)
    NestedScrollView layout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.birthplace)
    TextView birthplace;
    @BindView(R.id.bio)
    TextView bio;
    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.movies_label)
    TextView moviesLabel;
    @BindView(R.id.shows_label)
    TextView showsLabel;
    @BindView(R.id.favorite)
    ImageButton favorite;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.movie_role_recycler)
    RecyclerView movieRecycler;
    @BindView(R.id.show_role_recycler)
    RecyclerView showRecycler;

    private PersonMediaAdapter moviesAdapter;
    private PersonMediaAdapter showsAdapter;

    private int castId;
    private String path;

    private CastPresenter castPresenter;

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }

    private LayoutAnimationController getLayoutAnimationController() {
        return AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        castId = getIntent().getIntExtra(Util.Constants.CAST_ID, 0);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (appBarLayout.getTotalScrollRange() + verticalOffset == 0) {
                collapsingToolbarLayout.setTitle(name.getText().toString());
            } else {
                collapsingToolbarLayout.setTitle("");
            }
        });
        movieRecycler.setHasFixedSize(true);
        showRecycler.setHasFixedSize(true);
        movieRecycler.setLayoutAnimation(getLayoutAnimationController());
        showRecycler.setLayoutAnimation(getLayoutAnimationController());
        movieRecycler.setLayoutManager(getLinearLayoutManager());
        showRecycler.setLayoutManager(getLinearLayoutManager());
        moviesAdapter = new PersonMediaAdapter(this, new ArrayList<>());
        showsAdapter = new PersonMediaAdapter(this, new ArrayList<>());
        movieRecycler.setAdapter(moviesAdapter);
        showRecycler.setAdapter(showsAdapter);
        castPresenter.load(castId);
    }

    @OnClick(R.id.share)
    public void share() {
        Intent movieShareIntent = new Intent(Intent.ACTION_SEND);
        movieShareIntent.setType("text/plain");
        String extraText = name.getText().toString();
        movieShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
        startActivity(movieShareIntent);
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.favorite)
    public void toggleFavorite() {
        if (favorite.getTag().equals("1")) {
            castPresenter.removeFromFavorites(castId, Util.Constants.ACTOR);
            Util.notify(coordinatorLayout, name.getText().toString()
                    .concat(" has been deleted from favorite collection."));
            favorite.setTag("0");
            favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
        }
        else if (favorite.getTag().equals("0")){
            FavoritePreviewMedia favoritePreviewMedia = new FavoritePreviewMedia();
            favoritePreviewMedia.setResType(Util.Constants.ACTOR);
            favoritePreviewMedia.setResId(castId);
            favoritePreviewMedia.setPoster(path);
            favoritePreviewMedia.setName(name.getText().toString());
            castPresenter.addToFavorites(favoritePreviewMedia);
            Util.notify(coordinatorLayout, name.getText().toString()
                    .concat(" has been added to favorite collection."));
            favorite.setTag("1");
            favorite.setImageResource(R.drawable.ic_baseline_favorite_24px);
        }
    }

//    @Override
//    public void onCastLoaded(Person person) {
//        setTitle(person.getName());
//        bio.setText(person.getBiography());
//        birthplace.setText(String.format("Birthplace: %s", person.getPlaceOfBirth()));
//        String g = person.getGender() == 1 ? "Female" : "Male";
//        gender.setText(String.format("Gender: %s", g));
//        name.setText(person.getName());
//        if (person.getProfilePath() != null) {
//            path = person.getProfilePath();
//            Glide.with(CastActivity.this)
//                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_780
//                            .concat(person.getProfilePath()))
//                    .apply(RequestOptions.circleCropTransform())
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(image);
//        } else {
//            Glide.with(CastActivity.this)
//                    .load(R.drawable.ic_baseline_person_24px)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .into(image);
//        }
//        if (person.getBirthday() != null) {
//            Date date = Util.convertStringToDate(person.getBirthday(), Util.DateFormats.INVERSE_FORMAT);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            int birthYear = calendar.get(Calendar.YEAR);
//            if (person.getDeathday() == null) {
//                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//                age.setText(String.format("Age: %s years", currentYear - birthYear));
//            } else {
//                String death = Util.convertDateFromFormatToFormat(person.getDeathday(),
//                        Util.DateFormats.INVERSE_FORMAT, Util.DateFormats.STANDARD_FORMAT);
//                Date date1 = Util.convertStringToDate(person.getDeathday(), Util.DateFormats.INVERSE_FORMAT);
//                Calendar calendar1 = Calendar.getInstance();
//                calendar1.setTime(date1);
//                int deathYear = calendar1.get(Calendar.YEAR);
//                age.setText(String.format("Died on %s at %s years", death, deathYear - birthYear));
//            }
//        }
//    }
//
//    @Override
//    public void onComplete() {
//        progressBar.setVisibility(View.GONE);
//        layout.setVisibility(View.VISIBLE);
//        appBarLayout.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onLoading() {
//        progressBar.setVisibility(View.VISIBLE);
//        layout.setVisibility(View.GONE);
//        appBarLayout.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onFailure(String message) {
//
//    }
//
//    @Override
//    public void onCastMoviesLoaded(List<MovieCastsForPerson> movieCastsForPersonList) {
//        moviesAdapter.add(movieCastsForPersonList);
//        if (movieCastsForPersonList.size() > 0) moviesLabel.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onCastShowsLoaded(List<ShowCastsForPerson> showCastsForPersonList) {
//        showsAdapter.add(showCastsForPersonList);
//        if (showCastsForPersonList.size() > 0) showsLabel.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onFavorite(boolean isFavorite) {
//        if (isFavorite) {
//            favorite.setImageResource(R.drawable.ic_baseline_favorite_24px);
//            favorite.setTag("1");
//        }
//        else {
//            favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
//            favorite.setTag("0");
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
