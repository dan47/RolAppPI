package com.example.rolapppi.ui.cattle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.Executor;

public class CattleRepository {

    OnFireStoreDataAdded fireStoreDataAdded;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query cattleRef = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).collection("cattle");



    public CattleRepository(OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }

    public void getDataFromFireStore() {

//        cattleRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                if(!queryDocumentSnapshots.isEmpty()){
//                    fireStoreDataAdded.cattleDataAdded(queryDocumentSnapshots.toObjects(CattleModel.class));
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("dd", "dupa", e);
//            }
//        });

        cattleRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    fireStoreDataAdded.cattleDataAdded(task.getResult().toObjects(CattleModel.class));

                } else {

                    fireStoreDataAdded.OnError(task.getException());
                }
            }
        });



    }

    public interface OnFireStoreDataAdded {
        void cattleDataAdded(List<CattleModel> cattleModelList);
        void OnError(Exception e);

    }


}
