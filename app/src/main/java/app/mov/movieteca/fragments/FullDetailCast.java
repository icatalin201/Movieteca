package app.mov.movieteca.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.MovieCastAdapter;
import app.mov.movieteca.adapters.ShowCastAdapter;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.models.cast.MovieCastsDetails;
import app.mov.movieteca.models.cast.MovieCastsForPerson;
import app.mov.movieteca.models.cast.Person;
import app.mov.movieteca.models.cast.ShowCastsDetails;
import app.mov.movieteca.models.cast.ShowCastsForPerson;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Catalin on 12/23/2017.
 */

public class FullDetailCast extends Fragment {

    private ProgressBar progressBar;
    private TextView castName;
    private TextView castAge;
    private TextView biography;
    private TextView birthplace;
    private ImageView poster;
    private ImageButton fav;
    private ScrollView layout;
    private RecyclerView moviesRecycler;
    private RecyclerView showsRecycler;
    private MovieCastAdapter movieAdapter;
    private ShowCastAdapter showAdapter;
    private List<MovieCastsForPerson> movieCastsForPersonList;
    private Call<MovieCastsDetails> castsDetailsCall;
    private List<ShowCastsForPerson> showCastsForPersonList;
    private Call<ShowCastsDetails> showCastsDetailsCall;
    private Call<Person> personCall;
    private int cast_id;
    private boolean[] isJobDone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cast_full_detail, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cast", Context.MODE_PRIVATE);
        cast_id = sharedPreferences.getInt(Constants.cast_id,0);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        progressBar.setIndeterminate(true);
        isJobDone = new boolean[]{false, false};
        castName = (TextView)view.findViewById(R.id.cast_name);
        castName.setSelected(true);
        castAge = (TextView)view.findViewById(R.id.cast_age);
        birthplace = (TextView)view.findViewById(R.id.cast_birthplace);
        biography = (TextView)view.findViewById(R.id.cast_bio);
        poster = (ImageView)view.findViewById(R.id.cast_image);
        layout = (ScrollView)view.findViewById(R.id.cast_layout);
        fav = (ImageButton)view.findViewById(R.id.image_button_fav);
        moviesRecycler = (RecyclerView)view.findViewById(R.id.cast_movie_role_recycler);
        showsRecycler = (RecyclerView)view.findViewById(R.id.cast_show_role_recycler);
        movieCastsForPersonList = new ArrayList<>();
        showCastsForPersonList = new ArrayList<>();
        movieAdapter = new MovieCastAdapter(getContext(), movieCastsForPersonList);
        showAdapter = new ShowCastAdapter(getContext(), showCastsForPersonList);
        moviesRecycler.setAdapter(movieAdapter);
        moviesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        showsRecycler.setAdapter(showAdapter);
        showsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        loadData();
        return view;
    }

    public void checkLoaded(){
        boolean check = false;
        for (boolean flag : isJobDone){
            if (flag){
                check = true;
            }
            else {
                check = false;
            }
        }
        if (check) {
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void loadData(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        personCall = service.getPersonDetails(cast_id, Constants.api_key);
        personCall.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                if (!response.isSuccessful()){
                    personCall = call.clone();
                    personCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                Glide.with(getContext()).load(Constants.IMAGE_LOADING_BASE_URL_780.concat(response.body().getProfile_path()))
                        .asBitmap().centerCrop().into(poster);
                castName.setText(response.body().getName());
                setAgeOrDeath(response.body().getBirthday(), response.body().getDeathday());
                biography.setText(response.body().getBiography());
                birthplace.setText(response.body().getPlace_of_birth());
                setFavorite(getContext(), response.body().getId(), response.body().getProfile_path(), response.body().getName());
                setMovies();
                setShows();
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {

            }
        });

    }

    public void setFavorite(final Context context, final Integer id, final String path, final String title){
        if (Handler.isFav(context, "cast", id)){
            fav.setImageResource(R.drawable.ic_favorite_black_18dp);
            fav.setTag("favorit");
        }
        else {
            fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
            fav.setTag("nefavorit");
        }
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if (fav.getTag().equals("favorit")){
                    Handler.removeFromFavorites(context, "cast", id);
                    Helper.notifyUser("remove", "fav", title, context);
                    fav.setTag("nefavorit");
                    fav.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                }
                else if (fav.getTag().equals("nefavorit")){
                    Handler.addToFavorites(context, "cast", id, title, path);
                    Helper.notifyUser("add", "fav", title, context);
                    fav.setTag("favorit");
                    fav.setImageResource(R.drawable.ic_favorite_black_18dp);
                }
            }
        });
    }

    public void setMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        castsDetailsCall = service.getMovieCastsOfPerson(cast_id, Constants.api_key);
        castsDetailsCall.enqueue(new Callback<MovieCastsDetails>() {
            @Override
            public void onResponse(Call<MovieCastsDetails> call, Response<MovieCastsDetails> response) {
                if (!response.isSuccessful()){
                    castsDetailsCall = call.clone();
                    castsDetailsCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getCast() == null) return;

                for (MovieCastsForPerson movieCast : response.body().getCast()){
                    if (movieCast != null && movieCast.getBackdrop_path() != null && movieCast.getOriginal_title() != null && movieCast.getCharacter() != null) {
                        movieCastsForPersonList.add(movieCast);
                    }
                }
                isJobDone[0] = true;
                movieAdapter.notifyDataSetChanged();
                checkLoaded();
            }

            @Override
            public void onFailure(Call<MovieCastsDetails> call, Throwable t) {

            }
        });
    }

    public void setShows(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        showCastsDetailsCall = service.getTVCastsOfPerson(cast_id, Constants.api_key);
        showCastsDetailsCall.enqueue(new Callback<ShowCastsDetails>() {
            @Override
            public void onResponse(Call<ShowCastsDetails> call, Response<ShowCastsDetails> response) {
                if (!response.isSuccessful()){
                    showCastsDetailsCall = call.clone();
                    showCastsDetailsCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getCast() == null) return;

                for (ShowCastsForPerson showCast : response.body().getCast()){
                    if (showCast != null && showCast.getBackdrop_path() != null && showCast.getOriginal_name() != null && showCast.getCharacter() != null) {
                        showCastsForPersonList.add(showCast);
                    }
                }
                isJobDone[1] = true;
                showAdapter.notifyDataSetChanged();
                checkLoaded();
            }

            @Override
            public void onFailure(Call<ShowCastsDetails> call, Throwable t) {

            }
        });
    }

    public void setAgeOrDeath(String born, String death){
        if (born != null) {
            if (death == null || death.trim().isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int bornYear;
                String age = "";
                Date bornDate = null;
                try {
                    bornDate = format.parse(born);
                    bornYear = Integer.parseInt(yearFormat.format(bornDate));
                    age = Integer.toString(currentYear - bornYear);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                castAge.setText(age + " years");
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                Date borndate;
                Date deathdate;
                String age = "";
                int bornYear;
                int deathYear;
                try {
                    borndate = format.parse(born);
                    deathdate = format.parse(death);
                    bornYear = Integer.parseInt(yearFormat.format(borndate));
                    deathYear = Integer.parseInt(yearFormat.format(deathdate));
                    age = Integer.toString(deathYear - bornYear);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                castAge.setText("Died on " + death + " at " + age + " years.");
            }
        }
    }

}
