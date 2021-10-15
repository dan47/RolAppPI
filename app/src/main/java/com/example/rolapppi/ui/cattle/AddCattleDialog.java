package com.example.rolapppi.ui.cattle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rolapppi.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;


public class AddCattleDialog extends AppCompatDialogFragment {

    public static EditText animal_idE;
    private EditText mother_idE;
    private Chip male_chip, female_chip;
    private TextView birthdayE;
    private Button mPickDateButton;
    private CattleViewModel cattleViewModel;
    private Button submitBtnE, scanBtnE;
    private Boolean edit;
    private CattleModel cattleModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addcattle, null);

        mPickDateButton = view.findViewById(R.id.birthdayBtn);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Wybierz datę");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        birthdayE.setText(materialDatePicker.getHeaderText().toString());
                    }
                });

        builder.setView(view)
                .setTitle("Bydło dodanie zwierzęcia");

        animal_idE = view.findViewById(R.id.animal_id);
        mother_idE = view.findViewById(R.id.mother_id);
        birthdayE = view.findViewById(R.id.birthday);
        submitBtnE = view.findViewById(R.id.submitBtn);
        scanBtnE = view.findViewById(R.id.scanBtn);
        male_chip = view.findViewById(R.id.male);
        female_chip = view.findViewById(R.id.female);
        male_chip.setChecked(true);


        cattleViewModel = new ViewModelProvider(requireActivity()).get(CattleViewModel.class);
        try {
            cattleModel = cattleViewModel.selected.getValue();
            animal_idE.setText(cattleModel.getAnimal_id());
            mother_idE.setText(cattleModel.getMother_id());
            birthdayE.setText(cattleModel.getBirthday());
            edit = true;
            if (cattleModel.getGender().equals("Samica")) {
                female_chip.setChecked(true);
            }
        } catch (Exception e) {
            edit = false;
        }
        scanBtnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ScannerCode.class));
            }
        });


        submitBtnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String animal_id = animal_idE.getText().toString();
                String mother_id = mother_idE.getText().toString();
                String birthday = birthdayE.getText().toString();
                String gender;


                if (TextUtils.isEmpty(animal_id)) {
                    animal_idE.setError("Proszę wprowadzić numer identyfikacyjny");
                    return;
                }
                if (TextUtils.isEmpty(birthday)) {
                    birthdayE.setError("Proszę wybrać datę urodzin");
                    return;
                }
                if (TextUtils.isEmpty(mother_id)) {
                    mother_idE.setError("Proszę wprowadzić numer identyfikacyjny matki");
                    return;
                }

                if (male_chip.isChecked()) {
                    gender = "Samiec";
                } else {
                    gender = "Samica";
                }
                CattleModel model = new CattleModel(animal_id, birthday, gender, mother_id);

                if (edit) {
                        cattleViewModel.cattleEdit(model);
                        if (!model.getAnimal_id().equals(cattleModel.getAnimal_id())) {
                            cattleViewModel.cattleDelete(cattleModel);
                        }
                } else {
                    cattleViewModel.cattleAdd(model);
                }
                dismiss();

            }
        });

        return builder.create();
    }

}
