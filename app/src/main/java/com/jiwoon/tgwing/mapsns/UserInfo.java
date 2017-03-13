package com.jiwoon.tgwing.mapsns;

import java.util.UUID;

/**
 * Created by jiwoonwon on 2017. 3. 13..
 */
// TODO: Model 클래스 생성하기
public class UserInfo {
    private UUID mId;
    private String mName;
    private double Latitude;
    private double Longitude;

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
