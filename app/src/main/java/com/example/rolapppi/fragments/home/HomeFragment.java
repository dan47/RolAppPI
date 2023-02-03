package com.example.rolapppi.fragments.home;

import static java.time.temporal.ChronoUnit.DAYS;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolapppi.R;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements HAdapter.OnModelListener {

    CattleViewModel viewModel;
    TextView text_countCattle, text_countGenderMale, text_countGenderFemale, text_countCaliving, text_countDryness, text_countFeed, drynessTextResult, drynessRecently, feedRecently;
    private TextView countCattle, countGender, countCalving, countDryness, countFeed;
    private PieChart cattlePieChart, calvingPieChart;
    private NavController navController;
    private RecyclerView cattleDrynessRecyclerView, cattleDrynessRecentlyRecyclerView, cattleFeedRecentlyRecyclerView;
    private HAdapter hAdapterBefore, hAdapterNow, hAdapterAfter;


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
        cattleDrynessRecyclerView = view.findViewById(R.id.cattleDrynessRecyclerView);
        cattleDrynessRecentlyRecyclerView = view.findViewById(R.id.cattleDrynessRecentlyRecyclerView);
        cattleFeedRecentlyRecyclerView = view.findViewById(R.id.cattleFeedRecentlyRecyclerView);
        drynessTextResult = view.findViewById(R.id.drynessTextResult);
        drynessRecently = view.findViewById(R.id.drynessRecently);
        feedRecently = view.findViewById(R.id.feedRecently);

        cattleDrynessRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cattleDrynessRecyclerView.setHasFixedSize(true);
        cattleDrynessRecentlyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cattleDrynessRecentlyRecyclerView.setHasFixedSize(true);
        cattleFeedRecentlyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cattleFeedRecentlyRecyclerView.setHasFixedSize(true);


        hAdapterBefore = new HAdapter(this, "Before");
        hAdapterNow = new HAdapter(this, "Now");
        hAdapterAfter = new HAdapter(this, "After");
        cattleDrynessRecyclerView.setAdapter(hAdapterBefore);
        cattleDrynessRecentlyRecyclerView.setAdapter(hAdapterNow);
        cattleFeedRecentlyRecyclerView.setAdapter(hAdapterAfter);

        countCattle = view.findViewById(R.id.countCattle);
        countGender = view.findViewById(R.id.countGender);
        countCalving = view.findViewById(R.id.countCalving);
        countDryness = view.findViewById(R.id.countDryness);
        countFeed = view.findViewById(R.id.countFeed);

        text_countCattle = view.findViewById(R.id.text_countCattle);
        text_countGenderFemale = view.findViewById(R.id.text_countGenderFemale);
        text_countGenderMale = view.findViewById(R.id.text_countGenderMale);
        text_countCaliving = view.findViewById(R.id.text_countCaliving);
        text_countDryness = view.findViewById(R.id.text_countDryness);
        text_countFeed = view.findViewById(R.id.text_countFeed);

        cattlePieChart = view.findViewById(R.id.cattlePieChart);
        calvingPieChart = view.findViewById(R.id.calvingPieChart);
        setupPieChart(cattlePieChart, "Bydło");
        setupPieChart(calvingPieChart, "Zacielone");
        viewModel = new ViewModelProvider(requireActivity()).get(CattleViewModel.class);

        text_countCattle.setOnClickListener(view1 -> navController.navigate(R.id.nav_cattle));
        text_countGenderFemale.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("filtr", "0");
            navController.navigate(R.id.nav_cattle, bundle);
        });
        text_countGenderMale.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("filtr", "1");
            navController.navigate(R.id.nav_cattle, bundle);
        });
        text_countCaliving.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("filtr", "2");
            navController.navigate(R.id.nav_cattle, bundle);
        });
        text_countDryness.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("filtr", "3");
            navController.navigate(R.id.nav_cattle, bundle);
        });
        text_countFeed.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("filtr", "4");
            navController.navigate(R.id.nav_cattle, bundle);
        });


        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), cattleModels -> {

            countCattle.setText(String.format("%s", cattleModels.size()));

            List<CattleModel> countG = new ArrayList<>();
            List<CattleModel> countG2 = new ArrayList<>(); // zasuszenie za/po

            cattleModels.stream().filter(e -> e.getGender().equals("Samica")).forEach(countG::add);
            int female = countG.size();
            int male = cattleModels.size() - countG.size();
            countGender.setText(getString(R.string.female_male, female, male));


            countG.clear();
            cattleModels.stream().filter(e -> !e.getCaliving().isEmpty() && e.getGender().equals("Samica")).forEach(countG::add);

            int calving = countG.size();
            countCalving.setText(String.format("%s", calving));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            countG.sort(Comparator.comparingLong(a -> a.getDurationCalving()));
            Collections.reverse(countG);

            //            countG2 = countG.stream().filter(e -> DAYS.between(LocalDate.parse(e.getCaliving(), formatter), LocalDate.now()) > 235).filter(e -> DAYS.between(LocalDate.parse(e.getCaliving(), formatter), LocalDate.now()) < 243).collect(Collectors.toList());
            countG2 = countG.stream().filter(e -> e.getDurationCalving() > 221).filter(e -> e.getDurationCalving() < 236).collect(Collectors.toList());

            hAdapterBefore.setCattleModelData(countG2);
            hAdapterBefore.notifyDataSetChanged();

            if (countG2.size() == 0) {
                drynessTextResult.setVisibility(View.VISIBLE);
            } else {
                drynessTextResult.setVisibility(View.GONE);
            }

            countG2 = countG.stream().filter(e -> e.getDurationCalving() > 235).collect(Collectors.toList());
            hAdapterNow.setCattleModelData(countG2);
            hAdapterNow.notifyDataSetChanged();

            int dryness = countG2.size();
            countDryness.setText((Integer.toString(dryness)));

            if (dryness != 0) {
                drynessRecently.setVisibility(View.VISIBLE);
            } else {
                drynessRecently.setVisibility(View.GONE);
            }

            countG.clear();
            cattleModels.stream().filter(e -> e.getCaliving().isEmpty() && e.getGender().equals("Samica") && !e.getPreviousCaliving().isEmpty()).forEach(countG::add);

            countG2 = countG.stream().filter(e -> e.getDurationCalving() < 90).collect(Collectors.toList());
            hAdapterAfter.setCattleModelData(countG2);
            hAdapterAfter.notifyDataSetChanged();

            int feed = countG2.size();
            countFeed.setText((Integer.toString(feed)));

            if (feed != 0) {
                feedRecently.setVisibility(View.VISIBLE);
            } else {
                feedRecently.setVisibility(View.GONE);
            }

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
        });
    }

    @Override
    public void onModelClick(int position, String type) {
        switch(type){
            case "Before":
                viewModel.setSelected(hAdapterBefore.cattleModelList.get(position));
                navController.navigate(R.id.action_nav_home_to_detailsCattleFragment);
                break;
            case "Now":
                viewModel.setSelected(hAdapterNow.cattleModelList.get(position));
                navController.navigate(R.id.action_nav_home_to_detailsCattleFragment);
                break;
            case "After":
                viewModel.setSelected(hAdapterAfter.cattleModelList.get(position));
                navController.navigate(R.id.action_nav_home_to_detailsCattleFragment);
                break;
        }

    }

}