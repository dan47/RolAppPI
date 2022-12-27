package com.example.rolapppi.fragments.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;


public class AuthenticationViewModel extends AndroidViewModel {

    private AuthenticationRepository repository;
    private MutableLiveData<FirebaseUser> userData;
    private MutableLiveData<Boolean> loggedStatus;
    private MutableLiveData<Boolean> progressbarObservable;

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public MutableLiveData<Boolean> getProgressbarObservable() {
        return progressbarObservable;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    public AuthenticationViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthenticationRepository(application);
        userData = repository.getFirebaseUserMutableLiveData();
        loggedStatus = repository.getUserLoggedMutableLiveData();
        progressbarObservable = repository.getProgressbarObservable();
    }

    public void register(String email, String pass) {
        repository.register(email, pass);
    }

    public void delete(String password) {
        repository.delete(password);
    }

    public void changeEmail(String password, String newEmail){ repository.changeEmail(password, newEmail);}

    public void changePassword(String oldPassword, String newPassword) { repository.changePassword(oldPassword, newPassword); }

    public void signIn(String email, String pass) {
        repository.login(email, pass);
    }

    public void logOut() {
        repository.logOut();
    }

    public void resetPassword(String email) { repository.resetPassword(email); }
}
