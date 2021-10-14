package com.example.rolapppi.ui.cattle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CattleViewModel extends ViewModel implements CattleRepository.OnFireStoreDataAdded {

    MutableLiveData<List<CattleModel>> cattleModelListData = new MutableLiveData<>();

    CattleRepository firebaseRepo = new CattleRepository(this);


    public CattleViewModel() {
        firebaseRepo.loadData();
    }

    public LiveData<List<CattleModel>> getLiveDatafromFireStore() {
        return cattleModelListData;
    }

    @Override
    public void cattleDataAdded(List<CattleModel> cattleModelList) {
        cattleModelListData.setValue(cattleModelList);
    }

    public void cattleAdd(CattleModel cattleModel) {
        firebaseRepo.addCattle(cattleModel);
    }


    @Override
    public void OnError(Exception e) {

    }

}