package com.jiwoon.tgwing.mapsns.networking;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by jiwoonwon on 2017. 3. 31..
 */

public class UserNetwork extends DatabaseNetwork {
    private static final String TAG = "UserNetwork";

    public static final String FIREBASE_USERS = "users";
    public static final String FIREBASE_USERNAME = "username";
    public static final String FIREBASE_EMAIL = "email";
    public static final String FIREBASE_AGE = "age";
    public static final String FIREBASE_FOLLOWERS = "followers"; // ","로 유저 구분!
    public static final String FIREBASE_FOLLOWINGS = "followings"; // ","로 유저 구분!

    // Firebase로 유저DB 저장
    // 데이터 순서 : UserID, UserName, Email, Age, Followers, Followings
    public static void setUserToFirebase(String userID, String userName, String email, String age, String followers, String followings) {
        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_USERNAME).setValue(userName);
        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_EMAIL).setValue(email);
        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_AGE).setValue(age);
        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_FOLLOWERS).setValue(followers);
        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_FOLLOWINGS).setValue(followings);
        Log.d(TAG, "Data Stored : " + userID);
    }
}
