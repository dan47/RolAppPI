package com.example.rolapppi.ui.home;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private CattleViewModel viewModel;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button readFileBtn;
    private final int CHOOSE_FILE = 1001;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readFileBtn = view.findViewById(R.id.readFileBtn);

        readFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
//                intent.addCategory("application/csv");
//                startActivity(intent);
            startActivityForResult(intent, CHOOSE_FILE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_FILE && resultCode == RESULT_OK) {
            if ((data != null) ) {
                File file = new File(data.getData().getPath()) ;
                String path = file.getAbsolutePath().replace("/document/raw:","") ;
                Uri selectedFile = data.getData();
//                Uri selectedFile = Uri.parse(Environment.getExternalStorageDirectory()+"/Download/ListaZwierzat.csv");
                String src = selectedFile.getPath();
                Log.e("DIPA", path);
                String[] nextLine = new String[]{};
                try {
                    FileReader fileReader = new FileReader(path);
                    CSVReader reader = new CSVReader(fileReader);

                    String[] line = reader.readNext();

//                    List<CattleModel> cattleModelList = new ArrayList<CattleModel>();
                    viewModel = new ViewModelProvider(requireActivity()).get(CattleViewModel.class);
                    while (line != null) {
                        line = reader.readNext();
                        // nextLine[] is an array of values from the line
                        String[] lineN = line;
                        String[] dd = lineN[0].split(";");

                        if(lineN[0].contains("PARAMETRY")){
                            return;
                        }
                        dd[3] = dd[3].replace("-",".");
//                        cattleModelList.add(new CattleModel(dd[2], dd[3], dd[5], dd[18]));
                        viewModel.cattleAdd(new CattleModel(dd[2], dd[3], dd[5], dd[18]));

                    }

                } catch (Exception e) {
                    Log.e("DIPA", e.toString());;
                }
//                String[] modifiedArray = Arrays.copyOfRange(nextLine, 1, nextLine.length);
//                Arrays.stream(modifiedArray).

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}