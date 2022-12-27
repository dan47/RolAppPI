package com.example.rolapppi.fragments.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FeedViewModel extends ViewModel implements FeedRepository.OnFireStoreDataAdded  {

    MutableLiveData<List<FeedModel>> feedModelListData = new MutableLiveData<>();
    MutableLiveData<FeedModel> selected = new MutableLiveData<>();

    FeedRepository firebaseRepo = new FeedRepository(this);

    public FeedViewModel() {
        firebaseRepo.loadData();
    }

    public LiveData<List<FeedModel>> getLiveDatafromFireStore() {
        return feedModelListData;
    }

    @Override
    public void feedDataAdded(List<FeedModel> feedModels) {
        feedModelListData.setValue(feedModels);
    }


    public void feedEdit(FeedModel feedModel){
        selected.setValue(feedModel);
        firebaseRepo.updateFeed(feedModel);
    }

    public void feedAdd(FeedModel feedModel) {
        firebaseRepo.addFeed(feedModel);
    }

    public void feedDelete(FeedModel feedModel) {
        firebaseRepo.deleteFeed(feedModel);
    }

    public void setSelected(FeedModel feedModel) {
        selected.setValue(feedModel);
    }

    public MutableLiveData<FeedModel> getSelected() {
        return selected;
    }
}
