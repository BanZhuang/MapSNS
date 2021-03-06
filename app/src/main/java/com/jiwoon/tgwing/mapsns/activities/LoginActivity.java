package com.jiwoon.tgwing.mapsns.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.models.User;
import com.jiwoon.tgwing.mapsns.networking.StorageNetwork;
import com.jiwoon.tgwing.mapsns.networking.UserNetwork;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;

/**
 * Created by jiwoonwon on 2017. 3. 27..
 */

public class LoginActivity extends BaseActivity {
    // Firebase
    public FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private FirebaseUser mFirebaseUser;
    // User Info
    private User mUser;
    private String userID;
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
                mFirebaseUser = mFirebaseAuth.getCurrentUser();

                if (mFirebaseUser != null) {
                    // Login 되었을 때
                    userID = mFirebaseUser.getUid();
                    mUser = User.getInstance(userID);
                    Log.d(TAG, "LogIn : " + userID);

                    // FIREBASE에서 데이터 가져오기
                    UserNetwork.userReference.child(userID).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class); //임시 User객체

                                if(user == null) {
                                    Log.d(TAG, "User Info Not Exist");
                                    getUserInfoFromFacebook();

                                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String userName = user.getUserName();   //이름
                                    String email = user.getUserEmail();     //이메일
                                    try {
                                        String age = user.getAge();         //나이
                                        List<String> followers = user.getFollowers();   //팔로워
                                        List<String> followings = user.getFollowings(); //팔로잉
                                    } catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                    }

                                    mUser.copyInfo(user);
                                    Log.d(TAG, "userName : " + User.getInstance(userID).getUserName());

                                    if (mUser.getAge().length() > 0) {
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
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        }
                    );
                }
                else {
                    // Logout 되었을 때
                    setContentView(R.layout.activity_login);
                    Log.d(TAG, "LogOut");

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

                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            startActivity(intent);
                            finish();
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
            }
        };
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
                    String userName = object.getString("name");    // 이름
                    String userEmail = object.getString("email");  // 이메일
                    // 프로필 이미지
                    // TODO: 2017. 4. 17. 여기를 안거치고 통과함...!
                    if(object.has("picture")) {
                        String profileURL = object.getJSONObject("picture").getJSONObject("data").getString("url"); //사진 URL
                        Log.d(TAG, "profile URL : " + profileURL);
                        URL url = new URL(profileURL);

                        Bitmap profilePic = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        StorageNetwork.uploadProfileImage(mFirebaseUser.getUid(), StorageNetwork.IMAGE_SIZE_ORIGINAL, profilePic);
                    }

                    Log.d(TAG, "facebook login > " + "id: " + mUser.getUserId() +
                            ", name: " + userName + ", email: " + userEmail);
                    //Firebase에 데이터 저장
                    UserNetwork.addUserToFirebase(mFirebaseUser.getUid(), userName, userEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(150).height(150)");
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
