package com.example.cjw.testapp;

import java.io.Serializable;

public class MachineListItem implements Serializable {

    /**
     * Item ID
     */
    private Integer id = null;

    /**
     * Data array
     */
    private String[] dataArrary = null;

    /**
     * This item is selectable
     */
    private boolean selectable = false;

    /**
     * Constructor
     */
    public MachineListItem(Integer id, String installation_place, String installation_location,
                           String hours_of_operation, String certificate_type,
                           String road_name_address, String management_agency_name,
                           String contact_number, String land_lot_number_address,
                           String longitude, String latitude, String date_of_last_update) {

        this.id = id;

        dataArrary = new String[11];
        dataArrary[0] = installation_place;
        dataArrary[1] = installation_location;
        dataArrary[2] = hours_of_operation;
        dataArrary[3] = certificate_type;
        dataArrary[4] = road_name_address;
        dataArrary[5] = management_agency_name;
        dataArrary[6] = contact_number;
        dataArrary[7] = land_lot_number_address;
        dataArrary[8] = longitude;
        dataArrary[9] = latitude;
        dataArrary[10] = date_of_last_update;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String[] getDataArrary() {
        return dataArrary;
    }

    public String getData(int index) {
        if (dataArrary == null || index >= dataArrary.length || index < 0)
            return null;

        return dataArrary[index];
    }

    public void setDataArrary(String[] dataArrary) {
        this.dataArrary = dataArrary;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
