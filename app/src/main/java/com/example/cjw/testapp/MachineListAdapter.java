package com.example.cjw.testapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MachineListAdapter extends BaseAdapter {

    private Context context;
    private List<MachineListItem> items = new ArrayList<>();

    // constructor
    public MachineListAdapter(Context context) {
        this.context = context;
    }

    public void clear() {
        items.clear();
    }

    public void addItem(MachineListItem item) {
        items.add(item);
    }

    public void setListItems(List<MachineListItem> items) {
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
        MachineListItemView itemView;

        // convertView object: NULL ==> create new object
        if (convertView == null) {
            itemView = new MachineListItemView(context);
        }
        // convertView object: not NULL ==> reuse object
        else {
            itemView = (MachineListItemView) convertView;
        }

        // set current item data
        itemView.setInstallationPlace(items.get(position).getData(0));
        itemView.setLoadNameAddress(items.get(position).getData(4));
        itemView.setLandLotNumberAddress(items.get(position).getData(7));

        return itemView;
    }

}
