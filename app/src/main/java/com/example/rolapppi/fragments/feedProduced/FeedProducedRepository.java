package com.example.rolapppi.fragments.feedProduced;

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
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feedProduced").document().set(feedProducedModel);
    }

    public void deleteFeed(FeedProducedModel feedProducedModel){
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feedProduced").document(feedProducedModel.getId()).delete();
    }

    public void updateFeed(FeedProducedModel feedProducedModel) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("nameFeed", feedProducedModel.getNameFeed());
        result.put("origin", feedProducedModel.getOrigin());
        result.put("count", feedProducedModel.getCount());
        result.put("destination", feedProducedModel.getDestination());
        result.put("cattleType", feedProducedModel.getCattleType());
        result.put("acquisition", feedProducedModel.getAcquisition());
        firestore.collection("user_data").document(FirebaseAuth.getInstance().getUid())
                .collection("feedProduced").document(feedProducedModel.getId()).update(result);
    }

    public interface OnFireStoreDataAdded {
        void feedProducedDataAdded(List<FeedProducedModel> feedProducedModels);
    }
}
