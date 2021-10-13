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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolapppi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class CattleFragment extends Fragment implements MyAdapter.OnItemClicked{



    RecyclerView recyclerView;
    MyAdapter myAdapter;
    CattleViewModel viewModel;
    FloatingActionButton addBtn;
    ProgressBar progressBar;
    Animation fadein;
    Animation fadeout;

    public CattleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        myAdapter = new MyAdapter((MyAdapter.OnItemClicked) this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapter);

        addBtn = view.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddCattleDialog exampleDialog = new AddCattleDialog();
                        exampleDialog.show(getParentFragmentManager() , "example dialog");
                    }
                });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(CattleViewModel.class);
        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<List<CattleModel>>() {
            @Override
            public void onChanged(List<CattleModel> cattleModels) {

                myAdapter.setCattleModelData(cattleModels);
                myAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
//                recyclerView.setAnimation(fadein);
//                progressBar.setAnimation(fadeout);


            }
        });
    }

    @Override
    public void somethingClicked(int position) {



//        ListFragmentDirections.ActionListFragmentToDetailFragment action = ListFragmentDirections.actionListFragmentToDetailFragment();
//        action.setPosition(position);
//        navController.navigate(action);

    }
}