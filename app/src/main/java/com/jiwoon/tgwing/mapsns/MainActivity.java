package com.jiwoon.tgwing.mapsns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "7d21acf9557f38468561eff63f87ce8e";

    public MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = new MapView(MainActivity.this);
        mMapView.setDaumMapApiKey(API_KEY);

        ViewGroup mapContainerView = (ViewGroup) findViewById(R.id.daum_map);
        mapContainerView.addView(mMapView);
//
//        // 중심점 변경
//        daumMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
    }
}
