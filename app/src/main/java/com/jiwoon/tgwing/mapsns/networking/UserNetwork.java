package com.jiwoon.tgwing.mapsns.networking;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jiwoon.tgwing.mapsns.models.User;

import java.util.List;

/**
 * Created by jiwoonwon on 2017. 3. 31..
 */

public class UserNetwork extends DatabaseNetwork {
    private static final String TAG = "UserNetwork";

    private static final String FIREBASE_USERS = "users";
    public static DatabaseReference userReference = sDatabase.child(FIREBASE_USERS);

    // Firebase로 유저DB 저장
    // 데이터 순서 : UserID (FirebaseUser Uid), UserName, Email, Age, Followers, Followings
    public static void updateUserToFirebase(String userID, String userName, String email, String age,
                                            List<String> followers, List<String> followings) {
        User user = User.getInstance(userID);
        user.setUserInfo(userName, email, age, followers, followings);

        userReference.child(userID).setValue(user);

        Log.d(TAG, "Data Updated : " + userID);
    }

    // Facebook
    public static void addUserToFirebase(String userID, String userName, String email) {
        User user = User.getInstance(userID);
        user.setUserInfo(userName, email, null, null, null);

        userReference.child(userID).setValue(user);

        Log.d(TAG, "Data Stored : " + userID);
    }
}
