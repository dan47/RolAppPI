package com.example.rolapppi.ui.cattle.customCattle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rolapppi.ui.cattle.CattleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCattleRepository {
    OnFireStoreDataAdded fireStoreDataAdded;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query cattleRef = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).collection("customCattle");
    CollectionReference cattleCollection = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
            .collection("customCattle");

    public CustomCattleRepository(OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }

    public void getDataFromFireStore() {

        cattleRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, List<CattleModel>> temp = new HashMap<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("DIPA", document.getId() + " => " + document.getData());


                        List<CattleModel> cattleModelList = new ArrayList<>();
                        document.getReference().collection("customCattle").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                                    cattleModelList.add(querySnapshot.toObject(CattleModel.class));
                                }
                            }
                        });
                        temp.put(document.getId(), (List<CattleModel>) cattleModelList);


                    }
                    fireStoreDataAdded.customCattleDataAdded(temp);
                } else {
                    fireStoreDataAdded.OnError(task.getException());
                    Log.d("DIPA", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    public void loadData() {

        cattleRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }



                    Map<String, List<CattleModel>> temp = new HashMap<>();
                for(DocumentSnapshot dc : value.getDocuments()){

                    List<CattleModel> cattleModelList = new ArrayList<>();
                    dc.getReference().collection("customCattle").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null){
                                Log.e("Firestore error", error.getMessage());
                                return;
                            }
                            for(DocumentSnapshot dc : value.getDocuments()){
                                cattleModelList.add(dc.toObject(CattleModel.class));
                            }
                        }
                    });
                    temp.put(dc.getId(), (List<CattleModel>) cattleModelList);


                }

                    fireStoreDataAdded.customCattleDataAdded(temp);

            }
        });

    }


    public void addCustomCattle(List<CattleModel> cattleModel, String name) {
        cattleCollection.document(name).set(new HashMap<>());
        for (CattleModel model : cattleModel) {
            cattleCollection.document(name).collection("customCattle").document(model.getAnimal_id()).set(model);
        }
    }

    public void addCustomCattle2(Map<String,List<CattleModel>> cattleModelLists) {


    }

    public void deleteCustomCattle(List<CattleModel> cattleModel, String name) {
        for (CattleModel model : cattleModel) {
            FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                    .collection("customCattle").document(name).collection("customCattle").document(model.getAnimal_id()).delete();
        }
    }


    public interface OnFireStoreDataAdded {
        void customCattleDataAdded(Map<String, List<CattleModel>> cattleModelList);

        void OnError(Exception e);
    }
}
