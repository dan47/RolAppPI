package com.example.rolapppi.ui.cattle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.rolapppi.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.resources.TextAppearance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCattleDialog extends AppCompatDialogFragment {

    private EditText animal_idE, mother_idE;
    private Chip male_chip;
    private TextView birthdayE;
    private Button mPickDateButton;

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
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        birthdayE.setText(materialDatePicker.getHeaderText().toString());

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date

                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });

        builder.setView(view)
                .setTitle("Bydło dodanie zwierzęcia")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String animal_id = animal_idE.getText().toString();
                        String mother_id = mother_idE.getText().toString();
                        String birthday = birthdayE.getText().toString();
                        String gender;

//                        Map<String, Object> data = new HashMap<>();
//                        data.put("birthday", birthday);
//                        data.put("gender", gender);
//                        data.put("mother_id", mother_id);
                        if(male_chip.isChecked()){
                            gender = "Samiec";
                        }else{
                            gender = "Samica";
                        }

                        CattleModel model = new CattleModel(animal_id, birthday, gender, mother_id);

                        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                                .collection("cattle").document(animal_id).set(model);
//                        listener.applyTexts(animal_id, birthday, gender);
                    }
                });

        animal_idE = view.findViewById(R.id.animal_id);
        mother_idE = view.findViewById(R.id.mother_id);
        birthdayE = view.findViewById(R.id.birthday);
        male_chip = view.findViewById(R.id.male);
        male_chip.setChecked(true);

        return builder.create();
    }



    public interface ExampleDialogListener {
        void applyTexts(String animal_id, String birthday, String gender);
    }
}
