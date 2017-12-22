package app.mov.movieteca.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.MovieFullDetailCastsAdapter;
import app.mov.movieteca.adapters.MovieFullDetailVideoAdapter;
import app.mov.movieteca.adapters.MovieSimilarAdapter;
import app.mov.movieteca.models.movies.Genres;
import app.mov.movieteca.models.movies.Movie;
import app.mov.movieteca.models.movies.MovieCast;
import app.mov.movieteca.models.movies.MovieCredits;
import app.mov.movieteca.models.movies.MovieShort;
import app.mov.movieteca.models.movies.SimilarMovies;
import app.mov.movieteca.models.movies.VideoInfo;
import app.mov.movieteca.models.movies.Videos;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.Constants;
import app.mov.movieteca.utils.Helper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Catalin on 12/21/2017.
 */

public class FullDetailMovie extends Fragment  {

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
    private TextView movieRevenue;
    private TextView movieTagline;

    private MovieFullDetailVideoAdapter videoAdapter;
    private List<VideoInfo> videosList;
    private RecyclerView trailerRecycler;

    private MovieFullDetailCastsAdapter castsAdapter;
    private List<MovieCast> castList;
    private RecyclerView castRecycler;

    private MovieSimilarAdapter similarAdapter;
    private List<MovieShort> similarMoviesList;
    private RecyclerView similarRecycle;

    private List<Boolean> isJobDone;

    private Call<SimilarMovies> similarMoviesCall;
    private Call<Videos> videosCall;
    private Call<Movie> movieCall;
    private Call<MovieCredits> movieCreditsCall;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_movie_details_fragment, container, false);
        isJobDone = new ArrayList<>(3);
        movieTitle = (TextView)view.findViewById(R.id.movie_title);
        movieGenre = (TextView)view.findViewById(R.id.movie_genre);
        movieYear = (TextView)view.findViewById(R.id.movie_release_date);
        backdrop = (ImageView)view.findViewById(R.id.image_backdrop);
        poster = (ImageView)view.findViewById(R.id.image_poster);
        movieRating = (TextView)view.findViewById(R.id.movie_rating);
        movieOverview = (TextView)view.findViewById(R.id.movie_overview);
        movieBudget = (TextView)view.findViewById(R.id.movie_budget);
        movieRevenue = (TextView)view.findViewById(R.id.movie_revenue);
        movieRuntime = (TextView)view.findViewById(R.id.movie_runtime);
        movieTagline = (TextView)view.findViewById(R.id.movie_tagline);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("movies", Context.MODE_PRIVATE);
        movieId = sharedPreferences.getInt(Constants.movie_id, 0);

        videosList = new ArrayList<>();
        trailerRecycler = (RecyclerView)view.findViewById(R.id.recycler_trailers);
        videoAdapter = new MovieFullDetailVideoAdapter(getContext(), videosList);
        trailerRecycler.setAdapter(videoAdapter);
        trailerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        castList = new ArrayList<>();
        castRecycler = (RecyclerView)view.findViewById(R.id.recycler_cast);
        castsAdapter = new MovieFullDetailCastsAdapter(getContext(), castList);
        castRecycler.setAdapter(castsAdapter);
        castRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        similarMoviesList = new ArrayList<>();
        similarRecycle = (RecyclerView)view.findViewById(R.id.recycler_similar);
        similarAdapter = new MovieSimilarAdapter(getContext(), similarMoviesList);
        similarRecycle.setAdapter(similarAdapter);
        similarRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        loadActivity();

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
        Boolean check = false;
        for (Boolean flag : isJobDone){
            if (flag){
                check = true;
            }
        }
        if (check){
            progressBar.setVisibility(View.GONE);

        }
    }

    public void loadActivity(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        movieCall = service.getMovieDetails(movieId, Constants.api_key);
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()){
                    movieCall = call.clone();
                    movieCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;

                new GetImage(backdrop).execute(Constants.IMAGE_LOADING_BASE_URL_1000 + response.body().getBackdrop_path());
                new GetImage(poster).execute(Constants.IMAGE_LOADING_BASE_URL_780 + response.body().getPoster_path());
                movieTitle.setText(response.body().getOriginal_title());
                setGenre(response.body().getGenres());
                if (response.body().getTagline() != null){
                    movieTagline.setText(response.body().getTagline());
                    movieTagline.setVisibility(View.VISIBLE);
                }
                movieRating.setText(Double.toString(response.body().getVote_average()));
                movieOverview.setText(response.body().getOverview());
                setDetails(response.body().getBudget(), response.body().getRuntime(), response.body().getRevenue(), response.body().getRelease_date());
                setTrailers();
                setCasts();
                setSimilarMovies();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });

    }

    public void setSimilarMovies(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        similarMoviesCall = service.getSimilarMovies(movieId, Constants.api_key, 1);
        similarMoviesCall.enqueue(new Callback<SimilarMovies>() {
            @Override
            public void onResponse(Call<SimilarMovies> call, Response<SimilarMovies> response) {
                if (!response.isSuccessful()){
                    similarMoviesCall = call.clone();
                    similarMoviesCall.enqueue(this);
                    return;
                }
                if (response.body() == null) return;
                if (response.body().getResults() == null) return;

                for (MovieShort movie : response.body().getResults()){
                    if (movie != null && movie.getBackdrop_path() != null && movie.getOriginal_title() != null){
                        similarMoviesList.add(movie);
                    }
                }
                isJobDone.add(true);
                checkJobDone();
                similarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SimilarMovies> call, Throwable t) {

            }
        });

    }

    public void setCasts(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        movieCreditsCall = service.getMovieCredits(movieId, Constants.api_key);
        movieCreditsCall.enqueue(new Callback<MovieCredits>() {
            @Override
            public void onResponse(Call<MovieCredits> call, Response<MovieCredits> response) {
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
                isJobDone.add(true);
                checkJobDone();
                castsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieCredits> call, Throwable t) {

            }
        });
    }

    public void setTrailers(){
        NetworkService service = NetworkClient.getClient().create(NetworkService.class);
        videosCall = service.getMovieVideos(movieId, Constants.api_key);
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
                isJobDone.add(true);
                checkJobDone();
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }
        });
    }

    public void setDetails(Integer budget, Integer runtime, Integer revenue, String date){
        if (budget != null && budget != 0){
            movieBudget.setText("Budget: " + budget + " USD");
            movieBudget.setVisibility(View.VISIBLE);
        }
        if (runtime != null && runtime != 0){
            movieRuntime.setText("Runtime: " + runtime + " minutes");
            movieRuntime.setVisibility(View.VISIBLE);
        }
        if (revenue != null && revenue != 0){
            movieRevenue.setText("Revenue: " + revenue + " USD");
            movieRevenue.setVisibility(View.VISIBLE);
        }
        if (date != null){
            movieYear.setText("Release Date: " + Helper.formatDate(date));
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

    private class GetImage extends AsyncTask<String, Void, Bitmap>{

        private ImageView img;

        public GetImage(ImageView img){
            this.img = img;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urldisplay = strings[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            img.setImageBitmap(bitmap);
        }
    }
}