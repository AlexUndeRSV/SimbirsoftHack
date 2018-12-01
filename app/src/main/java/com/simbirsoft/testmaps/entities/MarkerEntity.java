package com.simbirsoft.testmaps.entities;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class MarkerEntity {
    @SerializedName("id")
    private String id;
    @SerializedName("longitude")
    private double lon;
    @SerializedName("latitude")
    private double lat;
    @SerializedName("type")
    private String type;
    @SerializedName("url")
    private String imageUrl;
    @SerializedName("casualty_radius")
    private Double radius;

    private Bitmap bitmap;

    public MarkerEntity(double lat, double lon) {
        this.lon = lon;
        this.lat = lat;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MarkerEntity)) {
            return false;
        }
        MarkerEntity marker = (MarkerEntity) obj;
        return id.equals(marker.id);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return id;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Double getRadius() {
        return radius;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}