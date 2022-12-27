package com.example.rolapppi.fragments.cropProtection;

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

public class CropProtectionRepository {


    CropProtectionRepository.OnFireStoreDataAdded fireStoreDataAdded;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query cropProtectionRef = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).collection("cropProtection");

    public CropProtectionRepository(CropProtectionRepository.OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }


    public void loadData(){

        cropProtectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                List<CropProtectionModel> tempCropProtectionModelList = new ArrayList<>();
                for(DocumentSnapshot dc : value.getDocuments()){
                    tempCropProtectionModelList.add(dc.toObject(CropProtectionModel.class));
                }
                fireStoreDataAdded.cropProtectionDataAdded(tempCropProtectionModelList);
            }
        });
    }


    public void addCropProtection(CropProtectionModel cropProtectionModel) {
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cropProtection").document().set(cropProtectionModel);
    }

    public void deleteCropProtection(CropProtectionModel cropProtectionModel){
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
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
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("cropProtection").document(cropProtectionModel.getId()).update(result);
    }

    public interface OnFireStoreDataAdded {
        void cropProtectionDataAdded(List<CropProtectionModel> cropProtectionModels);
    }

}
