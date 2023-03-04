package com.example.rolapppi.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rolapppi.fragments.cattle.CattleRepository;

public class UserDataViewModel extends ViewModel implements UserDataRepository.OnFireStoreDataAdded {

    MutableLiveData<UserData> selected = new MutableLiveData<>();

    UserDataRepository firebaseRepo = new UserDataRepository(this);

    public UserDataViewModel() {
        firebaseRepo.loadData();
    }

    public LiveData<UserData> getLiveDatafromFireStore() {
        return selected;
    }

    @Override
    public void userDataAdded(UserData userData) {
        selected.setValue(userData);
    }

    public void feedEdit(UserData userData){
        selected.setValue(userData);
        firebaseRepo.updateUserData(userData);
    }

}
