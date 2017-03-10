package com.jiwoon.tgwing.mapsns;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String API_KEY = "7d21acf9557f38468561eff63f87ce8e";

    public LocationManager locationManager;
    public MapView daumMap;

    public EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ACCESS_FINE_LOCATION 권한허가 (Using TedPermission Library)
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                // 다음지도 불러오기
                daumMap = new MapView(MainActivity.this);
                daumMap.setDaumMapApiKey(API_KEY);

                ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.daum_map);
                mapViewContainer.addView(daumMap);
                Log.d(TAG, "Daum Map is called");

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                // GPS, Network 프로바이더 사용가능여부
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                Log.d(TAG, "isGPSEnabled : " + isGPSEnabled);
                Log.d(TAG, "isNetworkEnabled : " + isNetworkEnabled);

                // 현위치 가져오기
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d(TAG, "latitude : " + latitude + ", longitude : " + longitude);
                        // 중심점 설정, 핀 가져오기
                        setCurrentLocation(latitude, longitude);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED ) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("이 어플리케이션을 사용하기 위해선 위치 접근권한이 필요합니다.")
                .setDeniedCloseButtonText("위치 접근권한을 거부하시면 서비스를 이용할 수 없습니다.\n[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        searchText = (EditText) findViewById(R.id.activity_main_search);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchText == null) {
                    searchText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.editTextNull));
                } else {
                    searchText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.editTextNotNull));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setCurrentLocation(double lat, double lng) {
        // 내 위치로 이동
        daumMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
    }
}
