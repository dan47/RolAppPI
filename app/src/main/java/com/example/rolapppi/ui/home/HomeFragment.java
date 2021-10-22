package com.example.rolapppi.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rolapppi.R;
import com.example.rolapppi.databinding.FragmentHomeBinding;
import com.example.rolapppi.ui.cattle.CattleModel;
import com.example.rolapppi.ui.cattle.CattleViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private CattleViewModel viewModel;
    private TextView countCattle, countGender, countCalving;


    public HomeFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        countCattle = view.findViewById(R.id.countCattle);
        countGender = view.findViewById(R.id.countGender);
        countCalving = view.findViewById(R.id.countCalving);

        viewModel = new ViewModelProvider(requireActivity()).get(CattleViewModel.class);

        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<List<CattleModel>>() {
            @Override
            public void onChanged(List<CattleModel> cattleModels) {
                countCattle.setText(Integer.toString(cattleModels.size()));
                List<CattleModel> countG = new ArrayList<>();
                cattleModels.stream().filter(e-> e.getGender().equals("Samica")).forEach(e-> countG.add(e));
                countGender.setText(Integer.toString(countG.size())+"/"+Integer.toString(cattleModels.size()-countG.size()));
                countG.clear();
                cattleModels.stream().filter(e-> !e.getCaliving().isEmpty()).forEach(e-> countG.add(e));
                countCalving.setText(Integer.toString(countG.size()));
//                cattleModels.sort((d1, d2) -> LocalDate.parse(d1.getBirthday(), formatter).compareTo(LocalDate.parse(d2.getBirthday(), formatter)));
//                myAdapter.setCattleModelData(cattleModels);
//                myAdapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                recyclerView.setAnimation(fadein);
//                progressBar.setAnimation(fadeout);
            }
        });
    }

}