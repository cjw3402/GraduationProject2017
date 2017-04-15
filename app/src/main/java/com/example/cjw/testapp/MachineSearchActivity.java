package com.example.cjw.testapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjw.testapp.db.MachineDatabase;

public class MachineSearchActivity extends AppCompatActivity {

    private static final String TAG = "MachineSearchActivity";

    private EditText searchAddress;                  // 검색창
    private ListView certificateListView;            // 발급기 리스트 뷰
    private MachineListAdapter machineListAdapter;   // 발급기 리스트 어뎁터

    private int listItemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_search);

        // title setting
        setTitle("무인 발급기 검색");

        // create home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchAddress = (EditText) findViewById(R.id.searchAddress);
        certificateListView = (ListView) findViewById(R.id.machineList);

        // create adapter object
        machineListAdapter = new MachineListAdapter(this);
        // listView - adapter setting
        certificateListView.setAdapter(machineListAdapter);
        // listView item select event
        certificateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewMachineInformation(position);
            }
        });

        setEditorAction();
    }

    // searchAddress(EditText)의 IME actionButton function change
    private void setEditorAction() {
        searchAddress.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        onSearchIconClicked(v);

                        break;
                }

                return true;
            }
        });
    }

    // load list data
    private int loadSearchDataList() {
        String keyword = searchAddress.getText().toString();
        if (keyword.length() == 0) {
            Toast.makeText(this, "검색 키워드를 입력해주세요.", Toast.LENGTH_LONG).show();
            machineListAdapter.clear();
            machineListAdapter.notifyDataSetChanged();   // update listView

            return -1;
        }

        String SQL = "select * from " + MachineDatabase.TABLE_MACHINE_INFO
                + " where road_name_address like '%" + keyword
                + "%' or land_lot_number_address like '%" + keyword
                + "%' order by _id";

        int recordCount = -1;
        if (MainActivity.database != null) {
            Cursor outCursor = MainActivity.database.rawQuery(SQL);
            recordCount = outCursor.getCount();
            Log.d(TAG, "cursor count: " + recordCount + "\n");

            machineListAdapter.clear();
            for (int i=0; i<recordCount; i++) {
                outCursor.moveToNext();

                Integer id = outCursor.getInt(0);
                String installation_place = outCursor.getString(1);
                String installation_location = outCursor.getString(2);
                String hours_of_operation = outCursor.getString(3);
                String certificate_type = outCursor.getString(4);
                String road_name_address = outCursor.getString(5);
                String management_agency_name = outCursor.getString(6);
                String contact_number = outCursor.getString(7);
                String land_lot_number_address = outCursor.getString(8);
                String longitude = outCursor.getString(9);
                String latitude = outCursor.getString(10);
                String date_of_last_update = outCursor.getString(11);

                String[] splitRoad = road_name_address.split(" ");
                String[] splitLand = land_lot_number_address.split(" ");

                String[] minSplit;
                if (splitRoad.length < splitLand.length)
                    minSplit = splitRoad;
                else
                    minSplit = splitLand;

                int index;
                for (index=0; index<minSplit.length; index++) {
                    if (!splitRoad[index].equals(splitLand[index])) {
                        break;
                    }
                }

                if (index > 0) {
                    land_lot_number_address = "";
                    for (int j=0; j<splitLand.length; j++) {
                        if (index <= j)
                            land_lot_number_address = land_lot_number_address + splitLand[j] + " ";
                    }
                }

                machineListAdapter.addItem(new MachineListItem(id, installation_place,
                        installation_location, hours_of_operation, certificate_type,
                        road_name_address, management_agency_name, contact_number,
                        land_lot_number_address, longitude, latitude, date_of_last_update));
            }
            outCursor.close();

            machineListAdapter.notifyDataSetChanged();   // update listView
        }

        return recordCount;
    }

    private void viewMachineInformation(int position) {
        Intent intent = new Intent(MachineSearchActivity.this, MachineInformationActivity.class);
        startActivity(intent);
    }

    // action bar item select event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // home icon event
        if (id == android.R.id.home) {
            Toast.makeText(this, "홈 아이콘 이벤트", Toast.LENGTH_SHORT).show();
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // addressClearIcon click event ==> searchAddress clear
    public void onAddressClearIconClicked(View v) {
        searchAddress.setText("");
    }

    // searchIcon click event
    public void onSearchIconClicked(View v) {
        loadSearchDataList(); // search data loading
    }

}
