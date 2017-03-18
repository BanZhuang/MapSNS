package com.jiwoon.tgwing.mapsns;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tsengvn.typekit.TypekitContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int navTag = 0;

    public ImageView buttonNavigationMap;
    public ImageView buttonCreateMemo;
    public ImageView buttonProfile;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = new MapFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        buttonNavigationMap = (ImageView) findViewById(R.id.navigation_map);
        buttonNavigationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프레그먼트 교체하기
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(navTag != 0) {
                    Fragment newFragment = new MapFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, newFragment)
                            .addToBackStack(null)
                            .commit();
                    navTag = 0;
                }

                // 백그라운드 날려버리기
                if(fragmentManager.getBackStackEntryCount()>=0) {
                    fragmentManager.popBackStack();
                    Log.d(TAG, "background popped");
                }
            }
        });

        buttonCreateMemo = (ImageView) findViewById(R.id.navigation_create_memo);
        buttonCreateMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(navTag != 1) {

                    navTag = 1;
                }

                // 백그라운드 날려버리기
                if(fragmentManager.getBackStackEntryCount()>=0) {
                    fragmentManager.popBackStack();
                    Log.d(TAG, "background popped");
                }
            }
        });

        buttonProfile = (ImageView) findViewById(R.id.navigation_profile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(navTag != 2) {

                    navTag = 2;
                }

                // 백그라운드 날려버리기
                if(fragmentManager.getBackStackEntryCount()>=0) {
                    fragmentManager.popBackStack();
                    Log.d(TAG, "background popped");
                }
            }
        });
    }

    // Custom font 적용 (각 Activity 마다 복붙해줘야함)
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
