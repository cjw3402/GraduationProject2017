package com.example.cjw.testapp;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class MachineListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MachineListItem> items = new ArrayList<>();

    // constructor
    public MachineListAdapter(Context context) {
        this.mContext = context;
    }

    public void clear() {
        items.clear();
    }

    public void addItem(MachineListItem item) {
        items.add(item);
    }

    public void setListItems(ArrayList<MachineListItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return items.get(position).isSelectable();
        }
        catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MachineListViewItem itemView;

        // convertView object: NULL ==> create new object
        if (convertView == null) {
            itemView = new MachineListViewItem(mContext);
        }
        // convertView object: not NULL ==> reuse object
        else {
            itemView = (MachineListViewItem) convertView;
        }

        // set current item data
        itemView.setInstallationPlace(items.get(position).getData(0));

        if (measureDistance(position) == -1)
            itemView.setDistance("거리 계산 오류");
        else
            itemView.setDistance(String.format("%.2fkm", measureDistance(position)));

        itemView.setLoadNameAddress(items.get(position).getData(4));
        itemView.setLandLotNumberAddress(items.get(position).getData(7));

        return itemView;
    }

    private double measureDistance(int position) {
        double distance = -1;

        Location currentLocation = GoogleMapFragment.currentLocation;
        Location machineLocation = new Location("machine");

        machineLocation.setLatitude(Double.parseDouble(items.get(position).getData(9)));
        machineLocation.setLongitude(Double.parseDouble(items.get(position).getData(8)));

        if (currentLocation != null)
            distance = currentLocation.distanceTo(machineLocation) / 1000;

        return distance;
    }

}
