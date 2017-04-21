package com.example.cjw.testapp;

import java.io.Serializable;

public class MachineListItem implements Serializable {

    /**
     * Item ID
     */
    private Integer id = null;

    /**
     * Distance
     */
    private Double distance = null;

    /**
     * Data array
     */
    private String[] dataArray = null;

    /**
     * This item is selectable
     */
    private boolean selectable = false;

    /**
     * Constructor
     */
    public MachineListItem(Integer id, Double distance, String installation_place, String installation_location,
                           String hours_of_operation, String certificate_type,
                           String road_name_address, String management_agency_name,
                           String contact_number, String land_lot_number_address,
                           String longitude, String latitude, String date_of_last_update) {

        this.id = id;
        this.distance = distance;

        dataArray = new String[11];
        dataArray[0] = installation_place;
        dataArray[1] = installation_location;
        dataArray[2] = hours_of_operation;
        dataArray[3] = certificate_type;
        dataArray[4] = road_name_address;
        dataArray[5] = management_agency_name;
        dataArray[6] = contact_number;
        dataArray[7] = land_lot_number_address;
        dataArray[8] = longitude;
        dataArray[9] = latitude;
        dataArray[10] = date_of_last_update;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String[] getDataArray() {
        return dataArray;
    }

    public String getData(int index) {
        if (dataArray == null || index >= dataArray.length || index < 0)
            return null;

        return dataArray[index];
    }

    public void setDataArray(String[] dataArrary) {
        this.dataArray = dataArrary;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

}
