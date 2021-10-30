package com.example.rolapppi.ui.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rolapppi.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;


public class SettingsFragment extends Fragment {


    private AuthenticationViewModel authenticationViewModel;
    private TextView accountMail;
    private Button editBtn, deleteBtn, editPassworBtn;

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
        editPassworBtn = view.findViewById(R.id.editPasswordBtn);
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
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_editemail, null);

                TextInputEditText password = dialogView.findViewById(R.id.InputPassword);
                TextInputEditText newEmail = dialogView.findViewById(R.id.editTextNewEmail);

                TextInputLayout passwordTextInputLayout = dialogView.findViewById(R.id.passwordTextInputLayout);
                TextInputLayout emailTextInputLayout = dialogView.findViewById(R.id.emailTextInputLayout);

                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Zmiana e-maila")
                        .setView(dialogView)
                        .setPositiveButton("Zmień", null)
                        .create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                String passwordString = password.getText().toString();
                                String newEmailString = newEmail.getText().toString();

                                if (passwordString.length() < 6) {
                                    passwordTextInputLayout.setError("Hasło za krótkie");
                                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmailString).matches()) {
                                    emailTextInputLayout.setError("Nieprawidłowy e-mail");
                                    passwordTextInputLayout.setErrorEnabled(false);
                                } else {
                                    authenticationViewModel.changeEmail(passwordString, newEmailString);
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        editPassworBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_editpassword, null);

                TextInputEditText oldPassword = dialogView.findViewById(R.id.editTextOldPassword);
                TextInputEditText newPassword = dialogView.findViewById(R.id.editTextNewPassword);
                TextInputEditText confrimPassword = dialogView.findViewById(R.id.editTextConfirmPassword);

                TextInputLayout oldPasswordTextInputLayout = dialogView.findViewById(R.id.oldPasswordTextInputLayout);
                TextInputLayout newPasswordTextInputLayout = dialogView.findViewById(R.id.newPasswordTextInputLayout);
                TextInputLayout confirmPasswordTextInputLayout = dialogView.findViewById(R.id.confirmPasswordTextInputLayout);

                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Zmiana hasła")
                        .setView(dialogView)
                        .setPositiveButton("Zmień", null)
                        .create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                String oldPasswordString = oldPassword.getText().toString();
                                String newPasswordString = newPassword.getText().toString();
                                String confrimPasswordString = confrimPassword.getText().toString();

                                if (oldPasswordString.length() < 6) {
                                    oldPasswordTextInputLayout.setError("Hasło za ktrótkie");
                                } else if (newPasswordString.equals(oldPasswordString)) {
                                    newPasswordTextInputLayout.setError("Nowe hasło musi różnić od poprzedniego");
                                    oldPasswordTextInputLayout.setErrorEnabled(false);
                                } else if (newPasswordString.length() < 6) {
                                    newPasswordTextInputLayout.setError("Hasło za ktrótkie");
                                } else if (!confrimPasswordString.equals(newPasswordString)) {
                                    confirmPasswordTextInputLayout.setError("Hasła nie pasują do siebie ");
                                    newPasswordTextInputLayout.setErrorEnabled(false);
                                } else {
                                    authenticationViewModel.changePassword(oldPasswordString, newPasswordString);
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_deleteuser, null);
                TextInputEditText password = dialogView.findViewById(R.id.InputPassword);
                TextInputLayout passwordTextInputLayout = dialogView.findViewById(R.id.passwordTextInputLayout);
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Usunięcie konta")
                        .setView(dialogView)
                        .setPositiveButton("Usuń", null)
                        .create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                String oldPasswordString = password.getText().toString();

                                if (oldPasswordString.length() < 6) {
                                    passwordTextInputLayout.setError("Hasło za krótkie");
                                } else {
                                    authenticationViewModel.delete(oldPasswordString);
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();


            }
        });


    }
}