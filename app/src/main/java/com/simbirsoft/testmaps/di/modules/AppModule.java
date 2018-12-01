package com.simbirsoft.testmaps.di.modules;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.simbirsoft.testmaps.gps.LocationController;
import com.simbirsoft.testmaps.net.IRestService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private IRestService rest;
    private LocationController locationController;

    public AppModule(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.59.129.2/")
                .client(createHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rest = retrofit.create(IRestService.class);

        locationController = new LocationController(context);
    }

    @Singleton
    @Provides
    IRestService provideRest() {
        return rest;
    }

    @Singleton
    @Provides
    LocationController provideLocationController() {
        return locationController;
    }

    private OkHttpClient createHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }
}
