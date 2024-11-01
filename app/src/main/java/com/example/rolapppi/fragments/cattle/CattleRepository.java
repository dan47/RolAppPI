package com.example.rolapppi.fragments.cattle;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class CattleRepository {

    OnFireStoreDataAdded fireStoreDataAdded;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query cattleRef = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).collection("cattle");

    public CattleRepository(OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }

    public void loadData(){

        cattleRef.addSnapshotListener((value, error) -> {
            if(error != null){
                Log.e("Firestore error", error.getMessage());
                return;
            }
            List<CattleModel> tempCattleModelList = new ArrayList<>();
            for(DocumentSnapshot dc : value.getDocuments()){
                tempCattleModelList.add(dc.toObject(CattleModel.class));
            }
            fireStoreDataAdded.cattleDataAdded(tempCattleModelList);
        });
    }

    public void addCattle(CattleModel cattleModel) {
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                                        .collection("cattle").document(cattleModel.getAnimal_id()).set(cattleModel);
    }

    public void deleteCattle(CattleModel cattleModel){
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cattle").document(cattleModel.getAnimal_id()).delete();
    }

    public void updateCattleMother(String cattleMotherId, String caliving) {
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cattle").document(cattleMotherId).update("caliving", "", "previousCaliving", caliving);
    }
    public void deleteCalving(String cattleMotherId) {
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cattle").document(cattleMotherId).update("caliving", "" );
    }

    public interface OnFireStoreDataAdded {

        void cattleDataAdded(List<CattleModel> cattleModelList);
    }


}
