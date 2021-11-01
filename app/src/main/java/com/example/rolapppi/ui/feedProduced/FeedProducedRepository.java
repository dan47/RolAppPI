package com.example.rolapppi.ui.feedProduced;

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

public class FeedProducedRepository {

    FeedProducedRepository.OnFireStoreDataAdded fireStoreDataAdded;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Query feedRef = firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid()).collection("feedProduced");

    public FeedProducedRepository(FeedProducedRepository.OnFireStoreDataAdded fireStoreDataAdded) {
        this.fireStoreDataAdded = fireStoreDataAdded;
    }

    public void getDataFromFireStore() {

        feedRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    fireStoreDataAdded.feedProducedDataAdded(task.getResult().toObjects(FeedProducedModel.class));

                } else {

                    fireStoreDataAdded.OnError(task.getException());
                }
            }
        });

    }


    public void loadData(){

        feedRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                List<FeedProducedModel> tempFeedProducedModelList = new ArrayList<>();
                for(DocumentSnapshot dc : value.getDocuments()){
                    tempFeedProducedModelList.add(dc.toObject(FeedProducedModel.class));
                }
                fireStoreDataAdded.feedProducedDataAdded(tempFeedProducedModelList);
            }
        });
    }


    public void addFeed(FeedProducedModel feedProducedModel) {
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feedProduced").document().set(feedProducedModel);
    }

    public void deleteFeed(FeedProducedModel feedProducedModel){
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feedProduced").document(feedProducedModel.getId()).delete();
    }

    public void updateFeed(FeedProducedModel feedProducedModel) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("nameFeed", feedProducedModel.getNameFeed());
        result.put("origin", feedProducedModel.getOrigin());
        result.put("count", feedProducedModel.getCount());
        result.put("weight", feedProducedModel.getWeight());
        result.put("destination", feedProducedModel.getDestination());
        result.put("cattleType", feedProducedModel.getCattleType());
        result.put("acquisition", feedProducedModel.getAcquisition());
        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feedProduced").document(feedProducedModel.getId()).update(result);
    }

    public interface OnFireStoreDataAdded {
        void feedProducedDataAdded(List<FeedProducedModel> feedProducedModels);
        void OnError(Exception e);
    }
}
