package com.example.rolapppi.fragments.cattle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;

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

import com.example.rolapppi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


public class CattleFragment extends Fragment implements CAdapter.OnModelListener{

    private RecyclerView recyclerView;
    private SearchView searchView;
    private CAdapter cAdapter;
    private CattleViewModel viewModel;
    private Button filterBtn;
    private FloatingActionButton addBtn;
    private ProgressBar progressBar;
    private NavController navController;

    public CattleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cattle, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.cattleRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        filterBtn = view.findViewById(R.id.filterBtn);
        addBtn = view.findViewById(R.id.addBtn);
        progressBar = view.findViewById(R.id.progressBarCattleFragment);

        cAdapter = new CAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cAdapter);


        navController = Navigation.findNavController(view);
        addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddCattleDialog exampleDialog = new AddCattleDialog();
                        exampleDialog.show(getParentFragmentManager() , "example dialog");
                    }
                });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        viewModel = new ViewModelProvider(requireActivity()).get(CattleViewModel.class);
        viewModel.selected.setValue(null);

        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<List<CattleModel>>() {
            @Override
            public void onChanged(List<CattleModel> cattleModels) {
                cattleModels.sort((d1, d2) -> LocalDate.parse(d1.getBirthday(), formatter).compareTo(LocalDate.parse(d2.getBirthday(), formatter)));
                cAdapter.setCattleModelData(cattleModels);
                cAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cAdapter.getFilter().filter(newText);
                return true;
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());

                String[] choicesArray =  new String[]{"Samica", "Samiec", "Zacielenie"};

                List<String> choicesList = Arrays.asList(choicesArray);
                builder.setTitle("Filtr")
                        .setSingleChoiceItems(choicesArray, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String currentItem = choicesList.get(i);
                                cAdapter.getFilter().filter(currentItem);
                                dialogInterface.dismiss();
                            }
                        });
                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cAdapter.getFilter().filter("");
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }


    @Override
    public void onModelClick(int position) {
        viewModel.setSelected(cAdapter.cattleModelList.get(position));
        searchView.setQuery("", false);
        searchView.setIconified(true);
        searchView.clearFocus();
        navController.navigate(R.id.action_nav_cattle_to_detailsCattleFragment);
    }
}