package com.example.rolapppi.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rolapppi.MainActivity;
import com.example.rolapppi.R;
import com.example.rolapppi.ui.feed.FeedViewModel;
import com.google.firebase.auth.FirebaseUser;


public class SettingsFragment extends Fragment {


    private AuthenticationViewModel authenticationViewModel;
    private TextView accountMail;
    private Button editBtn, deleteBtn;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountMail = view.findViewById(R.id.accountMail);
        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);

        authenticationViewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
        authenticationViewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    accountMail.setText(firebaseUser.getEmail());
                }
            }
        });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationViewModel.delete();
                authenticationViewModel.logOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


    }
}