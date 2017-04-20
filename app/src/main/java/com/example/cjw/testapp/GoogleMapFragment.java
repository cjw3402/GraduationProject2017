package com.example.cjw.testapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cjw.testapp.db.MachineDatabase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

public class GoogleMapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static  final String TAG = "GoogleMapFragment";

    public static Location currentLocation = null;
    public static GoogleMap mGoogleMap = null;

    private AppCompatActivity mActivity = null;
    private GoogleApiClient mGoogleApiClient = null;
    private Marker mCurrentMarker = null;
    private boolean askPermissionOnceAgain = false;
    private boolean isDoneMarkerCreation = false;
    private ClusterManager<MarkerItem> mClusterManager = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);

        mActivity = MainActivity.mainActivity;

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        return view;
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();

        super.onStart();
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

    @Override
    public void onPause() {
        // 위치 업데이트 중지
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        super.onPause();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(false);

        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        // API 23 이상이면 런타임 퍼미션 처리 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);

            if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(mActivity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        BasicInfo.REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
            }
        }

        setDefaultLocation();

        setUpCluster();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(BasicInfo.UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(BasicInfo.FASTEST_UPDATE_INTERVAL_MS);

        if (ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services connection lost.\n"
                    + "Cause = network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended(): Google Play services connection lost.\n"
                    + "Cause = service disconnected.");
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!isDoneMarkerCreation) {
            currentLocation = location;

            setCurrentLocation(currentLocation);   // 현재 위치에 마커 생성
        }
    }

    public void setDefaultLocation() {
        if (mCurrentMarker != null)
            mCurrentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(BasicInfo.DEFAULT_LOCATION);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        mCurrentMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BasicInfo.DEFAULT_LOCATION, 15));
    }

    public void setCurrentLocation(Location location) {
        if (mCurrentMarker != null)
            mCurrentMarker.remove();

        LatLng latLng= new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.person_icon));

        mCurrentMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        isDoneMarkerCreation = true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED && fineLocationRationale) {
            showDialogForPermission();
        }
        else if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == BasicInfo.REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION &&grantResults.length > 0) {
            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (!permissionAccepted) {
                checkPermissions();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("알림");
        builder.setMessage(BasicInfo.DIALOG_FOR_PERMISSION_MSG);
        builder.setCancelable(false);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(mActivity, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        BasicInfo.REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.finish();
            }
        });

        builder.create().show();
    }

    private void showDialogForPermissionSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("알림");
        builder.setMessage(BasicInfo.DIALOG_FOR_PERMISSION_SETTING_MSG);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mActivity.startActivity(myAppSettings);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.finish();
            }
        });

        builder.create().show();
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("위치 서비스 비활성화 안내");
        builder.setMessage(BasicInfo.DIALOG_FOR_LOCATION_SERVICE_SETTING_MSG);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, BasicInfo.REQUEST_GPS_ENABLE);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    /**
     * 지도에 무인 발급기 마커 생성
     */
    private int setLocationMarkers() {
        String SQL = "select _id, latitude, longitude from " + MachineDatabase.TABLE_MACHINE_INFO
                + " where latitude != '' and longitude != ''";

        int recordCount = -1;
        if (MainActivity.database != null) {
            Cursor outCursor = MainActivity.database.rawQuery(SQL);
            recordCount = outCursor.getCount();
            Log.d(TAG, "cursor count: " + recordCount + "\n");

            for (int i=0; i<recordCount; i++) {
                outCursor.moveToNext();

                Integer id = outCursor.getInt(0);
                Double latitude = Double.parseDouble(outCursor.getString(1));
                Double longitude = Double.parseDouble(outCursor.getString(2));

                MarkerItem markerItem = new MarkerItem(id, latitude, longitude);
                mClusterManager.addItem(markerItem);
            }

            outCursor.close();
        }

        return recordCount;
    }

    private void setUpCluster() {
        // Initialize the manager with the context and the map.
        mClusterManager = new ClusterManager<>(mActivity, mGoogleMap);
        mClusterManager.setRenderer(new ClusterRenderer(mActivity, mGoogleMap, mClusterManager));

        // Point the map's listeners at the listeners implemented by the cluster manager.
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);

        // Set cluster item click listener
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
            @Override
            public boolean onClusterItemClick(MarkerItem markerItem) {
                Toast.makeText(mActivity, "Marker is Clicked!", Toast.LENGTH_SHORT).show();
                viewMachineInformation(getMachineData(markerItem.getId()));

                return true;
            }
        });

        // Add cluster items (markers) to the cluster manager.
        setLocationMarkers();
    }

    private MachineListItem getMachineData(Integer id) {
        MachineListItem item = null;
        String SQL = "select * from " + MachineDatabase.TABLE_MACHINE_INFO
                + " where _id = " + id;

        if (MainActivity.database != null) {
            Cursor outCursor = MainActivity.database.rawQuery(SQL);

            outCursor.moveToFirst();
            item = new MachineListItem(id, outCursor.getString(1), outCursor.getString(2),
                    outCursor.getString(3), outCursor.getString(4), outCursor.getString(5),
                    outCursor.getString(6), outCursor.getString(7), outCursor.getString(8),
                    outCursor.getString(9), outCursor.getString(10), outCursor.getString(11));
        }

        return item;
    }

    private void viewMachineInformation(MachineListItem item) {
        Intent intent = new Intent(mActivity, MachineInformationActivity.class);
        intent.putExtra("itemInfo", item);

        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case BasicInfo.REQUEST_GPS_ENABLE:
                // 사용자가 GPS를 활성화 시켰는지 검사
                if (!checkLocationServicesStatus()) {
                    setDefaultLocation();
                }

                break;
        }
    }

}
