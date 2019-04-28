package app.mov.movieteca.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import javax.inject.Singleton;

import app.mov.movieteca.api.CastService;
import app.mov.movieteca.api.GenreService;
import app.mov.movieteca.api.MovieService;
import app.mov.movieteca.api.TvSeriesService;
import app.mov.movieteca.api.UserService;
import app.mov.movieteca.repository.CastRepository;
import app.mov.movieteca.repository.GenreRepository;
import app.mov.movieteca.repository.MovieRepository;
import app.mov.movieteca.repository.ShowRepository;
import app.mov.movieteca.repository.UserRepository;
import app.mov.movieteca.util.RequestInterceptor;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class ApiServiceModule {

    @Provides
    @Singleton
    public UserRepository getUserRepository(UserService userService) {
        return new UserRepository(userService);
    }

    @Provides
    @Singleton
    public CastRepository getCastRepository(CastService castService) {
        return new CastRepository(castService);
    }

    @Provides
    @Singleton
    public GenreRepository getGenreRepository(GenreService genreService) {
        return new GenreRepository(genreService);
    }

    @Provides
    @Singleton
    public MovieRepository getMovieRepository(MovieService movieService) {
        return new MovieRepository(movieService);
    }

    @Provides
    @Singleton
    public ShowRepository getShowRepository(TvSeriesService tvSeriesService) {
        return new ShowRepository(tvSeriesService);
    }

    @Provides
    @Singleton
    public UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Provides
    @Singleton
    public CastService provideCastService(Retrofit retrofit) {
        return retrofit.create(CastService.class);
    }

    @Provides
    @Singleton
    public GenreService provideGenreService(Retrofit retrofit) {
        return retrofit.create(GenreService.class);
    }

    @Provides
    @Singleton
    public MovieService provideMovieService(Retrofit retrofit) {
        return retrofit.create(MovieService.class);
    }

    @Provides
    @Singleton
    public TvSeriesService provideTvSeriesService(Retrofit retrofit) {
        return retrofit.create(TvSeriesService.class);
    }

    @Provides
    @Singleton
    Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Retrofit getRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    Cache getCache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1000);
    }

    @Provides
    File getFile(Context context) {
        File file = new File(context.getFilesDir(), "cache_dir");
        if (!file.exists()) file.mkdirs();
        return file;
    }

    @Provides
    @Singleton
    RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    OkHttpClient getOkHttpClient(Cache cache, HttpLoggingInterceptor requestInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .cache(cache)
                .build();
    }

}
