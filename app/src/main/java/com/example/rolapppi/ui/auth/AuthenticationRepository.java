package com.example.rolapppi.ui.auth;

import android.app.Application;
import android.content.Intent;
import android.net.ipsec.ike.TunnelModeChildSessionParams;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.rolapppi.MainActivity;
import com.example.rolapppi.R;
import com.example.rolapppi.utills.CheckInternet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public void register(String email, String pass) {
        CheckInternet checkInternet = new CheckInternet();
        if (checkInternet.isConnected(application)) {
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                    } else {
                        Toast.makeText(application, "Podany użytkownik już istnieje", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
    }

    public void login(String email, String pass) {
        CheckInternet checkInternet = new CheckInternet();
        if (checkInternet.isConnected(application)) {
            progressbarObservable.setValue(true);
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressbarObservable.setValue(false);
                        firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                    } else {
                        progressbarObservable.setValue(false);
                        Toast.makeText(application, "Nieprawidłowe dane", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
    }

    public void delete(String password) {

        CheckInternet checkInternet = new CheckInternet();
        if (checkInternet.isConnected(application)) {
            String uid = firebaseAuth.getCurrentUser().getUid();
            FirebaseUser user = firebaseAuth.getCurrentUser();

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("TAG", "User re-authenticated.");

                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firestore.collection("user_data/" + uid + "/feed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                firestore.collection("user_data/" + uid + "/feed").document(snapshot.getId()).delete();
                                            }
                                        }
                                    });
                                    firestore.collection("user_data/" + uid + "/cattle").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                firestore.collection("user_data/" + uid + "/cattle").document(snapshot.getId()).delete();
                                            }
                                        }
                                    });
                                    firestore.collection("user_data/" + uid + "/cropProtection").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                firestore.collection("user_data/" + uid + "/cropProtection").document(snapshot.getId()).delete();
                                            }
                                        }
                                    });
                                    firestore.collection("user_data").document(uid).delete();
                                }
                                if (task.isSuccessful()) {
                                    Toast.makeText(application, "Konto usunięto", Toast.LENGTH_SHORT).show();
                                    logOut();
                                    application.startActivity(new Intent(application, MainActivity.class));
                                } else
                                    Toast.makeText(application, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else
                        Toast.makeText(application, "Hasło nieprawidłowe", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();


    }

    public void changeEmail(String password, String newEmail) {
        CheckInternet checkInternet = new CheckInternet();
        if (checkInternet.isConnected(application)) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("TAG", "User re-authenticated.");

                    if (task.isSuccessful()) {

                        firebaseAuth.fetchSignInMethodsForEmail(newEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    try {

                                        if (task.getResult().getSignInMethods().size() == 1) {
                                            Toast.makeText(application, "Podany email jest już w użyciu", Toast.LENGTH_SHORT).show();


                                        } else {
                                            user.updateEmail(newEmail)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                firebaseUserMutableLiveData.setValue(user);
                                                                Log.d("TAG", "User email address updated.");
                                                                Toast.makeText(application, "Email użytkownika zmieniony", Toast.LENGTH_SHORT).show();
                                                            } else
                                                                Toast.makeText(application, "Nie udało się zmienić email użytkownika", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } catch (NullPointerException e) {
                                        Log.e("DIPA", "onComplete: NullPointerException" + e.getMessage());
                                    }
                                }
                            }
                        });


                    } else
                        Toast.makeText(application, "Podane hasło jest nieprawidłowe", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();

    }

    public void changePassword(String oldPassword, String newPassword) {
        CheckInternet checkInternet = new CheckInternet();
        if (checkInternet.isConnected(application)) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("TAG", "User re-authenticated.");

                    if (task.isSuccessful()) {

                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUserMutableLiveData.setValue(user);
                                            Log.d("TAG", "User password updated.");
                                            Toast.makeText(application, "Hasło użytkownika zmienione", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else
                        Toast.makeText(application, "Stare hasło nieprawidłowe", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();

    }

    public void logOut() {
        firebaseAuth.signOut();
        userLoggedMutableLiveData.postValue(true);
    }

    public void resetPassword(String email) {
        CheckInternet checkInternet = new CheckInternet();
        if (checkInternet.isConnected(application)) {


            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(application, "Sprawdź swoją pocztę", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(application, "Nieprawidłowe dane", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(application, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
    }
}
