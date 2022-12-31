package com.example.rolapppi.fragments.cattle.customCattle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.rolapppi.R;
import com.example.rolapppi.fragments.cattle.CattleModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Set;


public class AddCustomDialog extends AppCompatDialogFragment {


    private TextInputEditText name;
    private TextInputLayout nameLayout;
    private List<CattleModel> cattleModelList;
    private CustomCattleViewModel customCattleViewModel;

    public AddCustomDialog(List<CattleModel> cattleModelList){
        this.cattleModelList = cattleModelList;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_custom_cattle, null);

        name = view.findViewById(R.id.editTextName);
        nameLayout = view.findViewById(R.id.nameTextInputLayout);
        customCattleViewModel = new ViewModelProvider(requireActivity()).get(CustomCattleViewModel.class);

        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                .setTitle("Dodanie własnej listy")
                .setView(view)
                .setPositiveButton("Dodaj", null).create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String nameString = name.getText().toString();

                        if (nameString.isEmpty()) {
                            nameLayout.setError("Wpisz nazwę listy");
                        } else {
                            customCattleViewModel.customCattleAdd(cattleModelList, nameString);
                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_nav_cattle_to_customCattleFragment);
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        alertDialog.show();
        return alertDialog;
    }


}