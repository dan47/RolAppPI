package com.example.rolapppi.fragments.home;

import static java.time.temporal.ChronoUnit.DAYS;

import android.content.Intent;
import android.graphics.Color;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.rolapppi.R;
import com.example.rolapppi.fragments.cattle.CattleFragment;
import com.example.rolapppi.fragments.cattle.CattleModel;
import com.example.rolapppi.fragments.cattle.CattleViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private CattleViewModel viewModel;
    private TextView countCattle, countGender, countCalving, countDryness, text_countDryness;
    private PieChart cattlePieChart, calvingPieChart;
    private NavController navController;

    public HomeFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void setupPieChart(PieChart pieChart, String name) {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText(name);
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        countCattle = view.findViewById(R.id.countCattle);
        countGender = view.findViewById(R.id.countGender);
        countCalving = view.findViewById(R.id.countCalving);
        countDryness = view.findViewById(R.id.countDryness);
        text_countDryness = view.findViewById(R.id.text_countDryness);

        cattlePieChart = view.findViewById(R.id.cattlePieChart);
        calvingPieChart = view.findViewById(R.id.calvingPieChart);
        setupPieChart(cattlePieChart, "Bydło");
        setupPieChart(calvingPieChart, "Zacielone");
        viewModel = new ViewModelProvider(requireActivity()).get(CattleViewModel.class);

        text_countDryness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("filtr", "3");
                navController.navigate(R.id.nav_cattle, bundle);
            }
        });


        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<List<CattleModel>>() {
            @Override
            public void onChanged(List<CattleModel> cattleModels) {

                countCattle.setText(Integer.toString(cattleModels.size()));

                List<CattleModel> countG = new ArrayList<>();

                cattleModels.stream().filter(e -> e.getGender().equals("Samica")).forEach(e -> countG.add(e));
                int female = countG.size();
                int male = cattleModels.size() - countG.size();
                countGender.setText(Integer.toString(female) + "/" + Integer.toString(male));


                countG.clear();
                cattleModels.stream().filter(e -> !e.getCaliving().isEmpty()&&e.getGender().equals("Samica")).forEach(e -> countG.add(e));
                int calving = countG.size();
                countCalving.setText(Integer.toString(calving));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                int dryness = (int) countG.stream().filter(e -> DAYS.between(java.time.LocalDate.parse(e.getCaliving(), formatter), LocalDate.now()) > 235).count();
                countDryness.setText((Integer.toString(dryness)));
//                AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//                int i = 0;
//
//                for(CattleModel item:countG){
//                Date currentTime = Calendar.getInstance().getTime();
//                currentTime = DateUtils.addMinutes(currentTime, 5);
//                    Date date1 = null;
//                    try {
//                         date1=new SimpleDateFormat("dd.MM.yyyy").parse(item.getCaliving());
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    int requestCode = (int)date1.getTime()/1000;
//
//                    Intent intent = new Intent(getContext(), AlarmReceiver.class);
//                    intent.putExtra("NAME", item.getAnimal_id());
//                    intent.putExtra("REQUEST_CODE", requestCode);
//                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//                    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//
//                    i = i + 1;
//
//                    long nowy = System.currentTimeMillis() + (10000 * i);
//
//                    PendingIntent pi = PendingIntent.getBroadcast(getContext(), requestCode, intent,  PendingIntent.FLAG_ONE_SHOT);
//                    am.set(AlarmManager.RTC_WAKEUP, nowy, pi);
//                Toast.makeText(getContext(), "Alarm set "+item.getAnimal_id()+ " " + requestCode + " " + nowy, Toast.LENGTH_SHORT).show();
//                }

                ArrayList<Integer> colors = new ArrayList<>();
                for (int color : ColorTemplate.MATERIAL_COLORS) {
                    colors.add(color);
                }
                for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                    colors.add(color);
                }

                List<PieEntry> cattleEntries = new ArrayList<>();
                cattleEntries.add(new PieEntry(male, "Samce"));
                cattleEntries.add(new PieEntry(female, "Samice"));

                PieDataSet pieDataSet = new PieDataSet(cattleEntries, "Legenda");
                pieDataSet.setColors(colors);

                PieData data = new PieData(pieDataSet);
                data.setDrawValues(true);
                data.setValueFormatter(new PercentFormatter(cattlePieChart));
                data.setValueTextSize(12f);
                data.setValueTextColor(Color.BLACK);

                cattlePieChart.setData(data);
                cattlePieChart.invalidate();

                List<PieEntry> calvingEntries = new ArrayList<>();
                calvingEntries.add(new PieEntry(female - calving, "Niezacielone"));
                calvingEntries.add(new PieEntry(calving, "Zacielone"));

                PieDataSet calvingpieDataSet = new PieDataSet(calvingEntries, "Legenda");
                calvingpieDataSet.setColors(colors);

                PieData calvingData = new PieData(calvingpieDataSet);
                calvingData.setDrawValues(true);
                calvingData.setValueFormatter(new PercentFormatter(calvingPieChart));
                calvingData.setValueTextSize(12f);
                calvingData.setValueTextColor(Color.BLACK);

                calvingPieChart.setData(calvingData);
                calvingPieChart.invalidate();


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