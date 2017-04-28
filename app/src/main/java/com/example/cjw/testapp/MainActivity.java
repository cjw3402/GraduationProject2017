package com.example.cjw.testapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cjw.testapp.db.MachineDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private boolean askPermissionOnceAgain = false;

    public static MachineDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Splash 화면 띄우기
        startActivity(new Intent(this, SplashActivity.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();   // Splash 표시와 초기화를 동시에 진행

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.title_icon);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 사용 권한을 허가했는지 다시 검사
        if (askPermissionOnceAgain) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }

    /**
     * 초기화 작업 처리 - 동시 진행을 위해 쓰레드 처리
    */
    private void init() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // database open
                openDatabase();

                // google map fragment setting
                Fragment googleMapFragment = new GoogleMapFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, googleMapFragment).commit();
            }
        };
        new Thread(runnable).start();
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
//            Toast.makeText(this, "증명서 선택 버튼 이벤트", Toast.LENGTH_SHORT).show();

            new CertificateSelectDialog(this).show();

            return true;
        }

        // 해당 item이 search_item이면 MachineSearchActivity 실행
        if (id == R.id.search_item) {
//            Toast.makeText(this, "검색 버튼 이벤트", Toast.LENGTH_SHORT).show();

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

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED && fineLocationRationale) {
            showDialogForPermission();
        }
        else if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == BasicInfo.REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION && grantResults.length > 0) {
            boolean permissionAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);

            if (permissionAccepted) {
                // 재시작
                this.recreate();

            }
            else {
                checkPermissions();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage(BasicInfo.DIALOG_FOR_PERMISSION_MSG);
        builder.setCancelable(false);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        BasicInfo.REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.create().show();
    }

    private void showDialogForPermissionSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage(BasicInfo.DIALOG_FOR_PERMISSION_SETTING_MSG);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + MainActivity.this.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                MainActivity.this.startActivity(myAppSettings);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.create().show();
    }

}
