package com.example.cjw.testapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    public void itemSortByDistance() {
        Comparator<MachineListItem> compareAsc = new Comparator<MachineListItem>() {
            @Override
            public int compare(MachineListItem o1, MachineListItem o2) {
                if (o1.getDistance() < o2.getDistance())
                    return -1;
                else if (o1.getDistance().equals(o2.getDistance()))
                    return 0;
                else
                    return 1;
            }
        };
        Collections.sort(items, compareAsc);
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
    public MachineListItem getItem(int position) {
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
        itemView.setInstallationPlaceText(items.get(position).getData(0));

        Double distance = items.get(position).getDistance();
        if (distance == -1)
            itemView.setDistanceText("거리 계산 오류");
        else
            itemView.setDistanceText(String.format("%.2fkm", distance));

        itemView.setLoadNameAddressText(items.get(position).getData(4));
        itemView.setLandLotNumberAddressText(items.get(position).getData(7));

        return itemView;
    }

}
