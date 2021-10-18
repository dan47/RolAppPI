package com.example.rolapppi.ui.cattle;

import android.app.Dialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;


public class CattleFragment extends Fragment implements MyAdapter.OnModelListener{

    private DetailsCattleFragment detailsCattleFragment = new DetailsCattleFragment();
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    CattleViewModel viewModel;
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

//        fadein = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//        fadeout= AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        progressBar = view.findViewById(R.id.progressBarCattleFragment);

        myAdapter = new MyAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapter);



        progressBar.setVisibility(View.INVISIBLE);
        navController = Navigation.findNavController(view);
        addBtn = view.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddCattleDialog exampleDialog = new AddCattleDialog();
                        exampleDialog.show(getParentFragmentManager() , "example dialog");
                    }
                });
        progressBar.setVisibility(View.VISIBLE);
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

    }




    @Override
    public void onModelClick(int position) {

        viewModel.setSelected(myAdapter.cattleModelList.get(position));
        navController.navigate(R.id.action_nav_cattle_to_detailsCattleFragment);
    }
}