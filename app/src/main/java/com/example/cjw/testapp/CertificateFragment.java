package com.example.cjw.testapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class CertificateFragment extends Fragment {

    private Context mContext = null;
    private String[] certificateTypeData = null;
    private ExpandableListView mExpandableListView = null;
    private ArrayList<String> arrayGroup = new ArrayList<>();
    private HashMap<String, ArrayList<String>> arrayChild = new HashMap<>();

    private CertificateListVeiwChildItem certificateListVeiwItem = null;   // 증명서 리스트 뷰
    private CertificateListAdapter certificateListAdapter = null;     // 증명서 리스트 어뎁터

    public CertificateFragment(Context mContext, String certificate_type) {
        this.mContext = mContext;
        this.certificateTypeData = certificate_type.replace(" ", "").split("\\+");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_certificate, container, false);

        mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        setArrayData();

        certificateListAdapter = new CertificateListAdapter(mContext, arrayGroup, arrayChild);
        mExpandableListView.setAdapter(certificateListAdapter);

        return rootView;
    }

    private void setArrayData() {
        arrayGroup.clear();

        for (int i=0; i<certificateTypeData.length; i++) {
            arrayGroup.add(certificateTypeData[i]);
        }

        Comparator<String> compareAsc = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        Collections.sort(arrayGroup, compareAsc);

        ArrayList<String> arrayFirst = new ArrayList<>();
        arrayFirst.add("진짜");
        arrayFirst.add("싫어");

        ArrayList<String> arraySecond = new ArrayList<>();
        arraySecond.add("좀");
        arraySecond.add("제발");

        ArrayList<String> arrayThird = new ArrayList<>();
        arrayThird.add("허리가 아플 정도로");
        arrayThird.add("일어날 수 밖에 없을 때까지");
        arrayThird.add("영원히는 자는 건 거절한다");

        ArrayList<String> arrayFourth = new ArrayList<>();
        arrayFourth.add("살려줘");
        arrayFourth.add("진심");

        arrayChild.put(arrayGroup.get(0), arrayFirst);
        arrayChild.put(arrayGroup.get(1), arraySecond);
        arrayChild.put(arrayGroup.get(2), arrayThird);
        arrayChild.put(arrayGroup.get(3), arrayFourth);
    }

}
