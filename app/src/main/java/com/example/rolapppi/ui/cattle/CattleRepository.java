package com.example.rolapppi.ui.cattle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CattleRepository {

    OnFireStoreDataAdded fireStoreDataAdded;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query cattleRef = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).collection("cattle");



    public CattleRepository(OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }

    public void getDataFromFireStore() {

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
