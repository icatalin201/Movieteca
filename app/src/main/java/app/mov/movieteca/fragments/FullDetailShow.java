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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.MovieFullDetailVideoAdapter;
import app.mov.movieteca.adapters.TVShowCastAdapter;
import app.mov.movieteca.adapters.TVShowSimilarAdapter;
import app.mov.movieteca.database.Handler;
import app.mov.movieteca.models.movies.Genres;
import app.mov.movieteca.models.movies.MovieCast;
import app.mov.movieteca.models.movies.VideoInfo;
import app.mov.movieteca.models.movies.Videos;
import app.mov.movieteca.models.tvshows.SimilarTVShows;
import app.mov.movieteca.models.tvshows.TVShow;
import app.mov.movieteca.models.tvshows.TVShowCredits;
import app.mov.movieteca.models.tvshows.TVShowShort;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Catalin on 12/24/2017.
 */

public class FullDetailShow extends Fragment {

    private int movieId;
    private TextView movieTitle;
    private TextView movieYear;
    private TextView movieGenre;
    private ImageView backdrop;
    private ImageView poster;
    private ProgressBar progressBar;
    private TextView movieRating;
    private TextView movieOverview;
    private TextView movieRuntime;
    private TextView movieBudget;
    private TextView movieTagline;
    private TextView similar;
    private ImageButton fav;
    private ImageButton seen;

    private ScrollView layout;

    private MovieFullDetailVideoAdapter videoAdapter;
    private List<VideoInfo> videosList;
    private RecyclerView trailerRecycler;

    private TVShowCastAdapter castsAdapter;
    private List<MovieCast> castList;
    private RecyclerView castRecycler;

    private TVShowSimilarAdapter similarAdapter;
    private List<TVShowShort> similarMoviesList;
    private RecyclerView similarRecycle;

    private boolean[] isJobDone;

