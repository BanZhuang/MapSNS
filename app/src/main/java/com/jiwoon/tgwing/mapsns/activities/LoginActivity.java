package com.jiwoon.tgwing.mapsns.activities;

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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.models.UserInfo;
import com.jiwoon.tgwing.mapsns.singletons.UserLab;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by jiwoonwon on 2017. 3. 18..
 */

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";

    private CallbackManager mCallbackManager;
    private AccessToken mAccessToken;
    private UserInfo mUserInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(this.getApplicationContext()); <- 이제는 자동으로 선언되서 필요없음
        mCallbackManager = CallbackManager.Factory.create();
        mUserInfo = UserLab.getInstance().getUserInfo();

        mAccessToken = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "AccessToken : " + mAccessToken);
        if(mAccessToken != null) {
            Log.d(TAG, "User ID : " + mAccessToken.getUserId() + " / Permissions : " + mAccessToken.getPermissions());
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                Profile profile = Profile.getCurrentProfile();

                //Facebook에서 Email 가져오기
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v(TAG,"response : " + response.toString());

                        try {
                            String userName = object.getString("name");
                            String userEmail = object.getString("email");

                            //싱글톤에 이름,메일 저장
                            mUserInfo.setName(userName);
                            mUserInfo.setMail(userEmail);
                            Log.v("Email = ", " " + userEmail);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email");
                request.setParameters(parameters);
                request.executeAsync();

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
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
                        Arrays.asList("public_profile", "email", "user_friends"));
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
