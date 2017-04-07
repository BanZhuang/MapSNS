package com.jiwoon.tgwing.mapsns.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.models.User;
import com.jiwoon.tgwing.mapsns.networking.UserNetwork;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jiwoonwon on 2017. 3. 27..
 */

public class LoginActivity extends BaseActivity {
    // Firebase
    public FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private FirebaseUser mFirebaseUser;
    // Facebook
    private LoginButton mSigninFacebookButton;
    private CallbackManager mFacebookCallbackManager;
    private AccessToken mAccessToken;

    static final String TAG = LoginActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFacebookCallbackManager = CallbackManager.Factory.create();
        mAccessToken = AccessToken.getCurrentAccessToken();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseAuth = firebaseAuth;
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Log.d(TAG, "LogIn : " + mFirebaseUser.getUid());
                    User mUser = User.getInstance(mFirebaseUser.getUid());

                    UserNetwork.getDataFromFirebase(mUser.getUserId());
                    if(mUser.getAge() != null) {
                        if(mUser.getAge().length() > 0) {
                            Log.d(TAG, "User Info Exist : " + mUser.getUserName());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d(TAG, "User Info Not Fully Exist : " + mUser.getUserName());

                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.d(TAG, "User Info Not Exist");
                        getUserInfoFromFacebook();

                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Log.d(TAG, "LogOut");
                }
            }
        };

        setContentView(R.layout.activity_login);

        mSigninFacebookButton = (LoginButton) findViewById(R.id.sign_in_facebook_button);
        mSigninFacebookButton.setReadPermissions("email", "public_profile");
        mSigninFacebookButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mAccessToken = loginResult.getAccessToken();
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                Log.d(TAG, "Facebook Login Success " + mAccessToken.getToken());

                getUserInfoFromFacebook();

                Log.d(TAG, "UserToken : " + mAccessToken.getToken());
                mFirebaseAuth.signInWithCredential(credential);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "Facebook Login Error", error);
            }
        });
    }

    public void getUserInfoFromFacebook() {
        Log.d(TAG, "get Info From Facebook");
        //Facebook에서 정보 받아오기
        final GraphRequest request = GraphRequest.newMeRequest(mAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    //sUser에 id값 저장, 객체 선언
                    User mUser = User.getInstance(mFirebaseUser.getUid());
                    mUser.setUserName(object.getString("name"));    // 이름
                    mUser.setUserEmail(object.getString("email"));  // 이메일

                    Log.d(TAG, "facebook login > " + "id: " + mUser.getUserId() +
                            ", name: " + mUser.getUserName() + ", email: " + mUser.getUserEmail());
                    //Firebase에 데이터 저장
                    UserNetwork.setUserToFirebase(mFirebaseUser.getUid(), mUser.getUserName(), mUser.getUserEmail());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);

        new Thread(new Runnable() {
            @Override
            public void run() {
                request.executeAndWait();
            }

        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if ( mFirebaseAuthListener != null )
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
