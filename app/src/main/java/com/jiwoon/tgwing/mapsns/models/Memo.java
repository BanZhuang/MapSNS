package com.jiwoon.tgwing.mapsns.models;

import java.util.Calendar;

/**
 * Created by jiwoonwon on 2017. 4. 17..
 */

public class Memo {
    private String mText; //작성한 글 내용
    private String mWriterID; //작성자 ID

    private double Latitide; //경도
    private double Longitude; //위도
    private String LocationName; //지역이름

    private String mLikeUsers; //좋아요 누른 유저들

    private String mCreatedTime = null; //메모가 생성된 시각

    public Memo() {} // Default Constructor
    public void setMemoInfo(String text, String writerID, double latitide, double longitude,
                            String locationName, String likeUsers) {
        mText = text; mWriterID = writerID; Latitide = latitide; Longitude = longitude;
        LocationName = locationName; mLikeUsers = likeUsers;
        setCreatedTime();
    }

    public String getText() {
        return mText;
    }
    public void setText(String text) {
        mText = text;
    }

    public String getWriterID() {
        return mWriterID;
    }
    public void setWriterID(String writerID) {
        this.mWriterID = writerID;
    }

    public double getLatitide() {
        return Latitide;
    }
    public void setLatitide(double latitide) {
        this.Latitide = latitide;
    }

    public double getLongitude() {
        return Longitude;
    }
    public void setLongitude(double longitude) {
        this.Longitude = longitude;
    }

    public String getLocationName() {
        return LocationName;
    }
    public void setLocationName(String locationName) {
        this.LocationName = locationName;
    }

    public String getLikeUsers() {
        return mLikeUsers;
    }
    public void setLikeUsers(String likeUsers) {
        this.mLikeUsers = likeUsers;
    }

    public String getCreatedTime() {
        return mCreatedTime;
    }

    private void setCreatedTime() {
        if(mCreatedTime == null) {
            // 현재 시각 불러오기
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            mCreatedTime = String.valueOf(year) + String.valueOf(month) + String.valueOf(day) +
                    String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second);
        }
    }
}
