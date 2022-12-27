package com.example.rolapppi.fragments.cattle;

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

    public void cattleAdd(CattleModel cattleModel) {
        if (!cattleModelListData.getValue().stream().anyMatch(e -> e.getAnimal_id().equals(cattleModel.getAnimal_id()))) {
                 firebaseRepo.addCattle(cattleModel);
        }
    }
    public void addCalving(CattleModel cattleModel, String calving) {
        cattleModel.setCaliving(calving);
        firebaseRepo.addCattle(cattleModel);
    }

    public void cattleDelete(CattleModel cattleModel) {
        firebaseRepo.deleteCattle(cattleModel);
    }

    public void cattleUpdateMother(String cattleMotherId, String previousCaliving){ firebaseRepo.updateCattleMother(cattleMotherId,
            previousCaliving);}

    public void deleteCalving(String cattleMotherId){ firebaseRepo.deleteCalving(cattleMotherId);}

    public void setSelected(CattleModel cattleModel) {
        selected.setValue(cattleModel);
    }

    public void cattleEdit(CattleModel cattleModel){
        selected.setValue(cattleModel);
        firebaseRepo.addCattle(cattleModel);
    }

    public MutableLiveData<CattleModel> getSelected() {
        return selected;
    }
}