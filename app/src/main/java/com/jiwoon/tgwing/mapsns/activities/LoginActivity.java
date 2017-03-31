package com.jiwoon.tgwing.mapsns.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;


/**
 * Created by jiwoonwon on 2017. 3. 27..
 */

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    private User mUser;
    //Facebook
    private AccessToken accessToken;
    private CallbackManager mCallbackManager;
    //Firebase

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "AccessToken : " + accessToken);

        if(accessToken != null) {
            mUser = User.getInstance(accessToken.getUserId());
            //TODO::firebase DB 체크해서 유저정보 있는지 확인
                //User정보 있으면 User모델에 정보 파싱하고 MainActivity로
                //없으면 Register로
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setPublishPermissions(Arrays.asList("public_profile", "email", "read_custom_friendlists"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Login Success : " + loginResult.getAccessToken().toString());

                //Facebook에서 정보 받아오기
               GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    //sUser에 id값 저장, 객체 선언
                                    mUser = User.getInstance(object.getString("id"));
                                    mUser.setUserName(object.getString("lastname") + object.getString("firstname"));  //이름
                                    mUser.setUserEmail(object.getString("email"));//이메일

                                    Log.d(TAG, "facebook login : " + "id: " + mUser.getUserId() +
                                            ", name: " + mUser.getUserName() + ", email: " + mUser.getUserEmail());

                                    //RegisterActivity로!
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }),
                        GraphRequest.newMyFriendsRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray objects, GraphResponse response) {
                                try {
                                    // *친구목록* 받아올 수 있는 모듈!
                                    // 나중에 쓸 수도 있을거 같아서 추가함
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                );

                batch.addCallback(new GraphRequestBatch.Callback() {
                    @Override
                    public void onBatchCompleted(GraphRequestBatch graphRequests) {
                        // Application code for when the batch finishes
                    }
                });
                batch.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.toString());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
