package com.jiwoon.tgwing.mapsns.models;

import java.util.UUID;

/**
 * Created by jiwoonwon on 2017. 3. 13..
 */
public class User {
    // 생성해야 할 멤버변수
    // userName, age, profile, follower, following, latitude, longitude
    private static User sUser; //User 객체를 싱글톤으로 사용

    private String sUserId;  // Facebook ID
    private String mUserName;

    private String mUserEmail;
    private String mAge;
    private String mFollowers;
    private String mFollowings;
    private double Latitude;
    private double Longitude;

    private User() {}
    private User(String userId) {
        sUserId = userId;
    }

    public static User getInstance() {
        return sUser;
    }

    public static User getInstance(String userId) {
        if(sUser == null) {
            sUser = new User(userId);
        }
        return sUser;
    }
    public void copyInfo(User user) {
        this.mUserName = user.mUserName;
        this.mUserEmail = user.mUserEmail;
        this.mAge = user.mAge;
        this.mFollowers = user.mFollowers;
        this.mFollowings = user.mFollowings;
    }

    public String getUserId() { return sUserId; }

    public String getUserName() {
        return mUserName;
    }
    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserEmail() {
        return mUserEmail;
    }
    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public String getAge() {
        return mAge;
    }
    public void setAge(String age) {
        mAge = age;
    }

    public String getFollowers() {
        return mFollowers;
    }
    public void setFollowers(String followers) {
        mFollowers = followers;
    }

    public String getFollowings() {
        return mFollowings;
    }
    public void setFollowings(String followings) {
        mFollowings = followings;
    }

    public double getLatitude() {
        return Latitude;
    }
    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }
    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setUserInfo(String userName, String userEmail, String age, String followers, String followings) {
        mUserName = userName; mUserEmail = userEmail; mAge = age; mFollowers = followers; mFollowings = followings;
    }

    // 싱글톤 날리기
    public void initiateUser() {
        sUser = null;
    }
}
