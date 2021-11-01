package com.example.rolapppi.ui.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.rolapppi.HomeActivity;
import com.example.rolapppi.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.example.rolapppi.R;


public class LoginFragment extends Fragment {


    private TextInputEditText emailView, passwordView;
    private TextInputLayout emailInputLayout, passswordInputLayout;
    private ProgressBar progressBar;
    private TextView createAccuont, forgotPassword;
    private Button loginButton;
    private AuthenticationViewModel viewModel;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);
        viewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        viewModel.getProgressbarObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean progressObserve) {
                if (progressObserve) {
                    progressBar.setVisibility(View.VISIBLE);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Theme_RolAppPI);
        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // Inflate the layout for this fragment
        return localInflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailView = view.findViewById(R.id.inputEmail);
        passwordView = view.findViewById(R.id.inputPassword);
        createAccuont = view.findViewById(R.id.createNewAccount);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        loginButton = view.findViewById(R.id.btnLogin);
        progressBar = view.findViewById(R.id.progressBarLogin);
        passswordInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        emailInputLayout = view.findViewById(R.id.emailTextInputLayout);



        navController = Navigation.findNavController(view);
        createAccuont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_resetpassword, null);
                TextInputEditText email = dialogView.findViewById(R.id.inputEmail);
                TextInputLayout emailTextInputLayout = dialogView.findViewById(R.id.emailTextInputLayout);
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Resetowanie hasła")
                        .setView(dialogView)
                        .setPositiveButton("resetuj", null)
                        .create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                String emailString = email.getText().toString();

                                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
                                    emailTextInputLayout.setError("Nieprawidłowy e-mail");
                                } else {
                                    viewModel.resetPassword(emailString);
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailView.getText().toString();
                String pass = passwordView.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInputLayout.setError("Nieprawidłowy email");
                } else if (pass.length() < 6) {
                    passswordInputLayout.setError("Hasło za krótkie");
                    emailInputLayout.setErrorEnabled(false);
                }else{
                    passswordInputLayout.setErrorEnabled(false);
                    viewModel.signIn(email, pass);
                }
            }
        });

    }
}