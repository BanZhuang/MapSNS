package com.jiwoon.tgwing.mapsns.singletons;

import android.util.Log;

public class UserLab {
    private static final String TAG = UserLab.class.getSimpleName();
    private static UserLab sUserLab;
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

    public String getUserId() {
        Log.d(TAG, "getUserId()" + mUserId);
        return mUserId;
    }

    public void initiateUserId() {
        sUserLab = null;
    }
}
