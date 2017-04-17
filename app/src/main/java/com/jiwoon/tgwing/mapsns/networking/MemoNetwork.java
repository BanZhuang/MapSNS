package com.jiwoon.tgwing.mapsns.networking;

import com.google.firebase.database.DatabaseReference;
import com.jiwoon.tgwing.mapsns.models.Memo;

/**
 * Created by jiwoonwon on 2017. 4. 17..
 */

public class MemoNetwork extends DatabaseNetwork {
    private static final String TAG = "MemoNetwork";

    private static final String FIREBASE_MEMOS = "memos";
    public static DatabaseReference memoReference = sDatabase.child(FIREBASE_MEMOS);

    // Firebase MemoDB 저장
    // DB 내용 : 텍스트, 사진[], 작성자ID, 메모 좌표, 좋아요 누른 유저ID, 지역이름, 공개기간(생성일), 공개범위
    public static void addMemoToFirebase(String text, String writerID, double latitude, double longitude, String locationName
                                            ,String likeUsers) {
        Memo memo = new Memo();
        memo.setMemoInfo(text, writerID, latitude, longitude, locationName, likeUsers);

        String key = memoReference.push().getKey(); //랜덤하게 생성되는 키
        memoReference.child(key).setValue(memo); // Class 올리기
    }

    public void getMemoFromFirebase() {

    }
}
