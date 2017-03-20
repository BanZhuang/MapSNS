package com.jiwoon.tgwing.mapsns.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.models.UserInfo;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

/**
 * Created by jiwoonwon on 2017. 3. 11..
 */

public class MapFragment extends Fragment {

    private static final String TAG = "MapFragment";
    private static final String API_KEY = "7d21acf9557f38468561eff63f87ce8e";

    private ViewGroup mapViewContainer;
    private UserInfo mUserInfo;

    public LocationManager locationManager;
    public MapView daumMap;

    public ImageView buttonCurrentLocation;
    private int toggleCurrentLocation = 0;
    private int callCurrentLocation = 0;
    public EditText searchText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapViewContainer = (ViewGroup) view.findViewById(R.id.daum_map);
        mUserInfo = new UserInfo();

        // ACCESS_FINE_LOCATION 권한허가 (Using TedPermission Library)
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();

                // 다음지도 불러오기
                daumMap = new MapView(getActivity());
                daumMap.setDaumMapApiKey(API_KEY);

                mapViewContainer.addView(daumMap);
                Log.d(TAG, "Daum Map is called");

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
                        mUserInfo.setLatitude(latitude);
                        mUserInfo.setLongitude(longitude);

                        if (callCurrentLocation == 0) {
                            setCurrentLocation(latitude, longitude);
                            callCurrentLocation = 1;
                            Log.d(TAG, "Moved to Current Location");
                        }

                        if (toggleCurrentLocation != 0) {
                            showMyLocation();
                        }
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
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED ) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(getActivity())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("이 어플리케이션을 사용하기 위해선 위치 접근권한이 필요합니다.")
                .setDeniedCloseButtonText("위치 접근권한을 거부하시면 서비스를 이용할 수 없습니다.\n[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        buttonCurrentLocation = (ImageView) view.findViewById(R.id.fragment_map_current_location);
        buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleCurrentLocation == 0) {
                    showMyLocation();

                    toggleCurrentLocation = 1;
                    Log.d(TAG, "toggleCurrentLocation : " + toggleCurrentLocation);
                } else {
                    Bitmap imageEmpty = BitmapFactory.decodeResource(getResources(), R.drawable.icon_current_location_empty);
                    buttonCurrentLocation.setImageBitmap(imageEmpty);

                    MapPOIItem pastLocation = daumMap.findPOIItemByTag(0);
                    daumMap.removePOIItem(pastLocation);

                    toggleCurrentLocation = 0;
                }
            }
        });

        searchText = (EditText) view.findViewById(R.id.fragment_map_main_search);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void setCurrentLocation(double lat, double lng) {
        // 내 위치로 이동
        daumMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
    }

    public void showMyLocation() {
        Bitmap imageChecked = BitmapFactory.decodeResource(getResources(), R.drawable.icon_current_location_checked);
        buttonCurrentLocation.setImageBitmap(imageChecked);

        // TODO: setCurrentLocation 이벤트가 있음..! 요거로 코드 바꿔주기 ㅂㄷㅂㄷ
        if (daumMap.findPOIItemByTag(0) != null) {
            MapPOIItem pastLocation = daumMap.findPOIItemByTag(0);
            daumMap.removePOIItem(pastLocation);
        }

        Bitmap currentLocation = BitmapFactory.decodeResource(getResources(), R.drawable.icon_current_location);
        Bitmap resizeCurLocation = Bitmap.createScaledBitmap(currentLocation, (int)(currentLocation.getWidth()*0.18),
                (int)(currentLocation.getHeight()*0.18), true);

        MapPOIItem currentLocationMarker = new MapPOIItem();
        currentLocationMarker.setItemName("");
        currentLocationMarker.setTag(0);
        currentLocationMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(mUserInfo.getLatitude(), mUserInfo.getLongitude()));
        currentLocationMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        currentLocationMarker.setCustomImageBitmap(resizeCurLocation);
        currentLocationMarker.setCustomImageAutoscale(false);
        daumMap.addPOIItem(currentLocationMarker);

        setCurrentLocation(mUserInfo.getLatitude(), mUserInfo.getLongitude());
    }
}
