package com.example.rolapppi.fragments.cattle.customCattle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.rolapppi.R;
import com.example.rolapppi.fragments.cattle.CAdapter;
import com.example.rolapppi.fragments.cattle.CattleModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class CustomCattleFragment extends Fragment implements CAdapter.OnModelListener {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private CAdapter cAdapter2;
    private CustomCattleViewModel viewModel;
    private Button filterBtn;
    private Spinner spinner;
    private FloatingActionButton deleteBtn;
    private ProgressBar progressBar;
    private NavController navController;

    public CustomCattleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_cattle, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.customCattleRecyclerView);

        filterBtn = view.findViewById(R.id.filterBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        spinner = view.findViewById(R.id.spinner);

        progressBar = view.findViewById(R.id.progressBarCustomCattleFragment);

        cAdapter2 = new CAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cAdapter2);


        navController = Navigation.findNavController(view);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteCustomCattleGroup(spinner.getSelectedItem().toString());
                cAdapter2.notifyDataSetChanged();
            }
        });


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        viewModel = new ViewModelProvider(requireActivity()).get(CustomCattleViewModel.class);
        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), cattleModels -> {

            List<String> years = new ArrayList<>();
            for (Map.Entry<String, List<CattleModel>> entry : cattleModels.entrySet()) {
                years.add(entry.getKey());
            }
            List<String> sortedNames = years.stream().sorted().collect(Collectors.toList());

            if (!years.isEmpty()) {
                cAdapter2.setCattleModelData(cattleModels.get(sortedNames.get(0)));
                cAdapter2.notifyDataSetChanged();
            }
            spinner.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, sortedNames));
            progressBar.setVisibility(View.GONE);
//                recyclerView.setAnimation(fadein);
//                progressBar.setAnimation(fadeout);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    cAdapter2.setCattleModelData(cattleModels.get(sortedNames.get(i)));
                    cAdapter2.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });

//        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<Map<String, List<CattleModel>>>() {
//            @Override
//            public void onChanged(Map<String, List<CattleModel>> cattleModels) {
//
//                List<String> years = new ArrayList<>();
//                for (Map.Entry<String, List<CattleModel>> entry : cattleModels.entrySet()) {
//                    years.add(entry.getKey());
//                }
//                List<String> sortedNames = years.stream().sorted().collect(Collectors.toList());
//
//                if (!years.isEmpty()) {
//                    cAdapter2.setCattleModelData(cattleModels.get(sortedNames.get(0)));
//                    cAdapter2.notifyDataSetChanged();
//                }
//                spinner.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, sortedNames));
//                progressBar.setVisibility(View.GONE);
////                recyclerView.setAnimation(fadein);
////                progressBar.setAnimation(fadeout);
//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                       cAdapter2.setCattleModelData(cattleModels.get(sortedNames.get(i)));
//                        cAdapter2.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });
//            }
//        });


    }

    @Override
    public void onModelClick(int position) {

    }

    @Override
    public boolean onModelLongClick(int position) {
        return false;
    }
}