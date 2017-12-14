package com.tu.place.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by NhaKhoaPaRis on 12/7/2017.
 */

public class FirebaseManager {

    FirebaseDatabase database;
    String TAG = "FirebaseManager";

    public FirebaseManager() {
        database = FirebaseDatabase.getInstance();
    }

    public void getRef(String ref, final Callback callback){
        // Read from the database
        database.getReference(ref).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                java.util.ArrayList value = dataSnapshot.getValue(java.util.ArrayList.class);
//                Log.d(TAG, "Value is: " + value.get(0).toString());
                callback.success(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                callback.failed();
            }
        });
    }

    public void  writeRef(String ref, String key, Object value, final Callback callback){
        database.getReference(ref).child(key).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.success(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.failed();
            }
        });
    }

    public interface Callback{
        void success(DataSnapshot dataSnapshot);
        void failed();
    }
}
