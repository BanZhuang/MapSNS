package com.jiwoon.tgwing.mapsns.models;

import java.util.UUID;

/**
 * Created by jiwoonwon on 2017. 3. 13..
 */
// TODO: Model 클래스 생성하기
public class UserInfo {
    private UUID mId;
    private String mName;
    private String mMail;
    private double Latitude;
    private double Longitude;

    public UserInfo() {

    }

    public UserInfo(UUID id, String name, String mail) {
        mId = id;
        mName = name;
        mMail = mail;
    }

    public UUID getId() {
        return mId;
    }
    public void setId(UUID id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    public String getMail() {
        return mMail;
    }
    public void setMail(String mail) {
        mMail = mail;
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
}
