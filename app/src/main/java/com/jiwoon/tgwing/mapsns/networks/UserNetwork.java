package com.jiwoon.tgwing.mapsns.networks;

import android.util.Log;

import com.jiwoon.tgwing.mapsns.networks.FirebaseNetwork;

/**
 * Created by jiwoonwon on 2017. 3. 20..
 */

public class UserNetwork extends FirebaseNetwork {
    private static final String TAG = "UserNetwork";

    public static final String FIREBASE_USERS = "users";
    public static final String FIREBASE_USERNAME = "username";
    public static final String FIREBASE_AGE = "age";
    public static final String FIREBASE_FRIENDS = "friends";

    public static void setUserInfoToFirebase(String userId, String userName, String age, String[] friends) {
        sDatabase.child(FIREBASE_USERS).child(userId).child(FIREBASE_USERNAME).setValue(userName);
        sDatabase.child(FIREBASE_USERS).child(userId).child(FIREBASE_AGE).setValue(age);
        sDatabase.child(FIREBASE_USERS).child(userId).child(FIREBASE_FRIENDS).setValue(friends);
        Log.d(TAG, "User info has saved || userName : " + userName + ", age : " + age + ", friends : " + friends);
    }
}
