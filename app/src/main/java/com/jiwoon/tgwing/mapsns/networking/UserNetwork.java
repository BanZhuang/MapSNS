package com.jiwoon.tgwing.mapsns.networking;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jiwoon.tgwing.mapsns.models.User;

/**
 * Created by jiwoonwon on 2017. 3. 31..
 */

public class UserNetwork extends DatabaseNetwork {
    private static final String TAG = "UserNetwork";

    public static final String FIREBASE_USERS = "users";

    // Firebase로 유저DB 저장
    // 데이터 순서 : UserID (FirebaseUser Uid), UserName, Email, Age, Followers, Followings
    public static void setUserToFirebase(String userID, String userName, String email, String age, String followers, String followings) {
        User user = User.getInstance(userID);
        user.setUserInfo(userName, email, age, followers, followings);

        sDatabase.child(FIREBASE_USERS).child(userID).setValue(user);

        Log.d(TAG, "Data Stored : " + userID);
    }

    public static void setUserToFirebase(String userID, String userName, String email) {
        User user = User.getInstance(userID);
        user.setUserInfo(userName, email, "", "", "");

        sDatabase.child(FIREBASE_USERS).child(userID).setValue(user);

        Log.d(TAG, "Data Stored : " + userID);
    }
}
