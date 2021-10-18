package com.example.rolapppi.ui.cattle;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rolapppi.R;
import com.example.rolapppi.databinding.FragmentDetailsCattleBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailsCattleFragment extends Fragment {

    TextView animal_id, birthday, gender, mother_id, calving;
    LinearLayout calvingLayout;
    Button editBtn, deleteBtn, calvingBtn;

    public DetailsCattleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_cattle, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CattleViewModel cattleViewModel = new ViewModelProvider(requireActivity()).get(CattleViewModel.class);
        animal_id = view.findViewById(R.id.animal_id);
        birthday = view.findViewById(R.id.birthday);
        gender = view.findViewById(R.id.gender);
        mother_id = view.findViewById(R.id.mother_id);
        calving = view.findViewById(R.id.calving);
        calvingLayout = view.findViewById(R.id.calvingLayout);

        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        calvingBtn = view.findViewById(R.id.calvingBtn);


        cattleViewModel.getSelected().observe(getViewLifecycleOwner(), cattleModel -> {
            animal_id.setText(cattleModel.getAnimal_id());
            birthday.setText(cattleModel.getBirthday());
            gender.setText(cattleModel.getGender());
            mother_id.setText(cattleModel.getMother_id());
            if (cattleModel.getGender().equals(getString(R.string.female))) {
                String calvingT = cattleModel.getCaliving();
                if (calvingT.isEmpty()) {
                    calving.setText(getString(R.string.no_calivng));
                } else {
                    calving.setText(calvingT);
                }
                calvingLayout.setVisibility(View.VISIBLE);
                calvingBtn.setVisibility(View.VISIBLE);
            } else {
                calvingLayout.setVisibility(View.GONE);
                calvingBtn.setVisibility(View.GONE);
            }
        });

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Wybierz datę zacielenia");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        calvingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");
                        Date date = new Date((Long) selection);
                        calving.setText(simpleFormat.format(date));
                        cattleViewModel.addCalving(cattleViewModel.getSelected().getValue(), simpleFormat.format(date));
                    }
                });


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCattleDialog exampleDialog = new AddCattleDialog();
                exampleDialog.show(getParentFragmentManager(), "example dialog");
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cattleViewModel.cattleDelete(cattleViewModel.getSelected().getValue());
                getActivity().onBackPressed();
            }
        });
    }

}