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
    public static Boolean GET_FIREBASE_DATA;

    // Firebase로 유저DB 저장
    // 데이터 순서 : UserID (FirebaseUser Uid), UserName, Email, Age, Followers, Followings
    public static void setUserToFirebase(String userID, String userName, String email, String age, String followers, String followings) {
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_USERNAME).setValue(userName);
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_EMAIL).setValue(email);
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_AGE).setValue(age);
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_FOLLOWERS).setValue(followers);
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_FOLLOWINGS).setValue(followings);
        User user = User.getInstance(userID);
        user.setUserInfo(userName, email, age, followers, followings);

        sDatabase.child(FIREBASE_USERS).child(userID).setValue(user);

        Log.d(TAG, "Data Stored : " + userID);
    }

    public static void setUserToFirebase(String userID, String userName, String email) {
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_USERNAME).setValue(userName);
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_EMAIL).setValue(email);
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_AGE).setValue("");
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_FOLLOWERS).setValue("");
//        sDatabase.child(FIREBASE_USERS).child(userID).child(FIREBASE_FOLLOWINGS).setValue("");
        User user = User.getInstance(userID);
        user.setUserInfo(userName, email, "", "", "");

        sDatabase.child(FIREBASE_USERS).child(userID).setValue(user);

        Log.d(TAG, "Data Stored : " + userID);
    }

    public static void getDataFromFirebase(final String userID) {
        GET_FIREBASE_DATA = false;

        UserNetwork.sDatabase.child(UserNetwork.FIREBASE_USERS).child(userID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
//                            String userName = dataSnapshot.child(UserNetwork.FIREBASE_USERNAME).getValue().toString();
//                            String email = dataSnapshot.child(UserNetwork.FIREBASE_EMAIL).getValue().toString();
//                            String age = dataSnapshot.child(UserNetwork.FIREBASE_AGE).getValue().toString();
//                            String followers = dataSnapshot.child(UserNetwork.FIREBASE_FOLLOWERS).getValue().toString();
//                            String followings = dataSnapshot.child(UserNetwork.FIREBASE_FOLLOWINGS).getValue().toString();
                            User user = dataSnapshot.getValue(User.class);
                            String userName = user.getUserName();
                            String email = user.getUserEmail();
                            String age = user.getAge();
                            String followers = user.getFollowers();
                            String followings = user.getFollowings();

                            User.getInstance(userID).setUserInfo(userName, email, age, followers, followings);
                            Log.d(TAG, "userName : " + User.getInstance(userID).getUserName());

                            GET_FIREBASE_DATA = true;
                        } catch (Exception e) {
                            Log.e(TAG, "data not fully exist");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }
}
