package com.example.rolapppi.ui.cattle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CattleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CattleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Galeria");
    }

    public LiveData<String> getText() {
        return mText;
    }
}