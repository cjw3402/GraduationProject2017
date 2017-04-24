package com.example.cjw.testapp;

public class CertificateListChildItem {

    /**
     * Item ID
     */
    private Integer id = null;

    /**
     * Data array
     */
    private String[] dataArray = null;

    /**
     * Constructor
     */
    public CertificateListChildItem(Integer id, String document_group,
                                    String document_name, String available_time,
                                    String fee_inside, String fee_outside,
                                    String identity_check) {

        this.id = id;

        dataArray = new String[6];
        dataArray[0] = document_group;
        dataArray[1] = document_name;
        dataArray[2] = available_time;
        dataArray[3] = fee_inside;
        dataArray[4] = fee_outside;
        dataArray[5] = identity_check;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String[] getDataArray() {
        return dataArray;
    }

    public String getData(int index) {
        if (dataArray == null || index >= dataArray.length || index < 0)
            return null;

        return dataArray[index];
    }

    public void setDataArray(String[] dataArray) {
        this.dataArray = dataArray;
    }

}
