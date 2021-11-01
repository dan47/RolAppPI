package com.example.rolapppi.ui.feedProduced;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FeedProducedViewModel extends ViewModel implements FeedProducedRepository.OnFireStoreDataAdded{

    MutableLiveData<List<FeedProducedModel>> feedProducedModelListData = new MutableLiveData<>();
    MutableLiveData<FeedProducedModel> selected = new MutableLiveData<>();

    FeedProducedRepository firebaseRepo = new FeedProducedRepository(this);

    public FeedProducedViewModel() {
        firebaseRepo.loadData();
    }

    public LiveData<List<FeedProducedModel>> getLiveDatafromFireStore() {
        return feedProducedModelListData;
    }

    @Override
    public void feedProducedDataAdded(List<FeedProducedModel> feedProducedModels) {
        feedProducedModelListData.setValue(feedProducedModels);
    }


    public void feedProducedEdit(FeedProducedModel feedProducedModel){
        selected.setValue(feedProducedModel);
        firebaseRepo.updateFeed(feedProducedModel);
    }

    public void feedProducedAdd(FeedProducedModel feedProducedModel) {
        firebaseRepo.addFeed(feedProducedModel);
    }

    public void feedProducedDelete(FeedProducedModel feedProducedModel) {
        firebaseRepo.deleteFeed(feedProducedModel);
    }

    @Override
    public void OnError(Exception e) {

    }


    public void setSelected(FeedProducedModel feedProducedModel) {
        selected.setValue(feedProducedModel);
    }

    public MutableLiveData<FeedProducedModel> getSelected() {
        return selected;
    }
}
