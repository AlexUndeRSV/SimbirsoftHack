package com.simbirsoft.testmaps.di;

import com.simbirsoft.testmaps.di.modules.AppModule;
import com.simbirsoft.testmaps.di.modules.ContextModule;
import com.simbirsoft.testmaps.mvp.presenters.MapsPresenter;
import com.simbirsoft.testmaps.ui.MapsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ContextModule.class})
public interface AppComponent {
    void inject(MapsPresenter presenter);
    void inject(MapsActivity activity);
}
