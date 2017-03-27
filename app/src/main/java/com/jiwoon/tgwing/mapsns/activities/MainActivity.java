package com.jiwoon.tgwing.mapsns.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jiwoon.tgwing.mapsns.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public ImageView buttonNavigationMap;
    public ImageView buttonCreateMemo;
    public ImageView buttonProfile;

    private MapFragment     mapFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();

        buttonNavigationMap = (ImageView) findViewById(R.id.navigation_map);
        buttonNavigationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프레그먼트 교체하기

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mapFragment)
                        .addToBackStack(null)
                        .commit();

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

            }
        });

        buttonProfile = (ImageView) findViewById(R.id.navigation_profile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        mapFragment = new MapFragment();

        fragmentManager.beginTransaction().replace(R.id.fragment_container,mapFragment).commit();
        // TODO: 2017. 3. 22. 다른 프래그먼트도 이니셜라이즈 하고 한번만 생성하고 사용하게 버튼누를때마다 생성하면 x.
    }

    // Custom font 적용 (각 Activity 마다 복붙해줘야함)
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

}
