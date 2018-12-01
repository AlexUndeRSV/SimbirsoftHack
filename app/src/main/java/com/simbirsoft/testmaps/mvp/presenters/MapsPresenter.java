package com.simbirsoft.testmaps.mvp.presenters;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.simbirsoft.testmaps.MapApplication;
import com.simbirsoft.testmaps.gps.LocationController;
import com.simbirsoft.testmaps.mvp.views.MapsView;
import com.simbirsoft.testmaps.net.IRestService;
import com.simbirsoft.testmaps.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MapsPresenter extends MvpPresenter<MapsView> {
    private static final int SUCCESS = 1;
    private static final int UPDATE_LOCATION_DELAY = 3;
    private static final int STANDARD_DELAY = 5;

    private static final String TEAM_ID = "4ae0e1c567e11656a07c89cec0da8f45";

    @Inject
    Context context;

    @Inject
    IRestService rest;

    @Inject
    LocationController locationController;

    private List<Disposable> disposables = new ArrayList<>();

    public MapsPresenter() {
        MapApplication.getAppComponent().inject(this);
    }

    public void setMyPosition() {
        disposables.add(Flowable.just(TEAM_ID)
                .flatMap(team -> {
                    if (locationController.getCurrentLocation() != null) {
                        return rest.setMyPosition(team,
                                locationController.getCurrentLocation().longitude,
                                locationController.getCurrentLocation().latitude);
                    } else {
                        return Flowable.error(new Exception("Current location unknown"));
                    }
                })
                .flatMap(response -> {
                    if (response.getSuccess() == SUCCESS) {
                        return Flowable.just(response.getData());
                    } else {
                        return Flowable.error(new Exception(response.getMessage()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(t -> getViewState().onError(t))
                .repeatWhen(t -> t.delay(UPDATE_LOCATION_DELAY, TimeUnit.SECONDS))
                .retryWhen(t -> t.delay(UPDATE_LOCATION_DELAY, TimeUnit.SECONDS))
                .subscribe(data -> getViewState().onTeamData(data)));
    }

    public void getMarkers() {
        disposables.add(rest.getMarkers(TEAM_ID)
                .flatMap(response -> {
                    if (response.getSuccess() == SUCCESS) {
                        return Flowable.just(response.getData());
                    } else {
                        return Flowable.error(new Exception(response.getMessage()));
                    }
                })
                .flatMap(Flowable::fromIterable)
                .subscribeOn(Schedulers.io())
                .map(marker -> {
                    if (marker.getImageUrl() != null) {
                        marker.setBitmap(BitmapUtils.loadImage(context, marker.getImageUrl(), 80));
                    }
                    return marker;
                })
                .toList()
                .toFlowable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(t -> getViewState().onError(t))
                .repeatWhen(t -> t.delay(STANDARD_DELAY, TimeUnit.SECONDS))
                .retryWhen(t -> t.delay(STANDARD_DELAY, TimeUnit.SECONDS))
                .subscribe(markerEntities -> getViewState().onMarkersLoad(markerEntities)));
    }

    //TODO: Добавить метод для загрузки подсказок



    public void takeMarker(String key) {
        rest.takeMarker(TEAM_ID, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> getViewState().onTakeMarker(response.getMessage()));
    }

    public void clearTasks() {
        if (disposables != null) {
            for (Disposable d : disposables) {
                if (!d.isDisposed()) {
                    d.dispose();
                }
            }
            disposables.clear();
        }
    }
}