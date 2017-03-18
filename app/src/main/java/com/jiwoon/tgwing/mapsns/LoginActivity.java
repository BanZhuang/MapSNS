package com.jiwoon.tgwing.mapsns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by jiwoonwon on 2017. 3. 18..
 */

public class LoginActivity extends AppCompatActivity{

    public static final String TAG = "LoginActivity";

    private CallbackManager mCallbackManager;
    private AccessToken mAccessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(this.getApplicationContext()); <- 이제는 자동으로 선언되서 필요없음
        mCallbackManager = CallbackManager.Factory.create();

        mAccessToken = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "AccessToken : " + mAccessToken);
        if(mAccessToken != null) {
            //TODO: 다른 정보가 존재하면 MainActivity에, 없으면 RegisterActivity에
            Log.d(TAG, "User ID : " + mAccessToken.getUserId() + " / Permissions : " + mAccessToken.getPermissions());

        }

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //TODO: 다른 정보가 존재하면 MainActivity에, 없으면 RegisterActivity에
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook Login has Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, error.getMessage());
            }
        });

        setContentView(R.layout.activity_login);

        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "user_friends"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Activity Stopped");
    }
}
