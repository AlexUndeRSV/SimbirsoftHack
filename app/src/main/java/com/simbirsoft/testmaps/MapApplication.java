package com.simbirsoft.testmaps;

import android.app.Application;

import com.simbirsoft.testmaps.di.AppComponent;
import com.simbirsoft.testmaps.di.DaggerAppComponent;
import com.simbirsoft.testmaps.di.modules.AppModule;
import com.simbirsoft.testmaps.di.modules.ContextModule;

public class MapApplication extends Application {
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .contextModule(new ContextModule(getApplicationContext()))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
