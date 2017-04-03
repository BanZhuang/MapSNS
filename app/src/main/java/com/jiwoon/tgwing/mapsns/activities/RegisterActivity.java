package com.jiwoon.tgwing.mapsns.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jiwoon.tgwing.mapsns.R;
import com.jiwoon.tgwing.mapsns.models.User;
import com.jiwoon.tgwing.mapsns.networking.UserNetwork;

/**
 * Created by jiwoonwon on 2017. 4. 3..
 */
// 이 클래스에서는 facebook 로그인은 되있지만 firebase에 정보가 없는 유저의 정보를 입력받고 이를 전달하는 역할을 함
public class RegisterActivity extends AppCompatActivity {
    // TODO: 1. 정보를 성공적으로 입력하면 User 에서 Firebase로 데이터 전달
    private static final String TAG = "RegisterActivity";

    // Facebook
    private AccessToken accessToken;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mFirebaseUser; // Firebase User
    private User mUser; //로컬 User

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = User.getInstance();
        accessToken = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "accessToken: " + mUser.getUserId());
        mAuth = FirebaseAuth.getInstance(); // Auth 정보 받아오기

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = mAuth.getCurrentUser(); // Facebook이랑 연동되어있음

                if (mFirebaseUser != null) {
                    final String loginedUserID = mUser.getUserId(); // Facebook UserId
                    Log.d(TAG, "onAuthStateChanged : Sign_In : " + loginedUserID);

                    // Firebase에서 User정보 불러오기
                    UserNetwork.sDatabase.child(UserNetwork.FIREBASE_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean userExist = false;
                            // 유저정보 검색
                            for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String userID = userSnapshot.getKey();
                                Log.d(TAG, "currentData : " + userID);
                                if(userID.equals(loginedUserID)) {
                                    userExist = true;
                                    Log.d(TAG, "User Found : " + loginedUserID);
                                    getDataFromFirebase();  //Firebase에서 정보 받아오기
                                    moveToMainActivity();
                                }
                            }

                            if (userExist == false) {
                                Log.d(TAG, "User not found");
                                // TODO: updateUI(); 정보 입력받고 파이어베이스, 디비에 저장
                                updateUI();
                                moveToMainActivity();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Log.d(TAG, "onAuthStateChanged : Sign_Out");
                    //TODO: 원래 없어야 하는 코드 ㅠㅠ Firebase에 정보가 안가 ㅠㅠㅠ
                    moveToMainActivity();
                }
            }
        };

        handleFacebookAccessToken(accessToken);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateUI() {
        Log.d(TAG, "updateUI");
        setContentView(R.layout.activity_register);
        UserNetwork.setUserToFirebase(mUser.getUserId(), mUser.getUserName(), mUser.getUserEmail(), mUser.getAge()
        , mUser.getFollowers(), mUser.getFollowings());
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getDataFromFirebase() {
        String userName = UserNetwork.sDatabase.child(UserNetwork.FIREBASE_USERS).child(mUser.getUserId())
                .child(UserNetwork.FIREBASE_USERNAME).getKey();
        String email = UserNetwork.sDatabase.child(UserNetwork.FIREBASE_USERS).child(mUser.getUserId())
                .child(UserNetwork.FIREBASE_EMAIL).getKey();
        String age = UserNetwork.sDatabase.child(UserNetwork.FIREBASE_USERS).child(mUser.getUserId())
                .child(UserNetwork.FIREBASE_AGE).getKey();
        String followers = UserNetwork.sDatabase.child(UserNetwork.FIREBASE_USERS).child(mUser.getUserId())
                .child(UserNetwork.FIREBASE_FOLLOWERS).getKey();
        String followings = UserNetwork.sDatabase.child(UserNetwork.FIREBASE_USERS).child(mUser.getUserId())
                .child(UserNetwork.FIREBASE_FOLLOWINGS).getKey();
        mUser.setUserInfo(userName, email, age, followers, followings);
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookAccessToken");

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Log.d(TAG, "signInWithCredential Succeeded");
                }
            }
        });
    }
}
