package com.example.rolapppi.fragments.cropProtection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rolapppi.R;


public class DetailsCropProtectionFragment extends Fragment {

    TextView crop, treatmentTime, area, protectionProduct, dose, reason;
    Button editBtn, deleteBtn;


    public DetailsCropProtectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_crop_protection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CropProtectionViewModel cropProtectionViewModel = new ViewModelProvider(requireActivity()).get(CropProtectionViewModel.class);
        crop = view.findViewById(R.id.crop);
        treatmentTime = view.findViewById(R.id.treatmentTime);
        area = view.findViewById(R.id.area);
        protectionProduct = view.findViewById(R.id.protectionProduct);
        dose = view.findViewById(R.id.dose);
        reason = view.findViewById(R.id.reason);

        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);

        cropProtectionViewModel.getSelected().observe(getViewLifecycleOwner(), cropProtectionModel -> {
            crop.setText(cropProtectionModel.getCrop());
            treatmentTime.setText(cropProtectionModel.getTreatmentTime());
            area.setText(cropProtectionModel.getArea());
            protectionProduct.setText(cropProtectionModel.getProtectionProduct());
            dose.setText(cropProtectionModel.getDose());
            reason.setText(cropProtectionModel.getReason());
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCropProtectionDialog exampleDialog = new AddCropProtectionDialog();
                exampleDialog.show(getParentFragmentManager(), "example dialog");
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropProtectionViewModel.cropProtectionDelete(cropProtectionViewModel.getSelected().getValue());
                getActivity().onBackPressed();
            }
        });
    }
}