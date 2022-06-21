package com.example.rolapppi.fragments.cattle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rolapppi.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DetailsCattleFragment extends Fragment {

    TextView animal_id, birthday, gender, mother_id, calving, previousCalving;
    LinearLayout calvingLayout, previousCalvingLayout;
    Button editBtn, deleteBtn, calvingBtn, calvingDeleteBtn;
    String previousCalvingString, dateBirth;

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
        calving = view.findViewById(R.id.calving);
        previousCalving = view.findViewById(R.id.previousCalving);
        calvingLayout = view.findViewById(R.id.calvingLayout);
        previousCalvingLayout = view.findViewById(R.id.previousCalvingLayout);

        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        calvingBtn = view.findViewById(R.id.calvingBtn);
        calvingDeleteBtn = view.findViewById(R.id.calvingDeleteBtn);

        //zmieniam observe na get - dla pozostałych modółów później //odmieniłem bo nie aktualizuje - bym musial wysylac info z alertdialog do fragmentu
        CattleModel cattleModel = cattleViewModel.getSelected().getValue(); //prawdopodobbnie niepotrzebn

        cattleViewModel.getSelected().observe(getViewLifecycleOwner(), cattleModelT -> {
            animal_id.setText(cattleModelT.getAnimal_id());
            birthday.setText(cattleModelT.getBirthday());
            gender.setText(cattleModelT.getGender());
            mother_id.setText(cattleModelT.getMother_id());

            dateBirth = cattleModelT.getBirthday();

            previousCalvingString = cattleModel.getPreviousCaliving();

            //Sprawdzam czy pole istnieje poniewaz baza nie jest zaaktualizowana - nie wszystkie cattle posiadają pole previousCaliving
            if (previousCalvingString == null) {
                previousCalvingString = "";
            }


            if (cattleModelT.getGender().equals(getString(R.string.female))) {
                String calvingT = cattleModelT.getCaliving();
                calvingDeleteBtn.setVisibility(View.GONE);
                if (calvingT.isEmpty()) {
                    calving.setText(getString(R.string.no_calivng));

                } else {
                    calving.setText(calvingT);
                    previousCalving.setText(calvingT);
                    calvingDeleteBtn.setVisibility(View.VISIBLE);
                }

                if (!previousCalvingString.isEmpty()) {
                    previousCalving.setText(previousCalvingString);
                } else {
                    previousCalving.setText(getString(R.string.no_calivng));
                }

                calvingLayout.setVisibility(View.VISIBLE);
                previousCalvingLayout.setVisibility(View.VISIBLE);
                calvingBtn.setVisibility(View.VISIBLE);

            } else {

                calvingLayout.setVisibility(View.GONE);
                previousCalvingLayout.setVisibility(View.GONE);
                calvingBtn.setVisibility(View.GONE);
            }
        });


        calvingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
                materialDateBuilder.setTitleText("Wybierz datę zacielenia");
                if (previousCalvingString.equals("") && calving.getText().toString().equals("Brak")) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Czy chcesz?")
                            .setMessage("Ustawić datę 9 miesięcy w tył? ")
                            .setPositiveButton("Tak", (dialogInterface, i) -> {
                                Date dateCalving = new Date();

                                LocalDateTime ldt = LocalDateTime.ofInstant(dateCalving.toInstant(), ZoneId.systemDefault());
                                dateCalving = Date.from(ldt.minusDays(275).atZone(ZoneId.systemDefault()).toInstant());

                                materialDateBuilder.setSelection(dateCalving.getTime());

                                MaterialDatePicker materialDatePicker = materialDateBuilder.build();
                                materialDatePicker = createListener(materialDatePicker);
                                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                            })
                            .setNegativeButton("Nie", (dialogInterface, i) -> {
                                Date dateCalving = new Date();

                                materialDateBuilder.setSelection(dateCalving.getTime());

                                MaterialDatePicker materialDatePicker = materialDateBuilder.build();
                                materialDatePicker = createListener(materialDatePicker);
                                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                            }).show();
                } else {
                    Date dateCalving = new Date();
                    materialDateBuilder.setSelection(dateCalving.getTime());
                    //Ustawiam od razu zaznaczony kalendarz na dniu zacielenia
                    // (bez ustawienia godziny, automatycznie ustawia 00, a gdy taką datę ustawie w materialDateBuilder (long), to daje dzień wcześniejszy)
                    if (!cattleModel.getCaliving().isEmpty()) {

                        try {
                            dateCalving = new SimpleDateFormat("dd.MM.yyyy/HH").parse(cattleModel.getCaliving() + "/12");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        materialDateBuilder.setSelection(dateCalving.getTime());
                    }

                    MaterialDatePicker materialDatePicker = materialDateBuilder.build();
                    materialDatePicker = createListener(materialDatePicker);
                    materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }

            //murzynska metoda
            private MaterialDatePicker createListener(MaterialDatePicker materialDatePicker) {

                materialDatePicker.addOnPositiveButtonClickListener(
                        selection -> {
                            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                            Date date = new Date((long) selection);

                            LocalDate dateCalvingTemp = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            LocalDate dateBirthTemp = LocalDate.parse(dateBirth, formatter);
                            long months = ChronoUnit.MONTHS.between(dateBirthTemp, dateCalvingTemp);
                            long weeks = 8;

                            if (!previousCalvingString.equals("")) {
                                LocalDate datePreviousCalvingTemp = LocalDate.parse(previousCalvingString, formatter);
                                weeks = ChronoUnit.WEEKS.between(datePreviousCalvingTemp, dateCalvingTemp);
                            }

                            if (weeks < 6) {

                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Czy jesteś pewny?")
                                        .setMessage("Jeszcze nie mineło 6 tygodni od ostatniego wycielenia! Ostatnie wycielenie: " + previousCalvingString)
                                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                calving.setText(simpleFormat.format(date));
                                                calvingDeleteBtn.setVisibility(View.VISIBLE);
                                                cattleViewModel.addCalving(cattleViewModel.getSelected().getValue(), simpleFormat.format(date));
                                            }
                                        })
                                        .setNegativeButton("Nie", (dialogInterface, i) -> {
                                        }).show();
                            } else if (months < 15) {

                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Czy jesteś pewny?")
                                        .setMessage("Zwierzę ma mniej niż 15 miesięcy! Urodzone: " + birthday.getText())
                                        .setPositiveButton("Tak", (dialogInterface, i) -> {
                                            calving.setText(simpleFormat.format(date));
                                            calvingDeleteBtn.setVisibility(View.VISIBLE);
                                            cattleViewModel.addCalving(cattleViewModel.getSelected().getValue(), simpleFormat.format(date));
                                        })
                                        .setNegativeButton("Nie", (dialogInterface, i) -> {

                                        })
                                        .show();
                            } else {
                                calving.setText(simpleFormat.format(date));
                                calvingDeleteBtn.setVisibility(View.VISIBLE);
                                cattleViewModel.addCalving(cattleViewModel.getSelected().getValue(), simpleFormat.format(date));
                            }
                        });
                return materialDatePicker;
            }
        });


        calvingDeleteBtn.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                .setTitle("Czy jesteś pewny?")
                .setMessage("Operacja usunięcia zacielenia ")
                .setPositiveButton("Tak", (dialogInterface, i) -> {
                    cattleViewModel.deleteCalving(cattleModel.getAnimal_id());
                    cattleViewModel.selected.getValue().setCaliving(""); //Pozbyć się, zajmuje pewnie za dużo - wymyślić lepsze zabezpieczenie/ podczas usuwania calving nie zmienia sie w edycji (selected), przez co po edycji powraca stary calving
                    calving.setText(getString(R.string.no_calivng));
                    calvingDeleteBtn.setVisibility(View.GONE);
                })
                .setNegativeButton("Nie", (dialogInterface, i) -> {

                })
                .show());

        editBtn.setOnClickListener(v -> {
            AddCattleDialog exampleDialog = new AddCattleDialog();
            exampleDialog.show(getParentFragmentManager(), "example dialog");
        });

        deleteBtn.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                .setTitle("Czy jesteś pewny?")
                .setMessage("Usunięcie zwierzęcia")
                .setPositiveButton("Tak", (dialogInterface, i) -> {
                    cattleViewModel.cattleDelete(cattleViewModel.getSelected().getValue());
                    getActivity().onBackPressed();
                })
                .setNegativeButton("Nie", (dialogInterface, i) -> {

                })
                .show());
    }


}