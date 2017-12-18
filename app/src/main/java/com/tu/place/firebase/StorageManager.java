package com.tu.place.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by DONG A on 12/15/2017.
 */

public class StorageManager {
    FirebaseStorage storage;
    String TAG = "StorageManager";

    public StorageManager() {
        storage = FirebaseStorage.getInstance("gs://myplace-715d5.appspot.com/");
    }

    public void uploadFile(String fileName, byte[] data, final Callback callback){
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child(fileName);
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, exception.toString());
                callback.failed();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                callback.success(downloadUrl.toString());
            }
        });
    }

    public interface Callback{
        void success(String url);
        void failed();
    }
}
