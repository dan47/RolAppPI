package com.example.rolapppi.ui.feed;

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

public class FeedRepository {


    FeedRepository.OnFireStoreDataAdded fireStoreDataAdded;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query cropProtectionRef = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).collection("feed");

    public FeedRepository(FeedRepository.OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }

    public void getDataFromFireStore() {

        cropProtectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    fireStoreDataAdded.feedDataAdded(task.getResult().toObjects(FeedModel.class));

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
                List<FeedModel> tempFeedModelList = new ArrayList<>();
                for(DocumentSnapshot dc : value.getDocuments()){
                    tempFeedModelList.add(dc.toObject(FeedModel.class));
                }
                fireStoreDataAdded.feedDataAdded(tempFeedModelList);
            }
        });
    }


    public void addFeed(FeedModel feedModel) {
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feed").document().set(feedModel);
    }

    public void deleteFeed(FeedModel feedModel){
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feed").document(feedModel.getId()).delete();
    }

    public void updateFeed(FeedModel feedModel) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("purchaseDate", feedModel.getPurchaseDate());
        result.put("seller", feedModel.getSeller());
        result.put("producer", feedModel.getProducer());
        result.put("nameFeed", feedModel.getNameFeed());
        result.put("batch", feedModel.getBatch());
        result.put("count", feedModel.getCount());
        result.put("packageType", feedModel.getPackageType());
        result.put("remarks", feedModel.getRemarks());
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feed").document(feedModel.getId()).update(result);
    }

    public interface OnFireStoreDataAdded {
        void feedDataAdded(List<FeedModel> feedModels);
        void OnError(Exception e);
    }
}
