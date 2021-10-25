package com.example.rolapppi.ui.cropProtection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CropProtectionRepository {


    CropProtectionRepository.OnFireStoreDataAdded fireStoreDataAdded;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query cropProtectionRef = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).collection("cropProtection");

    public CropProtectionRepository(CropProtectionRepository.OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }

    public void getDataFromFireStore() {

        cropProtectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    fireStoreDataAdded.cropProtectionDataAdded(task.getResult().toObjects(CropProtectionModel.class));

                } else {

                    fireStoreDataAdded.OnError(task.getException());
                }
            }
        });

    }


    public void loadData(){

        cropProtectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                List<CropProtectionModel> tempCattleModelList = new ArrayList<>();
                for(DocumentSnapshot dc : value.getDocuments()){
                    tempCattleModelList.add(dc.toObject(CropProtectionModel.class));
                }
                fireStoreDataAdded.cropProtectionDataAdded(tempCattleModelList);
            }
        });
    }


    public void addCropProtection(CropProtectionModel cropProtectionModel) {
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cropProtection").document().set(cropProtectionModel);
    }

    public void deleteCropProtection(CropProtectionModel cropProtectionModel){
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cropProtection").document(cropProtectionModel.getId()).delete();
    }

    public void updateCropProtection(CropProtectionModel cropProtectionModel) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("area", cropProtectionModel.getArea());
        result.put("crop", cropProtectionModel.getCrop());
        result.put("dose", cropProtectionModel.getDose());
        result.put("protectionProduct", cropProtectionModel.getProtectionProduct());
        result.put("reason", cropProtectionModel.getReason());
        result.put("treatmentTime", cropProtectionModel.getTreatmentTime());
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cropProtection").document(cropProtectionModel.getId()).update(result);
    }

    public interface OnFireStoreDataAdded {
        void cropProtectionDataAdded(List<CropProtectionModel> cropProtectionModels);
        void OnError(Exception e);
    }

}
