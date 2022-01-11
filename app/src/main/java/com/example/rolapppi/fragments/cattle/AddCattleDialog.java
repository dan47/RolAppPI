package com.example.rolapppi.fragments.cattle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rolapppi.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class AddCattleDialog extends AppCompatDialogFragment {

    public static EditText animal_idE;
    private Chip male_chip, female_chip;
    private TextView birthdayE;
    private Button mPickDateButton;
    private CattleViewModel cattleViewModel;
    private Button submitBtnE, scanBtnE;
    private Boolean edit;
    private CattleModel cattleModel;
    private AutoCompleteTextView mother_idChoose;
    private ArrayAdapter arrayAdapter;
    private String calving;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addcattle, null);

        mPickDateButton = view.findViewById(R.id.birthdayBtn);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Wybierz datę");
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");

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
                        Date date = new Date((Long) selection);
                        birthdayE.setText(simpleFormat.format(date));
                    }
                });


        animal_idE = view.findViewById(R.id.animal_id);
        birthdayE = view.findViewById(R.id.birthday);
        submitBtnE = view.findViewById(R.id.submitBtn);
        scanBtnE = view.findViewById(R.id.scanBtn);
        male_chip = view.findViewById(R.id.male);
        female_chip = view.findViewById(R.id.female);
        mother_idChoose = view.findViewById(R.id.autoCompleteTextView);
        male_chip.setChecked(true);

        Date date = new Date();
        birthdayE.setText(simpleFormat.format(date));

        cattleViewModel = new ViewModelProvider(requireActivity()).get(CattleViewModel.class);

        try {
            cattleModel = cattleViewModel.selected.getValue();
            animal_idE.setText(cattleModel.getAnimal_id());
            mother_idChoose.setText(cattleModel.getMother_id());
            birthdayE.setText(cattleModel.getBirthday());
            calving = cattleModel.getCaliving();
            edit = true;
            if (cattleModel.getGender().equals(getString(R.string.female))) {
                female_chip.setChecked(true);
            }
        } catch (Exception e) {
            edit = false;
        }
        List<String> mothers_list;
        if(edit){
            builder.setView(view)
                    .setTitle("Bydło edycja zwierzęcia");
            mothers_list = cattleViewModel.getLiveDatafromFireStore().getValue().stream()
                    .filter(e->e.getGender().equals(getString(R.string.female)))
                    .map(x->x.getAnimal_id()).collect(Collectors.toList());
        }else{
            builder.setView(view)
                    .setTitle("Bydło dodanie zwierzęcia");
            mothers_list = cattleViewModel.getLiveDatafromFireStore().getValue().stream()
                    .filter(e->e.getGender().equals(getString(R.string.female)))
                    .filter(y->!y.getCaliving().isEmpty())
                    .map(x->x.getAnimal_id()).collect(Collectors.toList());
        }


        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, mothers_list);
        mother_idChoose.setAdapter(arrayAdapter);


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
                String mother_id = mother_idChoose.getText().toString();
                String birthday = birthdayE.getText().toString();
                String gender;


                if (TextUtils.isEmpty(animal_id)) {
                    animal_idE.setError("Proszę wprowadzić numer identyfikacyjny");
                    return;
                }
                if (animal_id.length()<13){
                    animal_idE.setError("Za krótki numer identyfikacyjny");
                    return;
                }

                if (TextUtils.isEmpty(mother_id)) {
                    mother_idChoose.setError("Proszę wprowadzić numer identyfikacyjny matki");
                    return;
                }
                if (mother_id.length()<13){
                    mother_idChoose.setError("Za krótki numer identyfikacyjny");
                    return;
                }

                if (male_chip.isChecked()) {
                    gender = getString(R.string.male);
                } else {
                    gender = getString(R.string.female);
                }


                if (edit) {
                    CattleModel model = new CattleModel(animal_id, birthday, gender, mother_id, calving);
                        cattleViewModel.cattleEdit(model);
                        if (!model.getAnimal_id().equals(cattleModel.getAnimal_id())) {
                            cattleViewModel.cattleDelete(cattleModel);
                        }
                } else {
                    CattleModel model = new CattleModel(animal_id, birthday, gender, mother_id);
                    cattleViewModel.cattleAdd(model);
                    if(mothers_list.contains(mother_id)){
                        cattleViewModel.cattleUpdateMother(mother_id);
                    }
                }
                dismiss();

            }
        });

        return builder.create();
    }

}
