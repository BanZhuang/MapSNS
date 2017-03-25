package com.jiwoon.tgwing.mapsns.networks;

import android.util.Log;

/**
 * Created by jiwoonwon on 2017. 3. 20..
 */

public class UserNetwork extends FirebaseNetwork {
    private static final String TAG = "UserNetwork";

    public static final String FIREBASE_USERS = "users";
    public static final String FIREBASE_USERNAME = "username";
    public static final String FIREBASE_AGE = "age";
    public static final String FIREBASE_FOLLOWING = "following";

    public static void setUserInfoToFirebase(String userEmail,String userId, String userName, String age, String[] following) {
        sDatabase.child(FIREBASE_USERS).child(userEmail).child(FIREBASE_USERNAME).setValue(userId);
        sDatabase.child(FIREBASE_USERS).child(userEmail).child(FIREBASE_USERNAME).setValue(userName);
        sDatabase.child(FIREBASE_USERS).child(userEmail).child(FIREBASE_AGE).setValue(age);
        sDatabase.child(FIREBASE_USERS).child(userEmail).child(FIREBASE_FOLLOWING).setValue(following);
        Log.d(TAG, "User info has saved || userName : " + userName + ", age : " + age + ", following : " + following);
    }
}
