package com.jiwoon.tgwing.mapsns.singletons;

import android.util.Log;

import com.jiwoon.tgwing.mapsns.models.UserInfo;

import java.util.List;

public class UserLab {
    private static final String TAG = UserLab.class.getSimpleName();
    private static UserLab sUserLab;

    private UserInfo mUserInfo;
    private String mUserId;

    private UserLab(String userId) {
        mUserId = userId;
    }

    public static UserLab getInstance() {
        return sUserLab;
    }

    public static UserLab getInstance(String userId) {
        if (sUserLab == null) {
            sUserLab = new UserLab(userId);
        }
        return sUserLab;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public String getUserId() {
        Log.d(TAG, "getUserId()" + mUserId);
        return mUserId;
    }

    public void initiateUserId() {
        sUserLab = null;
    }
}
