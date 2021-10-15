package com.example.rolapppi.ui.cattle;

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
import android.widget.TextView;

import com.example.rolapppi.R;
import com.example.rolapppi.databinding.FragmentDetailsCattleBinding;

public class DetailsCattleFragment extends Fragment {

    TextView animal_id, birthday, gender, mother_id;
    Button editBtn, deleteBtn;
    private NavController navController;
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
        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        cattleViewModel.getSelected().observe(getViewLifecycleOwner(), cattleModel -> {
            animal_id.setText(cattleModel.getAnimal_id());
            birthday.setText(cattleModel.getBirthday());
            gender.setText(cattleModel.getGender());
            mother_id.setText(cattleModel.getMother_id());
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCattleDialog exampleDialog = new AddCattleDialog();
                exampleDialog.show(getParentFragmentManager() , "example dialog");
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