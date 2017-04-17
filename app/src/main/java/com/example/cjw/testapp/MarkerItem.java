package com.example.cjw.testapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {

    private Integer id;
    private LatLng latLng;

    public MarkerItem(Integer id, Double latitude, Double longitude) {
        this.id = id;
        this.latLng = new LatLng(latitude, longitude);
    }

    public Integer getId() {
        return id;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
