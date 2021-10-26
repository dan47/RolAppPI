package com.example.rolapppi.ui.cropProtection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rolapppi.R;
import com.example.rolapppi.ui.cattle.CattleModel;
import com.example.rolapppi.ui.cattle.CattleViewModel;
import com.example.rolapppi.ui.cattle.ScannerCode;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AddCropProtectionDialog extends AppCompatDialogFragment {


    private Button mPickDateButton;
    private CropProtectionViewModel cropProtectionViewModel;
    private CropProtectionModel cropProtectionModel;
    private Button submitBtnE;
    private Boolean edit;
    private TextView treatmentTimeA;
    private AutoCompleteTextView cropA, areaA, protectionProductA, doseA, reasonA;
    private ArrayAdapter arrayAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_crop_protection, null);

        mPickDateButton = view.findViewById(R.id.treatmentBtn);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Wybierz datę");
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");


        final MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H).build();

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


                        treatmentTimeA.setText(simpleFormat.format(date));
                        materialTimePicker.show(getParentFragmentManager(), "fragment_tag");

                    }
                });
        materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
            String temp = treatmentTimeA.getText().toString();
            int hour = materialTimePicker.getHour();
            int min = materialTimePicker.getMinute();
            StringBuilder sb = new StringBuilder();
            if (hour < 10) {
                sb.append(0).append(hour);
            } else {
                sb.append(hour);
            }
            sb.append(":");
            if (min < 10) {
                sb.append(0).append(min);
            } else {
                sb.append(min);
            }

            treatmentTimeA.setText(sb + " " + temp);
        });


        treatmentTimeA = view.findViewById(R.id.treatmentTime);
        submitBtnE = view.findViewById(R.id.submitBtn);

        cropA = view.findViewById(R.id.autoCompleteCrop);
        areaA = view.findViewById(R.id.autoCompleteArea);
        protectionProductA = view.findViewById(R.id.autoCompleteProtectionProduct);
        doseA = view.findViewById(R.id.autoCompleteDose);
        reasonA = view.findViewById(R.id.autoCompleteReason);

        SimpleDateFormat simpleFormat2 = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        Date date = new Date();
        treatmentTimeA.setText(simpleFormat2.format(date));

        cropProtectionViewModel = new ViewModelProvider(requireActivity()).get(CropProtectionViewModel.class);

        try {
            cropProtectionModel = cropProtectionViewModel.selected.getValue();
            cropA.setText(cropProtectionModel.getCrop());
            areaA.setText(cropProtectionModel.getArea());
            protectionProductA.setText(cropProtectionModel.getProtectionProduct());
            doseA.setText(cropProtectionModel.getDose());
            reasonA.setText(cropProtectionModel.getReason());

            edit = true;
        } catch (Exception e) {
            edit = false;
        }

        List<CropProtectionModel> temp_models_list = cropProtectionViewModel.getLiveDatafromFireStore().getValue();
        List<String> crops = temp_models_list.stream().map(e->e.getCrop()).distinct().collect(Collectors.toList());
        List<String> areas = temp_models_list.stream().map(e->e.getArea()).distinct().collect(Collectors.toList());
        List<String> protectionProducts = temp_models_list.stream().map(e->e.getProtectionProduct()).distinct().collect(Collectors.toList());
        List<String> doses = temp_models_list.stream().map(e->e.getDose()).distinct().collect(Collectors.toList());
        List<String> reasons = temp_models_list.stream().map(e->e.getReason()).distinct().collect(Collectors.toList());

        if (edit) {
            builder.setView(view)
                    .setTitle("Ewidencja edycja zabiegu");
        } else {
            builder.setView(view)
                    .setTitle("Ewidencja dodanie zabiegu");
        }



        cropA.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, crops));
        areaA.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, areas));
        protectionProductA.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, protectionProducts));
        doseA.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, doses));
        reasonA.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, reasons));


        submitBtnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String treatmentTime = treatmentTimeA.getText().toString();
                String crop = cropA.getText().toString();
                String area = areaA.getText().toString();
                String protectionProduct = protectionProductA.getText().toString();
                String dose = doseA.getText().toString();
                String reason = reasonA.getText().toString();


                if (TextUtils.isEmpty(crop)) {
                    cropA.setError("Proszę wprowadzić nazwę uprawy");
                    return;
                }
                if (TextUtils.isEmpty(area)) {
                    areaA.setError("Proszę wprowadzić powierzchnie w ha");
                    return;
                }
                if (TextUtils.isEmpty(protectionProduct)) {
                    protectionProductA.setError("Proszę wprowadzić nazwę produktu");
                    return;
                }
                if (TextUtils.isEmpty(dose)) {
                    doseA.setError("Proszę wprowadzić dawkę środkka (l/ha)");
                    return;
                }
                if (TextUtils.isEmpty(reason)) {
                    reasonA.setError("Proszę wprowadzić przyczyny zastosowania środka");
                    return;
                }


                if (edit) {
                    CropProtectionModel model = new CropProtectionModel(cropProtectionModel.getId(), treatmentTime, crop, area, protectionProduct, dose, reason);
                    cropProtectionViewModel.cropProtectionEdit(model);
                } else {
                    CropProtectionModel model = new CropProtectionModel(treatmentTime, crop, area, protectionProduct, dose, reason);
                    cropProtectionViewModel.cropProtectionAdd(model);
                }
                dismiss();

            }
        });

        return builder.create();
    }

}