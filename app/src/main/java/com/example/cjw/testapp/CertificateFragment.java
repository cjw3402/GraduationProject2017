package com.example.cjw.testapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.cjw.testapp.db.MachineDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class CertificateFragment extends Fragment {

    private static final String TAG = "CertificateFragment";

    private Context mContext = null;
    private String[] certificateTypeData = null;
    private ExpandableListView mExpandableListView = null;
    private ArrayList<String> arrayGroup = new ArrayList<>();
    private HashMap<String, ArrayList<CertificateListChildItem>> arrayChild = new HashMap<>();
    private CertificateListAdapter certificateListAdapter = null;               // 증명서 리스트 어뎁터

    public CertificateFragment(Context mContext, String certificate_type) {
        this.mContext = mContext;
        this.certificateTypeData = certificate_type.replace(" ", "").split("\\+");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_certificate, container, false);
        mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);

        setGroupData();
        setChildData();

        certificateListAdapter = new CertificateListAdapter(mContext, arrayGroup, arrayChild);
        mExpandableListView.setAdapter(certificateListAdapter);

        return rootView;
    }

    private void setGroupData() {
        for (String aCertificateTypeData : certificateTypeData) {
            arrayGroup.add(aCertificateTypeData);
        }

        Comparator<String> compareAsc = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        Collections.sort(arrayGroup, compareAsc);
    }

    private void setChildData() {
        ArrayList<CertificateListChildItem> arrayList;
        CertificateListChildItem childItem;

        for (int i=0; i<arrayGroup.size(); i++) {
            arrayList = new ArrayList<>();
            childItem = getDocumentInfo(arrayGroup.get(i));

            arrayList.add(childItem);
            arrayChild.put(arrayGroup.get(i), arrayList);
        }
    }

    private CertificateListChildItem getDocumentInfo(String documentName) {
        CertificateListChildItem childItem = null;

        if (MainActivity.database != null) {
            String SQL = "select * from " + MachineDatabase.TABLE_DOCUMENT_INFO
                    + " where document_name = '"+ documentName + "' order by _id";

            Cursor outCursor = MainActivity.database.rawQuery(SQL);
            if (outCursor.getCount() == 0)
                return null;

            outCursor.moveToFirst();

            Integer id = outCursor.getInt(0);
            String document_group = outCursor.getString(1);
            String available_time = outCursor.getString(3);
            String fee_inside = outCursor.getString(4);
            String fee_outside = outCursor.getString(5);
            String identity_check = outCursor.getString(6);

            outCursor.close();

            childItem = new CertificateListChildItem(id, document_group,
                    documentName, available_time, fee_inside, fee_outside, identity_check);
        }

        return childItem;
    }

}
