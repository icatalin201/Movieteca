package app.mov.movieteca.application;

import android.app.Application;

import app.mov.movieteca.di.component.ApplicationComponent;
import app.mov.movieteca.di.component.DaggerApplicationComponent;
import app.mov.movieteca.di.module.ApiServiceModule;
import app.mov.movieteca.di.module.ContextModule;
import app.mov.movieteca.util.Shared;

public class Movieteca extends Application {

    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Shared.init(getApplicationContext());
        applicationComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .apiServiceModule(new ApiServiceModule())
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
