package com.example.cjw.testapp;

public class CertificateListChildItem {

    /**
     * Item ID
     */
    private Integer id = null;

    /**
     * Data array
     */
    private String[] dataArrary = null;

    /**
     * Constructor
     */
    public CertificateListChildItem(Integer id, String document_group,
                                    String document_name, String available_time,
                                    String fee_inside, String fee_outside,
                                    String identity_check) {

        this.id = id;

        dataArrary = new String[6];
        dataArrary[0] = document_group;
        dataArrary[1] = document_name;
        dataArrary[2] = available_time;
        dataArrary[3] = fee_inside;
        dataArrary[4] = fee_outside;
        dataArrary[5] = identity_check;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String[] getDataArray() {
        return dataArrary;
    }

    public String getData(int index) {
        if (dataArrary == null || index >= dataArrary.length || index < 0)
            return null;

        return dataArrary[index];
    }

    public void setDataArray(String[] dataArray) {
        this.dataArrary = dataArray;
    }

}
