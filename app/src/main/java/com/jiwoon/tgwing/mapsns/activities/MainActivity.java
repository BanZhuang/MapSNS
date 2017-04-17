package com.jiwoon.tgwing.mapsns.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.fragments.MapFragment;
import com.jiwoon.tgwing.mapsns.fragments.ProfileFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    public static final String CUR_FRAGMENT = "curFragment";

    public static final String MAP_FRAGMENT = "MapFragment";
    public static final String CREATE_MEMO_FRAGMENT = "CreateMemoFragment";
    private static final String PROFILE_FRAGMENT = "ProfileFragment";

    public ImageView buttonNavigationMap;
    public ImageView buttonCreateMemo;
    public ImageView buttonProfile;

    private String curFragment;

    private MapFragment mapFragment;
    private ProfileFragment profileFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = new MapFragment();
        profileFragment = new ProfileFragment();

        initFragment();

        buttonNavigationMap = (ImageView) findViewById(R.id.navigation_map);
        buttonNavigationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!curFragment.equals(MAP_FRAGMENT)) {
                    // 현 상태가 MAP_FRAGMENT임을 전달
                    Bundle bundle = new Bundle();
                    bundle.putString(CUR_FRAGMENT, MAP_FRAGMENT);
                    mapFragment.setArguments(bundle);

                    // 프레그먼트 교체하기
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, mapFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    curFragment = MAP_FRAGMENT;
                }

                // 백그라운드 날려버리기
                if(fragmentManager.getBackStackEntryCount() > 1) {
                    fragmentManager.popBackStack();
                    Log.d(TAG, "background popped");
                }
            }
        });

        buttonCreateMemo = (ImageView) findViewById(R.id.navigation_create_memo);
        buttonCreateMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CreateMemo Fragment를 따로 만들기 보단 MapFragment를 불러다 메모를 생성하는 창을 띄워주자
                // 현 상태가 CREATE_MEMO_FRAGMENT임을 전달
                Bundle bundle = new Bundle();
                bundle.putString(CUR_FRAGMENT, CREATE_MEMO_FRAGMENT);
                mapFragment.setArguments(bundle);

                // TODO: 2017. 4. 17. 현 프레그먼트가 MapFragment일때 Replace 말고 Bundle만 전달하여 MapFragment에서 이를 들을 수 있게 코드수정
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mapFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                curFragment = MAP_FRAGMENT;

                // 백그라운드 날려버리기
                if(fragmentManager.getBackStackEntryCount() > 1) {
                    fragmentManager.popBackStack();
                    Log.d(TAG, "background popped");
                }
            }
        });

        buttonProfile = (ImageView) findViewById(R.id.navigation_profile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!curFragment.equals(PROFILE_FRAGMENT)) {
                    // 프레그먼트 교체하기
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, profileFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    curFragment = PROFILE_FRAGMENT;
                }

                // 백그라운드 날려버리기
                if(fragmentManager.getBackStackEntryCount() > 1) {
                    fragmentManager.popBackStack();
                    Log.d(TAG, "background popped");
                }
            }
        });
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,mapFragment).commit();
        // TODO: 2017. 3. 22. 다른 프래그먼트도 이니셜라이즈 하고 한번만 생성하고 사용하게 버튼누를때마다 생성하면 x.
        curFragment = MAP_FRAGMENT;
    }
}