    private Call<SimilarTVShows> similarMoviesCall;
    private Call<Videos> videosCall;
    private Call<TVShow> movieCall;
    private Call<TVShowCredits> movieCreditsCall;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_show_detail_fragment, container, false);
        isJobDone = new boolean[]{false, false, false, false};
        fav = (ImageButton)view.findViewById(R.id.image_button_fav_movie_detail);
        seen = (ImageButton)view.findViewById(R.id.seen_button);
        movieTitle = (TextView)view.findViewById(R.id.movie_title);
        movieGenre = (TextView)view.findViewById(R.id.movie_genre);
        movieYear = (TextView)view.findViewById(R.id.movie_release_date);
        backdrop = (ImageView)view.findViewById(R.id.image_backdrop);
        poster = (ImageView)view.findViewById(R.id.image_poster);
        movieRating = (TextView)view.findViewById(R.id.movie_rating);
        movieOverview = (TextView)view.findViewById(R.id.movie_overview);
        movieBudget = (TextView)view.findViewById(R.id.movie_budget);
        movieRuntime = (TextView)view.findViewById(R.id.movie_runtime);
        movieTagline = (TextView)view.findViewById(R.id.movie_tagline);
        similar = (TextView)view.findViewById(R.id.movie_similar);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        layout = (ScrollView)view.findViewById(R.id.entire_layout);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("tv_shows", Context.MODE_PRIVATE);
        movieId = sharedPreferences.getInt(Constants.tv_show_id, 0);

        videosList = new ArrayList<>();
        trailerRecycler = (RecyclerView)view.findViewById(R.id.recycler_trailers);
        videoAdapter = new MovieFullDetailVideoAdapter(getContext(), videosList);
        trailerRecycler.setAdapter(videoAdapter);
        trailerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        castList = new ArrayList<>();
        castRecycler = (RecyclerView)view.findViewById(R.id.recycler_cast);
        castsAdapter = new TVShowCastAdapter(getContext(), castList);
        castRecycler.setAdapter(castsAdapter);
        castRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        similarMoviesList = new ArrayList<>();
        similarRecycle = (RecyclerView)view.findViewById(R.id.recycler_similar);
        similarAdapter = new TVShowSimilarAdapter(getContext(), similarMoviesList);
        similarRecycle.setAdapter(similarAdapter);
        similarRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        loadData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        videoAdapter.notifyDataSetChanged();
        castsAdapter.notifyDataSetChanged();
        similarAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (movieCall != null) movieCall.cancel();
        if (videosCall != null) videosCall.cancel();
        if (movieCreditsCall != null) movieCreditsCall.cancel();
        if (similarMoviesCall != null) similarMoviesCall.cancel();
    }

    private void checkJobDone(){
        boolean check = false;
        for (boolean flag : isJobDone){
            if (flag){
                check = true;
            }
            else {
                check = false;
            }
        }
        if (check){
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void loadData(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        movieCall = service.getTVShowDetails(movieId, Constants.api_key);
        movieCall.enqueue(new Callback<TVShow>() {
            @Override
            public void onResponse(Call<TVShow> call, Response<TVShow> response) {
                if (!response.isSuccessful()){
                    movieCall = call.clone();
                    movieCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                Glide.with(getContext()).load(Constants.IMAGE_LOADING_BASE_URL_780.concat(response.body().getBackdrop_path()))
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(backdrop);
                Glide.with(getContext()).load(Constants.IMAGE_LOADING_BASE_URL_342.concat(response.body().getPoster_path()))
                        .asBitmap()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(poster);
                movieTitle.setText(response.body().getOriginal_name());
                setGenre(response.body().getGenres());
                if (response.body().getType() != null){
                    movieTagline.setText("Type: " + response.body().getType());
                    movieTagline.setVisibility(View.VISIBLE);
                }
                movieRating.setText(Double.toString(response.body().getVote_average()));
                movieOverview.setText(response.body().getOverview());
                isJobDone[0] = true;
                setDetails(response.body().getNumber_of_episodes(), response.body().getNumber_of_seasons(), response.body().getFirst_air_date());
                setFavorite(getContext(), response.body().getId(), response.body().getBackdrop_path(), response.body().getOriginal_name());
                setSeen(getContext(), response.body().getId(), response.body().getBackdrop_path(), response.body().getOriginal_name());
                setTrailers();
                setCasts();
                setSimilarMovies();
            }

            @Override
            public void onFailure(Call<TVShow> call, Throwable t) {

            }
        });

    }

    public void setSeen(final Context context, final Integer id, final String path, final String title){
        if (Handler.isSeen(context, "tv_show", id)){
            seen.setTag("seen");
            seen.setImageResource(R.drawable.seen);
        }
        else {
            seen.setTag("unseen");
            seen.setImageResource(R.drawable.notseen);
        }
        seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if (seen.getTag().equals("seen")){
                    Handler.removeFromSeen(context, "tv_show", id);
                    Helper.notifyUser("remove", "seen", title, context);
                    seen.setTag("unseen");
                    seen.setImageResource(R.drawable.notseen);
                }
                else if (seen.getTag().equals("unseen")){
                    Handler.addToSeen(context, "tv_show", id, title, path);
                    Helper.notifyUser("add", "seen", title, context);
                    seen.setTag("seen");
                    seen.setImageResource(R.drawable.seen);
                }
            }
        });
    }

    public void setFavorite(final Context context, final Integer id, final String path, final String title){
        if (Handler.isFav(context, "tv_show", id)){
            fav.setImageResource(R.drawable.fav_big);
            fav.setTag("favorit");
        }
        else {
            fav.setImageResource(R.drawable.notfav_big);
            fav.setTag("nefavorit");
        }
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if (fav.getTag().equals("favorit")){
                    Handler.removeFromFavorites(context, "tv_show", id);
                    Helper.notifyUser("remove", "fav", title, context);
                    fav.setTag("nefavorit");
                    fav.setImageResource(R.drawable.notfav_big);
                }
                else if (fav.getTag().equals("nefavorit")){
                    Handler.addToFavorites(context, "tv_show", id, title, path);
                    Helper.notifyUser("add", "fav", title, context);
                    fav.setTag("favorit");
                    fav.setImageResource(R.drawable.fav_big);
                }
            }
        });
    }

    public void setSimilarMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        similarMoviesCall = service.getSimilarTVShows(movieId, Constants.api_key, 1);
        similarMoviesCall.enqueue(new Callback<SimilarTVShows>() {
            @Override
            public void onResponse(Call<SimilarTVShows> call, Response<SimilarTVShows> response) {
                if (!response.isSuccessful()){
                    similarMoviesCall = call.clone();
                    similarMoviesCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getResults() == null) return;
                for (TVShowShort movie : response.body().getResults()){
                    if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_name() != null){
                        similarMoviesList.add(movie);
                        similar.setVisibility(View.VISIBLE);
                    }
                }
                isJobDone[1] = true;
                similarAdapter.notifyDataSetChanged();
                checkJobDone();
            }

            @Override
            public void onFailure(Call<SimilarTVShows> call, Throwable t) {

            }
        });

    }

    public void setCasts(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        movieCreditsCall = service.getTVShowCredits(movieId, Constants.api_key);
        movieCreditsCall.enqueue(new Callback<TVShowCredits>() {
            @Override
            public void onResponse(Call<TVShowCredits> call, Response<TVShowCredits> response) {
                if (!response.isSuccessful()){
                    movieCreditsCall = call.clone();
                    movieCreditsCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getCast() == null) return;

                for (MovieCast cast : response.body().getCast()){
                    if (cast != null && cast.getCharacter() != null && cast.getName() != null && cast.getProfile_path() != null){
                        castList.add(cast);
                    }
                }
                isJobDone[2] = true;
                castsAdapter.notifyDataSetChanged();
                checkJobDone();
            }

            @Override
            public void onFailure(Call<TVShowCredits> call, Throwable t) {

            }
        });
    }

    public void setTrailers(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        videosCall = service.getTVShowVideos(movieId, Constants.api_key);
        videosCall.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {
                if (!response.isSuccessful()){
                    videosCall = call.clone();
                    videosCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                for (VideoInfo video : response.body().getResults()){
                    if (video != null && video.getSite() != null && video.getSite().equals("YouTube") && video.getType() != null && video.getType().equals("Trailer")) {
                        videosList.add(video);
                    }
                }
                isJobDone[3] = true;
                videoAdapter.notifyDataSetChanged();
                checkJobDone();
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }
        });
    }

    public void setDetails(Integer episodes, Integer seasons, String date){
        if (episodes != null && episodes != 0){
            movieBudget.setText("Number of Episodes: " + episodes);
            movieBudget.setVisibility(View.VISIBLE);
        }
        if (seasons != null && seasons != 0){
            movieRuntime.setText("Number of Seasons: " + seasons);
            movieRuntime.setVisibility(View.VISIBLE);
        }
        if (date != null){
            movieYear.setText("First Air Date: " + Helper.formatDate(date));
        }
    }

    public void setGenre(List<Genres> genresList){
        String genres = "";
        if (genresList != null) {
            for (int i = 0; i < genresList.size(); i++) {
                if (genresList.get(i) == null) continue;
                if (i == genresList.size() - 1) {
                    genres = genres.concat(genresList.get(i).getName());
                } else {
                    genres = genres.concat(genresList.get(i).getName() + ", ");
                }
            }
        }
        movieGenre.setText(genres);
    }
}
