package com.example.cjw.testapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {

    private LatLng latLng;
    private MachineListItem item;

    public MarkerItem(Double latitude, Double longitude, MachineListItem item) {
        this.latLng = new LatLng(latitude, longitude);
        this.item = item;
    }

    public MachineListItem getItem() {
        return item;
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
