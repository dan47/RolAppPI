package com.example.rolapppi.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;


import com.example.rolapppi.R;


public class RegisterFragment extends Fragment {
    private TextInputEditText emailView, passwordView, confirmPassword, farm_id;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout, confirmPasswordTextInputLayout, farmTextInputLayout;
    private TextView alreadyHaveAccount;
    private Button registerButton;
    private AuthenticationViewModel viewModel;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);
        viewModel.getUserData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
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
        farm_id = view.findViewById(R.id.inputFarmId);
        emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        confirmPasswordTextInputLayout = view.findViewById(R.id.confirmPasswordTextInputLayout);
        farmTextInputLayout = view.findViewById(R.id.farmTextInputLayout);
        alreadyHaveAccount = view.findViewById(R.id.alreadyHaveAccount);
        registerButton = view.findViewById(R.id.btnRegister);

        navController = Navigation.findNavController(view);

        alreadyHaveAccount.setOnClickListener(v -> navController.navigate(R.id.action_registerFragment_to_loginFragment));

        registerButton.setOnClickListener(v -> {
            String email = emailView.getText().toString();
            String pass = passwordView.getText().toString();
            String confPass = confirmPassword.getText().toString();
            String farmId = farm_id.getText().toString();

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTextInputLayout.setError("Nieprawidłowy email");
            } else if (farmId.length() != 12) {
                farmTextInputLayout.setError("Nr powinien mieć 12 cyfr");
            } else if (pass.length() < 6) {
                passwordTextInputLayout.setError("Hasło za krótkie");
            } else if (!confPass.equals(pass)) {
                confirmPasswordTextInputLayout.setError("Hasła są różne");
            } else {
                viewModel.register(email, pass, farmId);
            }
        });
    }
}