package com.simbirsoft.testmaps.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simbirsoft.testmaps.MapApplication;
import com.simbirsoft.testmaps.R;
import com.simbirsoft.testmaps.entities.MarkerEntity;
import com.simbirsoft.testmaps.entities.TeamData;
import com.simbirsoft.testmaps.gps.LocationController;
import com.simbirsoft.testmaps.mvp.presenters.MapsPresenter;
import com.simbirsoft.testmaps.mvp.views.MapsView;

import java.util.List;

import javax.inject.Inject;

public class MapsActivity extends MvpAppCompatActivity implements MapsView,
        OnMapReadyCallback, LocationController.OnLocationChangedListener {

    private static final int LOCATION_PERMISSION = 387;

    @InjectPresenter
    MapsPresenter presenter;

    @Inject
    LocationController locationController;

    private GoogleMap map;

    private TextView livesText;
    private TextView zombieText;
    private TextView jacketsTxt;
    private TextView flameTxt;

    private boolean isLocationReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapApplication.getAppComponent().inject(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        findViewById(R.id.my_toolbar);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        livesText = findViewById(R.id.lives);
        zombieText = findViewById(R.id.zombie);
        jacketsTxt = findViewById(R.id.jackets);
        flameTxt = findViewById(R.id.flamethrowers);

        findViewById(R.id.my_location).setOnClickListener(l -> {
            //TODO: Добавить перемещение на текущие координаты
            Double lng = locationController.getCurrentLocation().longitude;
            Double lat = locationController.getCurrentLocation().latitude;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));
            presenter.setMyPosition();
        });

        findViewById(R.id.my_markers).setOnClickListener(l -> {
            //TODO: Добавить такое перемещение, чтобы все метки оказались в границе видимости экрана
//            map.animateCamera(CameraUpdateFactory.newCameraPosition());


        });

        presenter.clearTasks();

        //TODO: Если реализуете функцию для получение подсказок, вызывать здесь

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationController.setLocationListener(this);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                //TODO: Добавить функционал для взятия метки. Для отправки метки использовать метод
                // presenter.takeMarker(ваша метка);
                break;
            case R.id.hybrid:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.normal:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.satellite:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        locationController.setLocationListener(this);
        presenter.getMarkers();
    }

    @Override
    public void onLocationChanged(LatLng location) {
        if (!isLocationReady) {
            presenter.setMyPosition();
            isLocationReady = true;
        }
        //TODO: Добавить отображение на карте текущий позиции

    }

    @Override
    public void onMarkersLoad(List<MarkerEntity> markerEntities) {
        //TODO: Добавить маркеры на карту
        for (MarkerEntity markerEntity : markerEntities) {
            map.addMarker(new MarkerOptions()
                    .draggable(false)
                    .position(new LatLng(markerEntity.getLat(), markerEntity.getLon()))
                    .icon(BitmapDescriptorFactory.fromBitmap(markerEntity.getBitmap()))
            );

        }
    }

    @Override
    public void onTakeMarker(String msg) {
        showToast(msg);
    }

    @Override
    public void onTeamData(TeamData data) {
        livesText.setText(String.valueOf(data.getLives()));
        zombieText.setText(String.valueOf(data.getKills()));
        //TODO: Добавить отображение количества огнеметов и шуб
        jacketsTxt.setText(String.valueOf(data.getJackets()));
        flameTxt.setText(String.valueOf(data.getFlamethrowers()));

    }

    @Override
    public void onError(Throwable t) {
        showToast(t.getMessage());
    }

    private void requestPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, LOCATION_PERMISSION);
            }
        } else {
            if (PermissionChecker.checkSelfPermission(this, permission) != PermissionChecker.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, LOCATION_PERMISSION);
            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}