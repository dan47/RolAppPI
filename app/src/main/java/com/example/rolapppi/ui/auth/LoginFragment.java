package com.example.rolapppi.ui.auth;

import android.os.Bundle;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseUser;
import com.example.rolapppi.R;


public class LoginFragment extends Fragment {

    private EditText emailView, passwordView;
    private ProgressBar progressBar;
    private TextView createAccuont;
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
                    navController.navigate(R.id.action_loginFragment_to_homeActivity);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailView = view.findViewById(R.id.inputEmail);
        passwordView = view.findViewById(R.id.inputPassword);
        createAccuont = view.findViewById(R.id.createNewAccount);
        loginButton = view.findViewById(R.id.btnLogin);
        progressBar = view.findViewById(R.id.progressBarLogin);

        navController = Navigation.findNavController(view);
        createAccuont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailView.getText().toString();
                String pass = passwordView.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()) {
                    viewModel.signIn(email, pass);
                }
            }
        });

    }
}