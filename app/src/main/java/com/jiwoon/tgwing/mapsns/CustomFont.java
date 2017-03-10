package com.jiwoon.tgwing.mapsns;

import android.app.Application;
import android.os.Bundle;

import com.tsengvn.typekit.Typekit;

/**
 * Created by jiwoonwon on 2017. 3. 10..
 */

public class CustomFont extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Custom Fonts : 나눔바른고딕
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(CustomFont.this, "NanumBarunGothis.ttf"))
                .addBold(Typekit.createFromAsset(CustomFont.this, "NanumBarunGothisBold.ttf"));
    }

    /* 적용법 :

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    를 모든 엑티비티 마지막에 추가 (or BaseAcvity 생성해서 상속)
    */
}
