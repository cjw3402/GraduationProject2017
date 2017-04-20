package com.example.cjw.testapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cjw.testapp.db.MachineDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static MachineDatabase database = null;
    public static AppCompatActivity mainActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.app_icon_ldpi);

        // database open
        openDatabase();

        // google map fragment setting
        Fragment googleMapFragment = new GoogleMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, googleMapFragment).commit();
    }

    /**
     * 데이터베이스 열기
     */
    public void openDatabase() {
        // open database
        if (database != null) {
            database.close();
            database = null;
        }

        database = MachineDatabase.getInstance(this);

        /* database is null test code */
//        if (database == null)
//            Toast.makeText(this, "database is null...", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(this, "database is not null!", Toast.LENGTH_SHORT).show();

        boolean isOpen = database.open();
        if (isOpen) {
            Log.d(TAG, "Machine database is open.");
        }
        else {
            Log.e(TAG, "Machine database is not open.");
        }
    }

    // create action bar - main_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // action bar item select event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        // 해당 item이 select_item이면 CertificateSelectDialog 실행
        if (id == R.id.select_item) {
            Toast.makeText(this, "증명서 선택 버튼 이벤트", Toast.LENGTH_SHORT).show();

            new CertificateSelectDialog(this).show();

            return true;
        }

        // 해당 item이 search_item이면 MachineSearchActivity 실행
        if (id == R.id.search_item) {
            Toast.makeText(this, "검색 버튼 이벤트", Toast.LENGTH_SHORT).show();

            intent = new Intent(this, MachineSearchActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // back key가 눌려지면 어플 종료 확인 dialog 생성
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("종료 확인");
            builder.setIcon(R.drawable.ic_info_outline_black_24dp);
            builder.setMessage("프로그램을 종료하시겠습니까?");

            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            });

            builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setCancelable(false);   // dialog 바깥의 배경을 클릭해도 대화창이 닫히지 않게 함
            builder.show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
