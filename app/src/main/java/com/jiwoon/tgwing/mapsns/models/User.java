package com.jiwoon.tgwing.mapsns.models;

import java.util.UUID;

/**
 * Created by jiwoonwon on 2017. 3. 13..
 */
public class User {
    // 생성해야 할 멤버변수
    // userName, age, profile, follower, following, latitude, longitude
    private static User sUser; //User 객체를 싱글톤으로 사용

    private static String sUserId;
    private String mUserName;
    private String mAge;
    private String[] mFollowers;
    private String[] mFollowings;
    private double Latitude;
    private double Longitude;

    private User() {} //TODO: LoginActivity 구현하고 이 생성자 삭제
    private User(String userId) {
        sUserId = userId;
    }

    public static User getInstance() {
        if(sUser == null) {
            sUser = new User();
        } //TODO: LoginActivity 구현하고 이 생성자 삭제
        return sUser;
    }

    public static User getInstance(String userId) {
        if(sUser == null) {
            sUser = new User(userId);
        }
        return sUser;
    }

    public String getUserName() {
        return mUserName;
    }
    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getAge() {
        return mAge;
    }
    public void setAge(String age) {
        mAge = age;
    }

    public String[] getFollowers() {
        return mFollowers;
    }
    public void setFollowers(String[] followers) {
        mFollowers = followers;
    }

    public String[] getFollowings() {
        return mFollowings;
    }
    public void setFollowings(String[] followings) {
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

    public void initiateUser() {
        sUser = null;
    }
}
