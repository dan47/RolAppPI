package com.example.rolapppi.ui.cropProtection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.rolapppi.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AddCropProtectionDialog extends AppCompatDialogFragment {


    private Button mPickDateButton;
    private CropProtectionViewModel cropProtectionViewModel;
    private CropProtectionModel cropProtectionModel;
    private Button submitBtnE;
    private Boolean edit;
    private TextView treatmentTime;
    private AutoCompleteTextView crop, area, protectionProduct, dose, reason;

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


                        treatmentTime.setText(simpleFormat.format(date));
                        materialTimePicker.show(getParentFragmentManager(), "fragment_tag");

                    }
                });
        materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
            String temp = treatmentTime.getText().toString();
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

            treatmentTime.setText(sb + " " + temp);
        });


        treatmentTime = view.findViewById(R.id.treatmentTime);
        submitBtnE = view.findViewById(R.id.submitBtn);

        crop = view.findViewById(R.id.autoCompleteCrop);
        area = view.findViewById(R.id.autoCompleteArea);
        protectionProduct = view.findViewById(R.id.autoCompleteProtectionProduct);
        dose = view.findViewById(R.id.autoCompleteDose);
        reason = view.findViewById(R.id.autoCompleteReason);

        SimpleDateFormat simpleFormat2 = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        Date date = new Date();
        treatmentTime.setText(simpleFormat2.format(date));

        cropProtectionViewModel = new ViewModelProvider(requireActivity()).get(CropProtectionViewModel.class);

        try {
            cropProtectionModel = cropProtectionViewModel.selected.getValue();
            crop.setText(cropProtectionModel.getCrop());
            area.setText(cropProtectionModel.getArea());
            protectionProduct.setText(cropProtectionModel.getProtectionProduct());
            dose.setText(cropProtectionModel.getDose());
            reason.setText(cropProtectionModel.getReason());
            treatmentTime.setText(cropProtectionModel.getTreatmentTime());

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



        crop.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, crops));
        area.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, areas));
        protectionProduct.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, protectionProducts));
        dose.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, doses));
        reason.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, reasons));


        submitBtnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String treatmentTimeString = treatmentTime.getText().toString();
                String cropString = crop.getText().toString();
                String areaString = area.getText().toString();
                String protectionProductString = protectionProduct.getText().toString();
                String doseString = dose.getText().toString();
                String reasonString = reason.getText().toString();


                if (TextUtils.isEmpty(cropString)) {
                    crop.setError("Proszę wprowadzić nazwę uprawy");
                    return;
                }
                if (TextUtils.isEmpty(areaString)) {
                    area.setError("Proszę wprowadzić powierzchnie w ha");
                    return;
                }
                if (TextUtils.isEmpty(protectionProductString)) {
                    protectionProduct.setError("Proszę wprowadzić nazwę produktu");
                    return;
                }
                if (TextUtils.isEmpty(doseString)) {
                    dose.setError("Proszę wprowadzić dawkę środkka (l/ha)");
                    return;
                }
                if (TextUtils.isEmpty(reasonString)) {
                    reason.setError("Proszę wprowadzić przyczyny zastosowania środka");
                    return;
                }


                if (edit) {
                    CropProtectionModel model = new CropProtectionModel(cropProtectionModel.getId(), treatmentTimeString, cropString, areaString, protectionProductString, doseString, reasonString);
                    cropProtectionViewModel.cropProtectionEdit(model);
                } else {
                    CropProtectionModel model = new CropProtectionModel(treatmentTimeString, cropString, areaString, protectionProductString, doseString, reasonString);
                    cropProtectionViewModel.cropProtectionAdd(model);
                }
                dismiss();

            }
        });

        return builder.create();
    }

}