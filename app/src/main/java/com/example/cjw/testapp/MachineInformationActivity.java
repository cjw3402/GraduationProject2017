package com.example.cjw.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MachineInformationActivity extends AppCompatActivity {

    Fragment informationFragment;
    Fragment certificateFragment;

    private MachineListItem itemInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_information);

        Intent intent = getIntent();
        itemInformation = (MachineListItem) intent.getSerializableExtra("itemInfo");

        setTitle(itemInformation.getData(0));   // title setting
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // create home button

        informationFragment = new InformationFragment();
        certificateFragment = new CertificateFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, informationFragment).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("기본정보"));
        tabs.addTab(tabs.newTab().setText("발급가능 문서"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                Log.d("this", "선택된 탭: " + position);

                Fragment selected = null;
                if (position == 0) {
                    selected = informationFragment;
                }
                else if (position == 1) {
                    selected = certificateFragment;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }

        });

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

    // phone call button click event
    public void onPhoneCallButtonClicked(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-9132-1260"));
        startActivity(intent);
    }

}
