package com.example.rolapppi.ui.cattle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolapppi.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CattleFragment extends Fragment implements MyAdapter.OnModelListener{

    private DetailsCattleFragment detailsCattleFragment = new DetailsCattleFragment();
    RecyclerView recyclerView;
    SearchView searchView;
    MyAdapter myAdapter;
    CattleViewModel viewModel;
    Button filterBtn;
    FloatingActionButton addBtn;
    ProgressBar progressBar;
    Animation fadein;
    Animation fadeout;
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

//        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);

//        fadein = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//        fadeout= AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        filterBtn = view.findViewById(R.id.filterBtn);
        addBtn = view.findViewById(R.id.addBtn);
        progressBar = view.findViewById(R.id.progressBarCattleFragment);

        myAdapter = new MyAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapter);




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
                myAdapter.setCattleModelData(cattleModels);
                myAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
//                recyclerView.setAnimation(fadein);
//                progressBar.setAnimation(fadeout);
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
                myAdapter.getFilter().filter(newText);
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
                                myAdapter.getFilter().filter(currentItem);
                                dialogInterface.dismiss();
                            }
                        });
                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myAdapter.getFilter().filter("");
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }


    @Override
    public void onModelClick(int position) {
        viewModel.setSelected(myAdapter.cattleModelList.get(position));
        searchView.setQuery("", false);
        searchView.setIconified(true);
        searchView.clearFocus();
        navController.navigate(R.id.action_nav_cattle_to_detailsCattleFragment);
    }
}