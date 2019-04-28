package app.mov.movieteca.di.component;

import javax.inject.Singleton;

import app.mov.movieteca.di.module.ApiServiceModule;
import app.mov.movieteca.di.module.ContextModule;
import app.mov.movieteca.di.module.DatabaseModule;
import app.mov.movieteca.view.viewmodel.CastViewModel;
import app.mov.movieteca.view.viewmodel.FavoriteViewModel;
import app.mov.movieteca.view.viewmodel.FullMovieListViewModel;
import app.mov.movieteca.view.viewmodel.FullShowListViewModel;
import app.mov.movieteca.view.viewmodel.GenreMovieViewModel;
import app.mov.movieteca.view.viewmodel.GenreShowViewModel;
import app.mov.movieteca.view.viewmodel.MainViewModel;
import app.mov.movieteca.view.viewmodel.MovieViewModel;
import app.mov.movieteca.view.viewmodel.MoviesViewModel;
import app.mov.movieteca.view.viewmodel.ProfileViewModel;
import app.mov.movieteca.view.viewmodel.SearchViewModel;
import app.mov.movieteca.view.viewmodel.ShowViewModel;
import app.mov.movieteca.view.viewmodel.ShowsViewModel;
import app.mov.movieteca.view.viewmodel.UserViewModel;
import dagger.Component;

@Singleton
@Component(modules = {
        ApiServiceModule.class,
        ContextModule.class,
        DatabaseModule.class
})
public interface ApplicationComponent {
    void inject(UserViewModel viewModel);
    void inject(MoviesViewModel viewModel);
    void inject(ShowsViewModel viewModel);
    void inject(MovieViewModel viewModel);
    void inject(ShowViewModel viewModel);
    void inject(FullShowListViewModel viewModel);
    void inject(FullMovieListViewModel viewModel);
    void inject(GenreMovieViewModel viewModel);
    void inject(GenreShowViewModel viewModel);
    void inject(FavoriteViewModel viewModel);
    void inject(SearchViewModel viewModel);
    void inject(CastViewModel viewModel);
    void inject(ProfileViewModel viewModel);
    void inject(MainViewModel viewModel);
}
