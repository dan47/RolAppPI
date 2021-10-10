package com.example.rolapppi.ui.cattle;

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

import com.example.rolapppi.databinding.FragmentCattleBinding;

public class CattleFragment extends Fragment {

    private CattleViewModel cattleViewModel;
    private FragmentCattleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cattleViewModel =
                new ViewModelProvider(this).get(CattleViewModel.class);

        binding = FragmentCattleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.cowList;
        cattleViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}