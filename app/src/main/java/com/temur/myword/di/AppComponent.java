package com.temur.myword.di;

import com.temur.myword.App;
import com.temur.myword.di.module.AppModule;
import com.temur.myword.ui.MainActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(App app);
    void inject(MainActivity mainActivity);



    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder app(App app);

        AppComponent build();
    }
}
