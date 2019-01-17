package app.mov.movieteca.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import app.mov.movieteca.R;
import app.mov.movieteca.activity.FullMediaActivity;
import app.mov.movieteca.activity.PreviewFullListActivity;
import app.mov.movieteca.adapter.PreviewMediaAdapter;
import app.mov.movieteca.model.BaseMovieResponse;
import app.mov.movieteca.model.BaseTVShowResponse;
import app.mov.movieteca.model.PreviewMedia;
import app.mov.movieteca.model.PreviewMovie;
import app.mov.movieteca.model.PreviewTVShow;
import app.mov.movieteca.retronetwork.NetworkClient;
import app.mov.movieteca.retronetwork.NetworkService;
import app.mov.movieteca.utils.LoadHelper;
import app.mov.movieteca.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements LoadHelper {

    private ProgressBar progressBar;
    private NestedScrollView containerView;

    private String type;

    private PreviewMediaAdapter nowPlayingAdapter;
    private PreviewMediaAdapter popularAdapter;
    private PreviewMediaAdapter upcomingAdapter;
    private PreviewMediaAdapter topRatedAdapter;

    private ImageView mainImage;
    private TextView mainTitle;
    private int mainId;

    private int count;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("type", type);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(activity).setSupportActionBar(toolbar);
        count = 0;
        progressBar = view.findViewById(R.id.progressBar);
        containerView = view.findViewById(R.id.container);
        mainImage = view.findViewById(R.id.main_image);
        mainTitle = view.findViewById(R.id.main_title);
        view.findViewById(R.id.main_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FullMediaActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("id", mainId);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.popular_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PreviewFullListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("internal_type", getString(R.string.popular_movies));
                intent.putExtra("extra", "popular");
                startActivity(intent);
            }
        });
        view.findViewById(R.id.upcoming_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PreviewFullListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("internal_type",  getString(R.string.upcoming_movies));
                intent.putExtra("extra", "upcoming");
                startActivity(intent);
            }
        });
        view.findViewById(R.id.top_rated_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PreviewFullListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("internal_type",  getString(R.string.top_rated_movies));
                intent.putExtra("extra", "top_rated");
                startActivity(intent);
            }
        });
        view.findViewById(R.id.now_playing_view_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PreviewFullListActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("internal_type",  getString(R.string.now_in_theaters_movies));
                intent.putExtra("extra", "now_playing");
                startActivity(intent);
            }
        });
        TextView selectMovies = view.findViewById(R.id.movies);
        TextView selectTv = view.findViewById(R.id.tv_shows);
        if (savedInstanceState != null) {
            type = savedInstanceState.getString("type");
        } else {
            type = Util.Constants.MOVIE;
        }
        selectMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = Util.Constants.MOVIE;
                activity.recreate();
            }
        });
        selectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = Util.Constants.TV_SHOW;
                activity.recreate();
            }
        });
        popularAdapter = new PreviewMediaAdapter(activity, new ArrayList<PreviewMedia>(), false);
        nowPlayingAdapter = new PreviewMediaAdapter(activity, new ArrayList<PreviewMedia>(), false);
        upcomingAdapter = new PreviewMediaAdapter(activity, new ArrayList<PreviewMedia>(), false);
        topRatedAdapter = new PreviewMediaAdapter(activity, new ArrayList<PreviewMedia>(), false);
        RecyclerView nowPlayingRecycler = view.findViewById(R.id.recycler_now_showing);
        RecyclerView popularRecycler = view.findViewById(R.id.recycler_popular);
        RecyclerView topRatedRecycler = view.findViewById(R.id.recycler_top_rated);
        RecyclerView upcomingRecycler = view.findViewById(R.id.recycler_upcoming);
        nowPlayingRecycler.setAdapter(nowPlayingAdapter);
        popularRecycler.setAdapter(popularAdapter);
        topRatedRecycler.setAdapter(topRatedAdapter);
        upcomingRecycler.setAdapter(upcomingAdapter);
        nowPlayingRecycler.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false));
        popularRecycler.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false));
        topRatedRecycler.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false));
        upcomingRecycler.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false));
        nowPlayingRecycler.setHasFixedSize(true);
        popularRecycler.setHasFixedSize(true);
        topRatedRecycler.setHasFixedSize(true);
        upcomingRecycler.setHasFixedSize(true);
        nowPlayingRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(activity, R.anim.layout_animation_down));
        popularRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(activity, R.anim.layout_animation_down));
        topRatedRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(activity, R.anim.layout_animation_down));
        upcomingRecycler.setLayoutAnimation(AnimationUtils
                .loadLayoutAnimation(activity, R.anim.layout_animation_down));
        new LoadData(this, type).startLoad();
        return view;
    }

    @Override
    public void onLoadComplete(boolean status) {
        count++;
        if (count == 4) {
            progressBar.setVisibility(View.GONE);
            containerView.setVisibility(View.VISIBLE);
        }
    }

    private class LoadData {

        private LoadHelper loadHelper;
        private String type;

        LoadData(LoadHelper loadHelper, String type) {
            this.loadHelper = loadHelper;
            this.type = type;
        }

        private void startLoad() {
            if (type.equals(Util.Constants.MOVIE)) {
                loadMovies();
            } else {
                loadTV();
            }
        }

        private void loadMovies() {
            NetworkService service = NetworkClient.getClient().create(NetworkService.class);
            Call<BaseMovieResponse> nowPlayingCall = service
                    .getNowShowingMovies(Util.Constants.API_KEY, 1, "US");
            nowPlayingCall.enqueue(new Callback<BaseMovieResponse>() {
                @Override
                public void onResponse(Call<BaseMovieResponse> call,
                                       Response<BaseMovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewMovie> previewMovieList = response.body().getResults();
                        List<PreviewMovie> previewMovieList1 = new ArrayList<>();
                        for (PreviewMovie previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewMovieList1.add(previewMovie);
                        }
                        nowPlayingAdapter.add(previewMovieList1);
                        loadHelper.onLoadComplete(true);
                    } else {
                        loadHelper.onLoadComplete(false);
                    }
                }

                @Override
                public void onFailure(Call<BaseMovieResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<BaseMovieResponse> popularCall = service
                    .getPopularMovies(Util.Constants.API_KEY, 1, "US");
            popularCall.enqueue(new Callback<BaseMovieResponse>() {
                @Override
                public void onResponse(Call<BaseMovieResponse> call,
                                       Response<BaseMovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewMovie> previewMovieList = response.body().getResults();
                        List<PreviewMovie> previewMovieList1 = new ArrayList<>();
                        for (PreviewMovie previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewMovieList1.add(previewMovie);
                        }
                        PreviewMovie previewMovie = getRandomMovie(previewMovieList1);
                        Glide.with(mainImage)
                                .load(Util.Constants.IMAGE_LOADING_BASE_URL_1000
                                        .concat(previewMovie.getBackdropPath()))
                                .apply(RequestOptions.centerCropTransform())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(mainImage);
                        mainTitle.setText(previewMovie.getTitle());
                        mainId = previewMovie.getId();
                        popularAdapter.add(previewMovieList1);
                        loadHelper.onLoadComplete(true);
                    } else {
                        loadHelper.onLoadComplete(false);
                    }
                }

                @Override
                public void onFailure(Call<BaseMovieResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<BaseMovieResponse> topRatedCall = service
                    .getTopRatedMovies(Util.Constants.API_KEY, 1, "US");
            topRatedCall.enqueue(new Callback<BaseMovieResponse>() {
                @Override
                public void onResponse(Call<BaseMovieResponse> call,
                                       Response<BaseMovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewMovie> previewMovieList = response.body().getResults();
                        List<PreviewMovie> previewMovieList1 = new ArrayList<>();
                        for (PreviewMovie previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewMovieList1.add(previewMovie);
                        }
                        topRatedAdapter.add(previewMovieList1);
                        loadHelper.onLoadComplete(true);
                    } else {
                        loadHelper.onLoadComplete(false);
                    }
                }

                @Override
                public void onFailure(Call<BaseMovieResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<BaseMovieResponse> upcomingCall = service
                    .getUpcomingMovies(Util.Constants.API_KEY, 1, "US");
            upcomingCall.enqueue(new Callback<BaseMovieResponse>() {
                @Override
                public void onResponse(Call<BaseMovieResponse> call,
                                       Response<BaseMovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewMovie> previewMovieList = response.body().getResults();
                        List<PreviewMovie> previewMovieList1 = new ArrayList<>();
                        for (PreviewMovie previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewMovieList1.add(previewMovie);
                        }
                        upcomingAdapter.add(previewMovieList1);
                        loadHelper.onLoadComplete(true);
                    } else {
                        loadHelper.onLoadComplete(false);
                    }
                }

                @Override
                public void onFailure(Call<BaseMovieResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
        }

        private PreviewTVShow getRandomTV(List<PreviewTVShow> previewTVShows) {
            PreviewTVShow previewMovie = previewTVShows.get(new Random().nextInt(previewTVShows.size()));
            if (previewMovie.getBackdropPath() == null) {
               return getRandomTV(previewTVShows);
            }
            return previewMovie;
        }

        private PreviewMovie getRandomMovie(List<PreviewMovie> previewMovies) {
            PreviewMovie previewMovie = previewMovies.get(new Random().nextInt(previewMovies.size()));
            if (previewMovie.getBackdropPath() == null) {
                return getRandomMovie(previewMovies);
            }
            return previewMovie;
        }

        private void loadTV() {
            NetworkService service = NetworkClient.getClient().create(NetworkService.class);
            Call<BaseTVShowResponse> nowPlayingCall = service
                    .getAiringTodayTVShows(Util.Constants.API_KEY, 1);
            nowPlayingCall.enqueue(new Callback<BaseTVShowResponse>() {
                @Override
                public void onResponse(Call<BaseTVShowResponse> call,
                                       Response<BaseTVShowResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewTVShow> previewMovieList = response.body().getResults();
                        List<PreviewTVShow> previewTVShows = new ArrayList<>();
                        for (PreviewTVShow previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewTVShows.add(previewMovie);
                        }
                        nowPlayingAdapter.add(previewTVShows);
                        loadHelper.onLoadComplete(true);
                    } else {
                        loadHelper.onLoadComplete(false);
                    }
                }

                @Override
                public void onFailure(Call<BaseTVShowResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<BaseTVShowResponse> popularCall = service
                    .getPopularTVShows(Util.Constants.API_KEY, 1);
            popularCall.enqueue(new Callback<BaseTVShowResponse>() {
                @Override
                public void onResponse(Call<BaseTVShowResponse> call,
                                       Response<BaseTVShowResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewTVShow> previewMovieList = response.body().getResults();
                        List<PreviewTVShow> previewTVShows = new ArrayList<>();
                        for (PreviewTVShow previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewTVShows.add(previewMovie);
                        }
                        PreviewTVShow previewMovie = getRandomTV(previewTVShows);
                        Glide.with(mainImage)
                                .load(Util.Constants.IMAGE_LOADING_BASE_URL_1000
                                        .concat(previewMovie.getBackdropPath()))
                                .apply(RequestOptions.centerCropTransform())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(mainImage);
                        mainTitle.setText(previewMovie.getOriginalName());
                        mainId = previewMovie.getId();
                        popularAdapter.add(previewTVShows);
                        loadHelper.onLoadComplete(true);
                    } else {
                        loadHelper.onLoadComplete(false);
                    }
                }

                @Override
                public void onFailure(Call<BaseTVShowResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<BaseTVShowResponse> upcomingCall = service
                    .getPopularTVShows(Util.Constants.API_KEY, 1);
            upcomingCall.enqueue(new Callback<BaseTVShowResponse>() {
                @Override
                public void onResponse(Call<BaseTVShowResponse> call,
                                       Response<BaseTVShowResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewTVShow> previewMovieList = response.body().getResults();
                        List<PreviewTVShow> previewTVShows = new ArrayList<>();
                        for (PreviewTVShow previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewTVShows.add(previewMovie);
                        }
                        upcomingAdapter.add(previewTVShows);
                        loadHelper.onLoadComplete(true);
                    } else {
                        loadHelper.onLoadComplete(false);
                    }
                }

                @Override
                public void onFailure(Call<BaseTVShowResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
            Call<BaseTVShowResponse> topRatedCall = service
                    .getPopularTVShows(Util.Constants.API_KEY, 1);
            topRatedCall.enqueue(new Callback<BaseTVShowResponse>() {
                @Override
                public void onResponse(Call<BaseTVShowResponse> call,
                                       Response<BaseTVShowResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PreviewTVShow> previewMovieList = response.body().getResults();
                        List<PreviewTVShow> previewTVShows = new ArrayList<>();
                        for (PreviewTVShow previewMovie : previewMovieList) {
                            if (previewMovie == null || previewMovie.getPosterPath() == null) {
                                continue;
                            }
                            previewMovie.setType(type);
                            previewTVShows.add(previewMovie);
                        }
                        topRatedAdapter.add(previewTVShows);
                        loadHelper.onLoadComplete(true);
                    } else {
                        loadHelper.onLoadComplete(false);
                    }
                }

                @Override
                public void onFailure(Call<BaseTVShowResponse> call, Throwable t) {
                    loadHelper.onLoadComplete(false);
                }
            });
        }

    }

}
