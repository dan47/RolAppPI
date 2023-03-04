package com.example.rolapppi.fragments.auth;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import com.example.rolapppi.MainActivity;
import com.example.rolapppi.utills.CheckNetwork;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;


public class AuthenticationRepository {
    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> userLoggedMutableLiveData;
    private MutableLiveData<Boolean> progressbarObservable;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedMutableLiveData() {
        return userLoggedMutableLiveData;
    }

    public MutableLiveData<Boolean> getProgressbarObservable() {
        return progressbarObservable;
    }

    public AuthenticationRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userLoggedMutableLiveData = new MutableLiveData<>();
        progressbarObservable = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
        }
    }

    public void register(String email, String pass, String farm_id) {
        CheckNetwork checkNetwork = new CheckNetwork();
        if (checkNetwork.isConnected(application)) {
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                    firestore.collection("user_data").document(firebaseAuth.getCurrentUser().getUid()).set(new HashMap<String, Object>() {{
                        put("farm_id", farm_id);
                    }});
                } else {
                    Toast.makeText(application, "Podany użytkownik już istnieje", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
    }

    public void login(String email, String pass) {
        CheckNetwork checkNetwork = new CheckNetwork();
        if (checkNetwork.isConnected(application)) {
            progressbarObservable.setValue(true);
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressbarObservable.setValue(false);
                    firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                } else {
                    progressbarObservable.setValue(false);
                    Toast.makeText(application, "Nieprawidłowe dane", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
    }

    public void delete(String password) {

        CheckNetwork checkNetwork = new CheckNetwork();
        if (checkNetwork.isConnected(application)) {

            String uid = firebaseAuth.getCurrentUser().getUid();
            FirebaseUser  user = firebaseAuth.getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                Log.d("TAG", "User re-authenticated.");

                if (task.isSuccessful()) {

                    deleteCollection(firestore, "user_data/" + uid + "/feed");
                    deleteCollection(firestore, "user_data/" + uid + "/cattle");
                    deleteCollection(firestore, "user_data/" + uid + "/cropProtection");
                    deleteCollection(firestore, "user_data/" + uid + "/feedProduced");

                    firestore.collection("user_data").document(uid).delete();

                    firebaseAuth.getCurrentUser().delete().addOnCompleteListener(t -> {
                        if (t.isSuccessful()) {
                            Toast.makeText(application, "Konto usunięto", Toast.LENGTH_SHORT).show();
                            logOut();
                            Intent intent = new Intent(application, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            application.startActivity(intent);
                        } else
                            Toast.makeText(application, task.getException().toString(), Toast.LENGTH_SHORT).show();

                    });


                } else
                    Toast.makeText(application, "Hasło nieprawidłowe", Toast.LENGTH_SHORT).show();
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();


    }

    public void changeEmail(String password, String newEmail) {
        CheckNetwork checkNetwork = new CheckNetwork();
        if (checkNetwork.isConnected(application)) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                Log.d("TAG", "User re-authenticated.");

                if (task.isSuccessful()) {

                    firebaseAuth.fetchSignInMethodsForEmail(newEmail).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            try {

                                if (task1.getResult().getSignInMethods().size() == 1) {
                                    Toast.makeText(application, "Podany email jest już w użyciu", Toast.LENGTH_SHORT).show();
                                } else {
                                    user.updateEmail(newEmail)
                                            .addOnCompleteListener(t -> {
                                                if (t.isSuccessful()) {
                                                    firebaseUserMutableLiveData.setValue(user);
                                                    Log.d("TAG", "User email address updated.");
                                                    Toast.makeText(application, "Email użytkownika zmieniony", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(application, "Nie udało się zmienić email użytkownika", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } catch (NullPointerException e) {
                                Log.e("DIPA", "onComplete: NullPointerException" + e.getMessage());
                            }
                        }
                    });


                } else
                    Toast.makeText(application, "Podane hasło jest nieprawidłowe", Toast.LENGTH_SHORT).show();
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();

    }

    public void changePassword(String oldPassword, String newPassword) {
        CheckNetwork checkNetwork = new CheckNetwork();
        if (checkNetwork.isConnected(application)) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                Log.d("TAG", "User re-authenticated.");

                if (task.isSuccessful()) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    firebaseUserMutableLiveData.setValue(user);
                                    Log.d("TAG", "User password updated.");
                                    Toast.makeText(application, "Hasło użytkownika zmienione", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(application, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else
                    Toast.makeText(application, "Stare hasło nieprawidłowe", Toast.LENGTH_SHORT).show();
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();

    }

    public void logOut() {
        firebaseAuth.signOut();
        userLoggedMutableLiveData.postValue(true);
    }

    public void resetPassword(String email) {
        CheckNetwork checkNetwork = new CheckNetwork();

        if (checkNetwork.isConnected(application)) {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    Toast.makeText(application, "Sprawdź swoją pocztę", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(application, "Nieprawidłowe dane", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCollection(FirebaseFirestore firestore, String documentId) {
        firestore.collection(documentId).get().addOnCompleteListener(t -> {
            for (QueryDocumentSnapshot snapshot : t.getResult()) {
                firestore.collection(documentId).document(snapshot.getId()).delete();
            }
            if (!t.isSuccessful())
                Toast.makeText(application, "Problem z usunięciem danych", Toast.LENGTH_SHORT).show();
        });
    }
}
