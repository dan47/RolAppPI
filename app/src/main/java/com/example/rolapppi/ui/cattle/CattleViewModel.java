package com.example.rolapppi.ui.cattle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CattleViewModel extends ViewModel implements CattleRepository.OnFireStoreDataAdded {

    MutableLiveData<List<CattleModel>> cattleModelListData = new MutableLiveData<>();
    MutableLiveData<CattleModel> selected = new MutableLiveData<>();

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

    public void cattleEdit(CattleModel cattleModel){
        selected.setValue(cattleModel);
        firebaseRepo.addCattle(cattleModel);
    }

    public void cattleAdd(CattleModel cattleModel) {
        firebaseRepo.addCattle(cattleModel);
    }

    public void cattleDelete(CattleModel cattleModel) {
        firebaseRepo.deleteCattle(cattleModel);
    }

    public void cattleUpdateMother(String cattleMotherId){ firebaseRepo.updateCattleMother(cattleMotherId);}

    @Override
    public void OnError(Exception e) {

    }

    public void setSelected(CattleModel cattleModel) {
        selected.setValue(cattleModel);
    }

    public MutableLiveData<CattleModel> getSelected() {
        return selected;
    }

    public void addCalving(CattleModel cattleModel, String calving) {
        cattleModel.setCaliving(calving);
        firebaseRepo.addCattle(cattleModel);
    }
}