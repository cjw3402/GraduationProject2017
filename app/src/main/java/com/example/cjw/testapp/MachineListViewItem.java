package com.example.cjw.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MachineListViewItem extends LinearLayout {

    private TextView installationPlaceText;
    private TextView distanceText;
    private TextView loadNameAddressText;
    private TextView landLotNumberAddressText;

    public MachineListViewItem(Context context) {
        super(context);

        // 레이아웃을 메모리에 객체화
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_machine_list_view, this, true);

        installationPlaceText = (TextView) findViewById(R.id.installationPlaceText);
        distanceText = (TextView) findViewById(R.id.distanceText);
        loadNameAddressText = (TextView) findViewById(R.id.loadNameAddressText);
        landLotNumberAddressText = (TextView) findViewById(R.id.landLotNumberAddressText);
    }

    public void setInstallationPlaceText(String installationPlace) {
        this.installationPlaceText.setText(installationPlace);
    }

    public void setDistanceText(String distance) {
        this.distanceText.setText(distance);
    }

    public void setLoadNameAddressText(String loadNameAddress) {
        this.loadNameAddressText.setText(loadNameAddress);
    }

    public void setLandLotNumberAddressText(String landLotNumberAddress) {
        this.landLotNumberAddressText.setText(landLotNumberAddress);
    }

}
