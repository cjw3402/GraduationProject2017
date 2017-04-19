package com.example.cjw.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MachineListViewItem extends LinearLayout {

    private TextView installationPlace;
    private TextView distance;
    private TextView loadNameAddress;
    private TextView landLotNumberAddress;

    public MachineListViewItem(Context context) {
        super(context);

        // 레이아웃을 메모리에 객체화
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_machine_list_view, this, true);

        installationPlace = (TextView) findViewById(R.id.installationPlace);
        distance = (TextView) findViewById(R.id.distance);
        loadNameAddress = (TextView) findViewById(R.id.loadNameAddress);
        landLotNumberAddress = (TextView) findViewById(R.id.landLotNumberAddress);
    }

    public void setInstallationPlace(String installationPlace) {
        this.installationPlace.setText(installationPlace);
    }

    public void setDistance(String distance) {
        this.distance.setText(distance);
    }

    public void setLoadNameAddress(String loadNameAddress) {
        this.loadNameAddress.setText(loadNameAddress);
    }

    public void setLandLotNumberAddress(String landLotNumberAddress) {
        this.landLotNumberAddress.setText(landLotNumberAddress);
    }

}
