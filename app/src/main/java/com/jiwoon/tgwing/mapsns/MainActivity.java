package com.jiwoon.tgwing.mapsns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "0dcd3d83bc6dc4d32f06a63b3119e6d0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MapView daumMap = new MapView(this);
        daumMap.setDaumMapApiKey(API_KEY);

        ViewGroup mapContainerView = (ViewGroup) findViewById(R.id.daum_map);
        mapContainerView.addView(daumMap);

        // 중심점 변경
        daumMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
    }
}
