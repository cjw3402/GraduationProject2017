package com.example.cjw.testapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

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
    private ArrayList<String> arrayGroup = null;
    private HashMap<String, ArrayList<CertificateListChildItem>> arrayChild = null;
    private CertificateListAdapter certificateListAdapter = null;   // 증명서 리스트 어뎁터

    public static CertificateFragment newInstance(String certificate_type) {
        CertificateFragment fragment = new CertificateFragment();

        Bundle bundle = new Bundle();
        bundle.putString("certificate_type", certificate_type);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_certificate, container, false);
        mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);

        certificateTypeData = getArguments().getString("certificate_type").split("\\+");

        setGroupData(certificateTypeData);
        setChildData();

        certificateListAdapter = new CertificateListAdapter(mContext, arrayGroup, arrayChild);
        mExpandableListView.setAdapter(certificateListAdapter);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (certificateListAdapter.getChildrenCount(groupPosition) == 0)
                    Toast.makeText(mContext, "해당 정보가 없습니다.", Toast.LENGTH_LONG).show();

                return false;
            }
        });

        return rootView;
    }

    private void setGroupData(String[] certificateTypeData) {
        arrayGroup = new ArrayList<>();
        String lastChild = null;

        if (MainActivity.database != null) {
            for (int i=0; i<certificateTypeData.length; i++) {
                String SQL = "select document_name, detailed_document_name from "
                        + MachineDatabase.TABLE_DOCUMENT_INFO
                        + " where document_group = '"+ certificateTypeData[i]
                        + "' or document_name = '" + certificateTypeData[i]
                        + "' or detailed_document_name = '" + certificateTypeData[i]
                        + "' order by _id";

                Cursor outCursor = MainActivity.database.rawQuery(SQL);
                int recordCount = outCursor.getCount();

                if (recordCount == 0) {
                    if (i == certificateTypeData.length-1) {
                        lastChild = certificateTypeData[i];
                    }
                    else {
                        arrayGroup.add(certificateTypeData[i]);
                    }
                }
                else {
                    for (int j=0; j<recordCount; j++) {
                        outCursor.moveToNext();

                        String document_name = outCursor.getString(0);
                        String detailed_document_name = outCursor.getString(1);

                        if (detailed_document_name != null)
                            arrayGroup.add(detailed_document_name);
                        else if (document_name != null)
                            arrayGroup.add(document_name);
                        else {
                            Log.e(TAG, "Group Item Data Error...");
                            return;
                        }
                    }

                }

                outCursor.close();
            }

            Comparator<String> compareAsc = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            };
            Collections.sort(arrayGroup, compareAsc);

            if (lastChild != null)
                arrayGroup.add(lastChild);
        }   // if (MainActivity.database != null): end

    }

    private void setChildData() {
        ArrayList<CertificateListChildItem> arrayList;
        CertificateListChildItem childItem;

        arrayChild = new HashMap<>();

        for (int i=0; i<arrayGroup.size(); i++) {
            arrayList = new ArrayList<>();
            childItem = getDocumentInfo(arrayGroup.get(i));

            if (childItem != null) {
                arrayList.add(childItem);
                arrayChild.put(arrayGroup.get(i), arrayList);
            }
        }
    }

    @Nullable
    private CertificateListChildItem getDocumentInfo(String groupName) {
        CertificateListChildItem childItem = null;

        if (MainActivity.database != null) {
            String SQL = "select * from " + MachineDatabase.TABLE_DOCUMENT_INFO
                    + " where detailed_document_name = '"+ groupName + "' order by _id";

            Cursor outCursor = MainActivity.database.rawQuery(SQL);
            if (outCursor.getCount() == 1) {
                outCursor.moveToFirst();

                Integer id = outCursor.getInt(0);
                String document_group = outCursor.getString(1);
                String document_name = outCursor.getString(2);
                String detailed_document_name = outCursor.getString(3);
                String available_time = outCursor.getString(4);
                String fee_inside = outCursor.getString(5);
                String fee_outside = outCursor.getString(6);
                String identity_check = outCursor.getString(7);

                childItem = new CertificateListChildItem(id, document_group,
                        document_name, detailed_document_name, available_time,
                        fee_inside, fee_outside, identity_check);

                outCursor.close();
            }
            else if (outCursor.getCount() == 0) {
                SQL = "select * from " + MachineDatabase.TABLE_DOCUMENT_INFO
                        + " where document_name = '"+ groupName + "' order by _id";

                outCursor = MainActivity.database.rawQuery(SQL);
                if (outCursor.getCount() == 1) {
                    outCursor.moveToFirst();

                    Integer id = outCursor.getInt(0);
                    String document_group = outCursor.getString(1);
                    String document_name = outCursor.getString(2);
                    String detailed_document_name = outCursor.getString(3);
                    String available_time = outCursor.getString(4);
                    String fee_inside = outCursor.getString(5);
                    String fee_outside = outCursor.getString(6);
                    String identity_check = outCursor.getString(7);

                    childItem = new CertificateListChildItem(id, document_group,
                            document_name, detailed_document_name, available_time,
                            fee_inside, fee_outside, identity_check);

                    outCursor.close();
                }
                else {
                    return null;
                }

            }   // else if end
            else {   // child not found error
                Log.e(TAG, "Child Item Count Error...");
                return null;
            }

        }   // if (MainActivity.database != null): end

        return childItem;
    }

}
