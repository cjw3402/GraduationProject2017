package com.example.cjw.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CertificateListViewChildItem extends LinearLayout {

    private TextView documentGroupText;
    private TextView availableTimeText;
    private TextView feeInsideText;
    private TextView feeOutsideText;
    private TextView identityCheckText;

    public CertificateListViewChildItem(Context context) {
        super(context);

        // 레이아웃을 메모리에 객체화
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_certificate_list_view_child, this, true);

        documentGroupText = (TextView) findViewById(R.id.documentGroupText);
        availableTimeText = (TextView) findViewById(R.id.availableTimeText);
        feeInsideText = (TextView) findViewById(R.id.feeInsideText);
        feeOutsideText = (TextView) findViewById(R.id.feeOutsideText);
        identityCheckText = (TextView) findViewById(R.id.identityCheckText);
    }

    public void setDocumentGroupText(String documentGroup) {
        this.documentGroupText.setText(documentGroup);
    }

    public void setAvailableTimeText(String availableTime) {
        this.availableTimeText.setText(availableTime);
    }

    public void setFeeInsideText(String feeInside) {
        this.feeInsideText.setText(feeInside);
    }

    public void setFeeOutsideText(String feeOutside) {
        this.feeOutsideText.setText(feeOutside);
    }

    public void setIdentityCheckText(String identityCheck) {
        this.identityCheckText.setText(identityCheck);
    }

}
