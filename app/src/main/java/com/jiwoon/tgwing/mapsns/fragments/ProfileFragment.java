package com.jiwoon.tgwing.mapsns.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.activities.LoginActivity;

/**
 * Created by jiwoonwon on 2017. 4. 14..
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    @Override
    public void onCreate(Bundle savedInsatnceState) { super.onCreate(savedInsatnceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 로그아웃 버튼
        Button logoutBtn = (Button) view.findViewById(R.id.sign_out_facebook_button);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // Firebase 로그아웃
                LoginManager.getInstance().logOut(); // Facebook 로그아웃
                Log.d(TAG, "User signed_out");


                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
