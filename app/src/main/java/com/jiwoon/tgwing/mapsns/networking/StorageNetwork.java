package com.jiwoon.tgwing.mapsns.networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by jiwoonwon on 2017. 4. 17..
 */

public class StorageNetwork {
    private static final String TAG = StorageNetwork.class.getSimpleName();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference sReference = storage.getReferenceFromUrl("gs://mpasns-f94a6.appspot.com/");

    private static final String STORAGE_USERS = "uesrs";
    private static final String STORAGE_MEMOS = "memos";

    public static void uploadProfileImage(String userID, String fileName, ImageView profileImage) {
        StorageReference profileRef = sReference.child(STORAGE_USERS + "/" + userID  + "/" + fileName);

        // Firebase Storage에 올릴 수 있도록 profileImage 변환
        profileImage.setDrawingCacheEnabled(true);
        profileImage.buildDrawingCache();
        Bitmap bitmap = profileImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profileRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "profile upload complete : " + taskSnapshot.getDownloadUrl());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "profile upload failed : " + e);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        });
    }

    public static void downloadProfileImage(String userID, String filename, final ImageView profileImage) {
        StorageReference islandRef = sReference.child(STORAGE_USERS + "/" + userID + "/" + filename);
        final long ONE_MEGABYTE = 400 * 400;
        islandRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profileImage.setImageBitmap(bitmap);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "DownloadImageHandler:" + exception.getMessage());
                    }
                });
    }

    public static void uploadMemoImage(String memoID, String fileName, List<Bitmap> memoImage) {
        StorageReference memoRef = sReference.child(STORAGE_MEMOS + "/" + memoID + "/" + fileName);

        // TODO: 2017. 4. 17. List 파일 업로드하는 코드 만들기
    }
}
