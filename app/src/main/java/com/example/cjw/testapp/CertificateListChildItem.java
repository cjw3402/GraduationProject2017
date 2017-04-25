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
    public CertificateListChildItem(Integer id, String documentGroup,
                                    String documentName, String detailedDocumentName,
                                    String availableTime, String feeInside,
                                    String feeOutside, String identityCheck) {

        this.id = id;

        dataArray = new String[7];
        dataArray[0] = documentGroup;
        dataArray[1] = documentName;
        dataArray[2] = detailedDocumentName;
        dataArray[3] = availableTime;
        dataArray[4] = feeInside;
        dataArray[5] = feeOutside;
        dataArray[6] = identityCheck;
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
