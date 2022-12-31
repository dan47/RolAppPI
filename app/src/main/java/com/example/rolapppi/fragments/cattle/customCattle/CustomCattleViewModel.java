package com.example.rolapppi.fragments.cattle.customCattle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rolapppi.fragments.cattle.CattleModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomCattleViewModel extends ViewModel implements CustomCattleRepository.OnFireStoreDataAdded {

    MutableLiveData<Map<String,List<CattleModel>>> cattleModelListsData = new MutableLiveData<>();


    CustomCattleRepository firebaseRepo = new CustomCattleRepository(this);

    public CustomCattleViewModel() {firebaseRepo.loadData();
    }

    public LiveData<Map<String,List<CattleModel>>> getLiveDatafromFireStore() {
        return cattleModelListsData;
    }

    @Override
    public void customCattleDataAdded(Map<String, List<CattleModel>> cattleModelLists) {
        cattleModelListsData.setValue(cattleModelLists);
    }


    public void customCattleAdd(List<CattleModel> cattleModelList, String name) {
        firebaseRepo.addCustomCattle(cattleModelList, name);
    }

    public void customCattleAdd2(Map<String,List<CattleModel>> cattleModelListse) {
        firebaseRepo.addCustomCattle2(cattleModelListse);
    }

    public void customCattleDelete(List<CattleModel> cattleModelList, String name) {
        firebaseRepo.deleteCustomCattle(cattleModelList, name);
    }
    public void deleteCustomCattleGroup(String name) {
        firebaseRepo.deleteCustomCattleGroup(name);
    }

    @Override
    public void OnError(Exception e) { }


}
