package com.example.rolapppi.ui.cattle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.WatchChange;

import java.util.ArrayList;
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


    public void loadData(){

        cattleRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                List<CattleModel> tempCattleModelList = new ArrayList<>();
                for(DocumentSnapshot dc : value.getDocuments()){
                    tempCattleModelList.add(dc.toObject(CattleModel.class));
                }
                fireStoreDataAdded.cattleDataAdded(tempCattleModelList);
            }
        });
    }


    public void addCattle(CattleModel cattleModel) {
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                                        .collection("cattle").document(cattleModel.getAnimal_id()).set(cattleModel);
    }

    public void deleteCattle(CattleModel cattleModel){
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cattle").document(cattleModel.getAnimal_id()).delete();
    }

    public interface OnFireStoreDataAdded {
        void cattleDataAdded(List<CattleModel> cattleModelList);
        void OnError(Exception e);
    }

}
