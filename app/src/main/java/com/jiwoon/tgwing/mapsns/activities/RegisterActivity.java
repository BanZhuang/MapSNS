package com.jiwoon.tgwing.mapsns.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.jiwoon.tgwing.mapsns.models.UserInfo;
import com.jiwoon.tgwing.mapsns.singletons.UserLab;
import com.jiwoon.tgwing.mapsns.networks.UserNetwork;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by jiwoonwon on 2017. 3. 19..
 */

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mFirebaseUser;
    private UserInfo mUserInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "accessToken: " + accessToken);
        mAuth = FirebaseAuth.getInstance();
        mUserInfo = UserLab.getInstance().getUserInfo();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();

                if(mFirebaseUser != null) {
                    //User Signed In
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mFirebaseUser.getUid());
                    final String existingUserID = mFirebaseUser.getUid();

                    UserNetwork.sDatabase.child(UserNetwork.FIREBASE_USERS).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean userExists = false;

                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String userId = userSnapshot.getKey();
                                Log.d(TAG, "onDataChange:userId:" + userId);
                                if (userId.equals(existingUserID)) {
                                    Log.d(TAG, "Existing User is Found");
                                    userExists = true;
                                    UserLab.getInstance(existingUserID);
                                    moveToMainActivity();
                                }
                            }

                            if (userExists == false) {
                                Log.d(TAG, "Existing User is not Found");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    //User Signed out
                    Toast.makeText(RegisterActivity.this, "User Signed out", Toast.LENGTH_SHORT).show();
                }
            }
        };

        ViewGroup buttonStart = (ViewGroup) findViewById(R.id.button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText mUsernameEditText = (EditText) findViewById(R.id.activity_register_username);
                final EditText mAgeEditText = (EditText) findViewById(R.id.activity_register_age);
                mUsernameEditText.setText(mUserInfo.getName());

                String userEmail = mUserInfo.getMail();
                String userId = mFirebaseUser.getUid();
                String userName = mUsernameEditText.getText().toString();
                String age = mAgeEditText.getText().toString();
                String[] userFollowing = {}; //비어있는 Array 생성

                UserNetwork.setUserInfoToFirebase(userEmail, userId, userName, age, userFollowing);
                moveToMainActivity();
            }
        });
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void handleFacebookAccessToken(AccessToken accessToken) {
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
