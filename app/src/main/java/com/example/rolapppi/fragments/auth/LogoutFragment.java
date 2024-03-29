package com.example.rolapppi.fragments.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rolapppi.MainActivity;
import com.example.rolapppi.R;


public class LogoutFragment extends Fragment {

    private AuthenticationViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);
        viewModel.getLoggedStatus().observe(this, aBoolean -> {
            if (aBoolean){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new AlertDialog.Builder(view.getContext())
                .setTitle("Czy chcesz się wylogować?")
                .setPositiveButton("Tak", (dialogInterface, i) -> viewModel.logOut())
                .setNegativeButton("Nie", (dialogInterface, i) -> getActivity().onBackPressed()).show();

//        navController = Navigation.findNavController(view);
//        signOutBtn = view.findViewById(R.id.signOutBtn);
//
//        signOutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewModel.signOut();
//            }
//        });
    }
}