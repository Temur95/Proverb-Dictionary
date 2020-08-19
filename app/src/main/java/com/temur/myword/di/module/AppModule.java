package com.temur.myword.di.module;

import android.app.Application;
import android.content.Context;

import com.temur.myword.App;
import com.temur.myword.di.ApplicationContext;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {

    @Provides
    @ApplicationContext
    static Context provideAppContext(Application application) {
        return application.getApplicationContext();
    }

    @Binds
    @Singleton
    /*
     * Singleton annotation isn't necessary since Application instance is unique but is here for
     * convention. In general, providing Activity, Fragment, BroadcastReceiver, etc does not require
     * them to be scoped since they are the components being injected and their instance is unique.
     *
     * However, having a scope annotation makes the module easier to read. We wouldn't have to look
     * at what is being provided in order to understand its scope.
     */
    abstract Application application(App app);

}
