package com.example.cjw.testapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InformationFragment extends Fragment {

    private Context mContext;
    private MachineListItem itemInformation;

    public InformationFragment(Context context, MachineListItem itemInformation) {
        mContext = context;
        this.itemInformation = itemInformation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_information, container, false);

        setItemData(rootView);

        return rootView;
    }

    private void setItemData(ViewGroup viewGroup) {
        TextView installation_location_text = (TextView) viewGroup.findViewById(R.id.installationLocationText);
        TextView hours_of_operation_text = (TextView) viewGroup.findViewById(R.id.hoursOfOperationText);
        TextView road_name_address_text = (TextView) viewGroup.findViewById(R.id.roadNameAddressText);
        TextView land_lot_number_address_text = (TextView) viewGroup.findViewById(R.id.landLotNumberAddressText);
        TextView management_agency_name_text = (TextView) viewGroup.findViewById(R.id.managementAgencyNameText);
        TextView contact_number_text = (TextView) viewGroup.findViewById(R.id.contactNumberText);
        TextView date_of_last_update_text = (TextView) viewGroup.findViewById(R.id.date_of_last_update_text);

        installation_location_text.setText(itemInformation.getData(1));
        hours_of_operation_text.setText(itemInformation.getData(2));
        road_name_address_text.setText(itemInformation.getData(4));
        land_lot_number_address_text.setText(itemInformation.getData(7));
        management_agency_name_text.setText(itemInformation.getData(5));
        contact_number_text.setText(itemInformation.getData(6));
        date_of_last_update_text.setText(date_of_last_update_text.getText() + itemInformation.getData(10));
    }

}
