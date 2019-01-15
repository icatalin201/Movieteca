package app.mov.movieteca.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import app.mov.movieteca.R;
import app.mov.movieteca.adapter.PersonMediaAdapter;
import app.mov.movieteca.database.AppDatabaseHelper;
import app.mov.movieteca.model.BaseMediaForPerson;
import app.mov.movieteca.model.FavoritePreviewMedia;
import app.mov.movieteca.model.FavoritePreviewMediaDao;
import app.mov.movieteca.model.MovieCastsDetails;
import app.mov.movieteca.model.MovieCastsForPerson;
import app.mov.movieteca.model.Person;
import app.mov.movieteca.model.ShowCastsDetails;
import app.mov.movieteca.model.ShowCastsForPerson;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.LoadHelper;
import app.mov.movieteca.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastActivity extends AppCompatActivity implements LoadHelper {

    private ProgressBar progressBar;
    private ScrollView layout;

    private TextView name;
    private TextView age;
    private TextView birthplace;
    private TextView bio;
    private TextView gender;
    private ImageView image;

    private PersonMediaAdapter moviesAdapter;
    private PersonMediaAdapter showsAdapter;

    private TextView moviesLabel;
    private TextView showsLabel;

    private int castId;
    private int count;

    private String path;
    private ImageButton favorite;

    private CoordinatorLayout coordinatorLayout;
    private FavoritePreviewMediaDao favoritePreviewMediaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        count = 0;
        this.favoritePreviewMediaDao = AppDatabaseHelper.getDatabase(this).getDao();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        castId = getIntent().getIntExtra(Util.Constants.CAST_ID, 0);
        progressBar = findViewById(R.id.progressBar);
        layout = findViewById(R.id.cast_layout);
        favorite = findViewById(R.id.favorite);
        name = findViewById(R.id.name);
        coordinatorLayout = findViewById(R.id.coordinator);
        moviesLabel = findViewById(R.id.movies_label);
        showsLabel = findViewById(R.id.shows_label);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        image = findViewById(R.id.image);
        birthplace = findViewById(R.id.birthplace);
        bio = findViewById(R.id.bio);
        RecyclerView movieRecycler = findViewById(R.id.movie_role_recycler);
        RecyclerView showRecycler = findViewById(R.id.show_role_recycler);
        movieRecycler.setHasFixedSize(true);
        showRecycler.setHasFixedSize(true);
        movieRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(this, R.anim.layout_animation_down));
        showRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(this, R.anim.layout_animation_down));
        movieRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        showRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        moviesAdapter = new PersonMediaAdapter(this, new ArrayList<BaseMediaForPerson>());
        showsAdapter = new PersonMediaAdapter(this, new ArrayList<BaseMediaForPerson>());
        movieRecycler.setAdapter(moviesAdapter);
        showRecycler.setAdapter(showsAdapter);
        new LoadData(this).loadData();
        new IsFavorite().execute();
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favorite.getTag().equals("1")){
                    new Thread(new Runnable() {
                        public void run() {
                            favoritePreviewMediaDao.removeFavoriteByQuery(castId, Util.Constants.ACTOR);
                        }
                    }).start();
                    Util.notify(coordinatorLayout, name.getText().toString()
                            .concat(" has been deleted from favorite collection."));
                    favorite.setTag("0");
                    favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
                }
                else if (favorite.getTag().equals("0")){
                    final FavoritePreviewMedia favoritePreviewMedia = new FavoritePreviewMedia();
                    favoritePreviewMedia.setResType(Util.Constants.ACTOR);
                    favoritePreviewMedia.setResId(castId);
                    favoritePreviewMedia.setPoster(path);
                    favoritePreviewMedia.setName(name.getText().toString());
                    new Thread(new Runnable() {
                        public void run() {
                            favoritePreviewMediaDao.insertFavorite(favoritePreviewMedia);                        }
                    }).start();
                    Util.notify(coordinatorLayout, name.getText().toString()
                            .concat(" has been added to favorite collection."));
                    favorite.setTag("1");
                    favorite.setImageResource(R.drawable.ic_baseline_favorite_24px);
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class IsFavorite extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return favoritePreviewMediaDao.isFavorite(castId, Util.Constants.ACTOR) != null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_24px);
                favorite.setTag("1");
            }
            else {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
                favorite.setTag("0");
            }
        }
    }

    @Override
    public void onLoadComplete(boolean status) {
        count++;
        if (count == 3) {
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    private class LoadData {

        private LoadHelper loadHelper;

        LoadData(LoadHelper loadHelper) {
            this.loadHelper = loadHelper;
        }

        void loadData() {
            NetworkService service = NetworkClient.getClient().create(NetworkService.class);
            Call<Person> personCall = service.getPersonDetails(castId, Util.Constants.API_KEY);
            personCall.enqueue(new Callback<Person>() {
                @Override
                public void onResponse(Call<Person> call, Response<Person> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Person person = response.body();
                        setTitle(person.getName());
                        bio.setText(person.getBiography());
                        birthplace.setText(String.format("Birthplace: %s", person.getPlace_of_birth()));
                        String g = person.getGender() == 1 ? "Female" : "Male";
                        gender.setText(String.format("Gender: %s", g));
                        name.setText(person.getName());
                        if (person.getProfile_path() != null) {
                            path = person.getProfile_path();
                            Glide.with(CastActivity.this)
                                    .load(Util.Constants.IMAGE_LOADING_BASE_URL_780
                                            .concat(person.getProfile_path()))
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
                            Date date = Util.convertStringToDate(person.getBirthday(), Util.DateFormats.INVERSE_FORMAT);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            int birthYear = calendar.get(Calendar.YEAR);
                            if (person.getDeathday() == null) {
                                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                                age.setText(String.format("Age: %s years", currentYear - birthYear));
                            } else {
                                String death = Util.convertDateFromFormatToFormat(person.getDeathday(),
                                        Util.DateFormats.INVERSE_FORMAT, Util.DateFormats.STANDARD_FORMAT);
                                Date date1 = Util.convertStringToDate(person.getDeathday(), Util.DateFormats.INVERSE_FORMAT);
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.setTime(date1);
                                int deathYear = calendar1.get(Calendar.YEAR);
                                age.setText(String.format("Died on %s at %s years", death, deathYear - birthYear));
                            }
                        }
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<Person> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<MovieCastsDetails> castMovies = service.getMovieCastsOfPerson(castId, Util.Constants.API_KEY);
            castMovies.enqueue(new Callback<MovieCastsDetails>() {
                @Override
                public void onResponse(Call<MovieCastsDetails> call, Response<MovieCastsDetails> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        MovieCastsDetails movieCastsDetails = response.body();
                        List<MovieCastsForPerson> movieCastsForPersonList = movieCastsDetails.getCast();
                        if (movieCastsForPersonList.size() > 0) moviesLabel.setVisibility(View.VISIBLE);
                        for (MovieCastsForPerson movieCastsForPerson : movieCastsForPersonList) {
                            movieCastsForPerson.setType(Util.Constants.MOVIE);
                        }
                        moviesAdapter.add(movieCastsForPersonList);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<MovieCastsDetails> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<ShowCastsDetails> castShows = service.getTVCastsOfPerson(castId, Util.Constants.API_KEY);
            castShows.enqueue(new Callback<ShowCastsDetails>() {
                @Override
                public void onResponse(Call<ShowCastsDetails> call, Response<ShowCastsDetails> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ShowCastsDetails movieCastsDetails = response.body();
                        List<ShowCastsForPerson> movieCastsForPersonList = movieCastsDetails.getCast();
                        if (movieCastsForPersonList.size() > 0) showsLabel.setVisibility(View.VISIBLE);
                        for (ShowCastsForPerson movieCastsForPerson : movieCastsForPersonList) {
                            movieCastsForPerson.setType(Util.Constants.TV_SHOW);
                        }
                        showsAdapter.add(movieCastsForPersonList);
                    }
                    loadHelper.onLoadComplete(true);
                }

                @Override
                public void onFailure(Call<ShowCastsDetails> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
        }

    }

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
}
