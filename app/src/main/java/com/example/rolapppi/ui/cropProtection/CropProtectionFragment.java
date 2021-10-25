package com.example.rolapppi.ui.cropProtection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.rolapppi.R;
import com.example.rolapppi.ui.cattle.AddCattleDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CropProtectionFragment extends Fragment implements CpAdapter.OnModelListener{


    RecyclerView recyclerView;
    CpAdapter cpAdapter;
    CropProtectionViewModel viewModel;
    FloatingActionButton addBtn;
    ProgressBar progressBar;
    private Spinner spinner;
    private NavController navController;

    public CropProtectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crop_protection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.cattleRecyclerView);
        addBtn = view.findViewById(R.id.addBtn);
        progressBar = view.findViewById(R.id.progressBarCattleFragment);
        spinner = view.findViewById(R.id.spinner);

        cpAdapter = new CpAdapter( this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cpAdapter);


        navController = Navigation.findNavController(view);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCropProtectionDialog exampleDialog = new AddCropProtectionDialog();
                exampleDialog.show(getParentFragmentManager(), "example dialog");
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        viewModel = new ViewModelProvider(requireActivity()).get(CropProtectionViewModel.class);
        viewModel.selected.setValue(null);

        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<List<CropProtectionModel>>() {
            @Override
            public void onChanged(List<CropProtectionModel> cropProtectionModels) {
                cropProtectionModels.sort((d1, d2) -> LocalDateTime.parse(d1.getTreatmentTime(), formatter).compareTo(LocalDateTime.parse(d2.getTreatmentTime(), formatter)));
                Collections.reverse(cropProtectionModels);
                List<String> years = new ArrayList<>();
                years.add("Wszystko");
                years.addAll(cropProtectionModels.stream().map(e->e.getTreatmentTime().substring(6,10)).distinct().collect(Collectors.toList()));
                cpAdapter.setCropProtectionModelData(cropProtectionModels);
                cpAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(years.size()!=1){
                    spinner.setVisibility(View.VISIBLE);
                }else{
                    spinner.setVisibility(View.INVISIBLE);
                }
                spinner.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, years));
//                recyclerView.setAnimation(fadein);
//                progressBar.setAnimation(fadeout);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cpAdapter.getFilter().filter( adapterView.getItemAtPosition(i).toString());

                try {
                    //Your task here
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onModelClick(int position) {
        viewModel.setSelected(cpAdapter.cropProtectionModelList.get(position));
        navController.navigate(R.id.action_nav_cropProtection_to_detailsCropProtectionFragment);
    }
}