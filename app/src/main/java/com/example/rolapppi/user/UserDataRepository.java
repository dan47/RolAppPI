package com.example.rolapppi.user;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rolapppi.fragments.feed.FeedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserDataRepository {

    OnFireStoreDataAdded fireStoreDataAdded;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public UserDataRepository(OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }


    public void loadData() {

        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserData tempUserData = new UserData();
                        tempUserData.setFarmId(document.getString("farm_id"));
                        Log.d("Firestore", "Farm ID: " + tempUserData.getFarmId());
                        fireStoreDataAdded.userDataAdded(tempUserData);
                    } else {
                        Log.d("Firestore", "No such document");
                        Log.d("Firestore", FirebaseAuth.getInstance().getUid());
                    }
                } else {
                    Log.d("Firestore", "Error getting document: ", task.getException());
                }
            }
        });

    }


    public void updateUserData(UserData userData) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("farm_id", userData.getFarmId());

        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).update(result);
    }

    public interface OnFireStoreDataAdded {
        void userDataAdded(UserData UserData);
    }
}
