package com.example.rolapppi.fragments.cropProtection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CropProtectionViewModel extends ViewModel implements CropProtectionRepository.OnFireStoreDataAdded  {

    MutableLiveData<List<CropProtectionModel>> cropProtectionModelListData = new MutableLiveData<>();
    MutableLiveData<CropProtectionModel> selected = new MutableLiveData<>();

    CropProtectionRepository firebaseRepo = new CropProtectionRepository(this);

    public CropProtectionViewModel() {
        firebaseRepo.loadData();
    }

    public LiveData<List<CropProtectionModel>> getLiveDatafromFireStore() {
        return cropProtectionModelListData;
    }

    @Override
    public void cropProtectionDataAdded(List<CropProtectionModel> cropProtectionModels) {
        cropProtectionModelListData.setValue(cropProtectionModels);
    }


    public void cropProtectionEdit(CropProtectionModel cropProtectionModel){
        selected.setValue(cropProtectionModel);
        firebaseRepo.updateCropProtection(cropProtectionModel);
    }

    public void cropProtectionAdd(CropProtectionModel cropProtectionModel) {
            firebaseRepo.addCropProtection(cropProtectionModel);
    }

    public void cropProtectionDelete(CropProtectionModel cropProtectionModel) {
        firebaseRepo.deleteCropProtection(cropProtectionModel);
    }


    public void setSelected(CropProtectionModel cropProtectionModel) {
        selected.setValue(cropProtectionModel);
    }

    public MutableLiveData<CropProtectionModel> getSelected() {
        return selected;
    }

}
