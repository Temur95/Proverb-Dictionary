package com.temur.myword;

import android.app.Application;

import com.temur.myword.di.AppComponent;
import com.temur.myword.di.DaggerAppComponent;

public class App extends Application {

    private static final String TAG = App.class.getName();
    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().app(this).build();
        appComponent.inject(this);
    }
}
