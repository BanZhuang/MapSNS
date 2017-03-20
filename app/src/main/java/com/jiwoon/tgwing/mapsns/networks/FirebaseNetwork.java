package com.jiwoon.tgwing.mapsns.networks;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jiwoonwon on 2017. 3. 20..
 */

public class FirebaseNetwork {
    public static DatabaseReference sDatabase = FirebaseDatabase.getInstance().getReference();

    public abstract static class SnapshotListener implements ValueEventListener {
        private static final String TAG = SnapshotListener.class.getSimpleName();

        public abstract void doWithSnapshot(DataSnapshot dataSnapshot);

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            doWithSnapshot(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "onCancelled" + databaseError.toException());

        }
    }
}
