package com.example.cjw.testapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class CertificateSelectDialog extends Dialog {

    private Context context;
    String[] certificate;

    private Button selectBtn;
    private Button cancelBtn;
    private ListView certificateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   // delate title
        setContentView(R.layout.dialog_certificate_select);

        setCanceledOnTouchOutside(false);   // dialog 바깥의 배경을 클릭해도 대화창이 닫히지 않게 함

        selectBtn = (Button) findViewById(R.id.selectBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        certificateList = (ListView) findViewById(R.id.certificateList);

        certificate = new String[] {"주민등록등본", "주민등록초본", "개별공시지가확인서", " 토지이용계획확인서", "토지대장등본", "대지권등록부", "건축물대장",
                "주민등록등본", "주민등록초본", "개별공시지가확인서", " 토지이용계획확인서", "토지대장등본", "대지권등록부", "건축물대장"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, certificate);
        certificateList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        certificateList.setAdapter(adapter);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public CertificateSelectDialog(Context context) {
        super(context);
        this.context = context;
    }

}
