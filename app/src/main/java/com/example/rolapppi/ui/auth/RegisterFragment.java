package com.example.rolapppi.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;


import com.example.rolapppi.R;


public class RegisterFragment extends Fragment {
    private TextInputEditText emailView, passwordView, confirmPassword;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout, confirmPasswordTextInputLayout;
    private TextView alreadyHaveAccount;
    private Button registerButton;
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
                    navController.navigate(R.id.action_registerFragment_to_loginFragment);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailView = view.findViewById(R.id.inputEmail);
        passwordView = view.findViewById(R.id.inputPassword);
        confirmPassword = view.findViewById(R.id.inputConfirmPassword);
        emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        confirmPasswordTextInputLayout = view.findViewById(R.id.confirmPasswordTextInputLayout);
        alreadyHaveAccount = view.findViewById(R.id.alreadyHaveAccount);
        registerButton = view.findViewById(R.id.btnRegister);

        navController = Navigation.findNavController(view);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailView.getText().toString();
                String pass = passwordView.getText().toString();
                String confPass = confirmPassword.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailTextInputLayout.setError("Nieprawidłowy email");
                } else if (pass.length() < 6) {
                    passwordTextInputLayout.setError("Hasło za krótkie");
                } else if (!confPass.equals(pass)) {
                    confirmPasswordTextInputLayout.setError("Hasła są różne");
                } else {
                    viewModel.register(email, pass);
                }
            }
        });
    }
}