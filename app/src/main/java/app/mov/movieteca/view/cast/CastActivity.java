package app.mov.movieteca.view.cast;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Calendar;
import java.util.Date;

import app.mov.movieteca.R;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.response.MovieCastsForPerson;
import app.mov.movieteca.model.response.ShowCastsForPerson;
import app.mov.movieteca.util.Constants;
import app.mov.movieteca.util.DateUtils;
import app.mov.movieteca.util.Utils;
import app.mov.movieteca.view.adapter.PersonMovieAdapter;
import app.mov.movieteca.view.adapter.PersonShowAdapter;
import app.mov.movieteca.view.movie.MovieActivity;
import app.mov.movieteca.view.tvshow.ShowActivity;
import app.mov.movieteca.view.viewmodel.CastViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CastActivity extends AppCompatActivity
        implements PersonMovieAdapter.OnItemClickListener, PersonShowAdapter.OnItemClickListener {

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int castId;
    private String path;

    private CastViewModel castViewModel;
    private PersonMovieAdapter personMovieAdapter;
    private PersonShowAdapter personShowAdapter;

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
        setSupportActionBar(toolbar);
        setTitle("");
        castId = getIntent().getIntExtra("id", 0);
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
        personMovieAdapter = new PersonMovieAdapter(this, this);
        personShowAdapter = new PersonShowAdapter(this, this);
        movieRecycler.setAdapter(personMovieAdapter);
        showRecycler.setAdapter(personShowAdapter);
        castViewModel = ViewModelProviders.of(this).get(CastViewModel.class);
        castViewModel.start(castId);
        castViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });
        castViewModel.getIsFavorite().observe(this, isFavorite -> {
            if (isFavorite) {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_24px);
                favorite.setTag("1");
            }
            else {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
                favorite.setTag("0");
            }
        });
        castViewModel.getPerson().observe(this, person -> {
            setTitle(person.getName());
            bio.setText(person.getBiography());
            birthplace.setText(String.format("Birthplace: %s", person.getPlaceOfBirth()));
            String g = person.getGender() == 1 ? "Female" : "Male";
            gender.setText(String.format("Gender: %s", g));
            name.setText(person.getName());
            if (person.getProfilePath() != null) {
                path = person.getProfilePath();
                Glide.with(CastActivity.this)
                        .load(Constants.IMAGE_LOADING_BASE_URL_780
                                .concat(person.getProfilePath()))
                        .apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(image);
            } else {
                Glide.with(CastActivity.this)
                        .load(R.drawable.ic_baseline_person_24px)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(image);
            }
            if (person.getBirthday() != null) {
                Date date = DateUtils.convertStringToDate(person.getBirthday(), DateUtils.STANDARD_DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int birthYear = calendar.get(Calendar.YEAR);
                if (person.getDeathday() == null) {
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    age.setText(String.format("Age: %s years", currentYear - birthYear));
                } else {
                    String death = DateUtils.convertDateFromFormatToFormat(person.getDeathday(),
                            DateUtils.STANDARD_DATE_FORMAT, DateUtils.INVERSE_DATE_FORMAT);
                    Date date1 = DateUtils.convertStringToDate(person.getDeathday(), DateUtils.STANDARD_DATE_FORMAT);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(date1);
                    int deathYear = calendar1.get(Calendar.YEAR);
                    age.setText(String.format("Died on %s at %s years", death, deathYear - birthYear));
                }
            }
        });
        castViewModel.getMovies().observe(this, movieCastsForPeople -> {
            personMovieAdapter.add(movieCastsForPeople);
            if (movieCastsForPeople.size() > 0) moviesLabel.setVisibility(View.VISIBLE);
            else moviesLabel.setVisibility(View.GONE);
        });
        castViewModel.getShows().observe(this, showCastsForPeople -> {
            personShowAdapter.add(showCastsForPeople);
            if (showCastsForPeople.size() > 0) showsLabel.setVisibility(View.VISIBLE);
            else showsLabel.setVisibility(View.GONE);
        });
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
            castViewModel.removeFavorite(castId);
            Utils.notify(coordinatorLayout, name.getText().toString()
                    .concat(" has been deleted from favorite collection."));
        }
        else if (favorite.getTag().equals("0")){
            FavoritePreviewMedia favoritePreviewMedia = new FavoritePreviewMedia();
            favoritePreviewMedia.setResType(Constants.ACTOR);
            favoritePreviewMedia.setResId(castId);
            favoritePreviewMedia.setPoster(path);
            favoritePreviewMedia.setName(name.getText().toString());
            castViewModel.addFavorite(favoritePreviewMedia);
            Utils.notify(coordinatorLayout, name.getText().toString()
                    .concat(" has been added to favorite collection."));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(MovieCastsForPerson movieCastsForPerson) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("id", movieCastsForPerson.getId());
        startActivity(intent);
    }

    @Override
    public void onClick(ShowCastsForPerson movieCastsForPerson) {
        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra("id", movieCastsForPerson.getId());
        startActivity(intent);
    }
}
