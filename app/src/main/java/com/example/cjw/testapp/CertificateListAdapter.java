package com.example.cjw.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CertificateListAdapter extends BaseExpandableListAdapter {

    private Context mContext = null;
    private ArrayList<String> arrayGroup = new ArrayList<>();
    private HashMap<String, ArrayList<CertificateListChildItem>> arrayChild = new HashMap<>();

    public CertificateListAdapter(Context mContext, ArrayList<String> arrayGroup,
                                  HashMap<String, ArrayList<CertificateListChildItem>> arrayChild) {
        this.mContext = mContext;
        this.arrayGroup = arrayGroup;
        this.arrayChild = arrayChild;
    }

    public void clear() {
        arrayGroup.clear();
    }

    @Override
    public int getGroupCount() {
        return arrayGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arrayChild.get(arrayGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrayGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arrayChild.get(arrayGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupData = arrayGroup.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_certificate_list_view_group, null);
        }

        TextView groupDataText = (TextView) convertView.findViewById(R.id.groupDataText);
        groupDataText.setText(groupData);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CertificateListViewChildItem childItemView;

        // convertView object: NULL ==> create new object
        if (convertView == null) {
            childItemView = new CertificateListViewChildItem(mContext);
        }
        // convertView object: not NULL ==> reuse object
        else {
            childItemView = (CertificateListViewChildItem) convertView;
        }

        CertificateListChildItem childItem = arrayChild.get(arrayGroup.get(groupPosition))
                .get(childPosition);

        childItemView.setDocumentGroupText(childItem.getData(0));
        childItemView.setAvailableTimeText(childItem.getData(2));
        childItemView.setFeeInsideText(childItem.getData(3));
        childItemView.setFeeOutsideText(childItem.getData(4));
        childItemView.setIdentityCheckText(childItem.getData(5));

        return childItemView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
