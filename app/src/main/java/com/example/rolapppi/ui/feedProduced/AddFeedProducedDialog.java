package com.example.rolapppi.ui.feedProduced;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.rolapppi.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class AddFeedProducedDialog extends AppCompatDialogFragment {

    private Button mPickDateButton;
    private FeedProducedViewModel feedProducedViewModel;
    private FeedProducedModel feedProducedModel;
    private Button submitBtnE;
    private Boolean edit;
    private TextView acquisition;
    private AutoCompleteTextView nameFeed, origin, count, destination, cattleType;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_feed_produced, null);

        mPickDateButton = view.findViewById(R.id.acquisitionDateBtn);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Wybierz datę");
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");


        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Date date = new Date((Long) selection);
                        acquisition.setText(simpleFormat.format(date));
                    }
                });


        acquisition = view.findViewById(R.id.acquisitionDate);
        submitBtnE = view.findViewById(R.id.submitBtn);

        nameFeed = view.findViewById(R.id.autoCompleteNameFeed);
        origin = view.findViewById(R.id.autoCompleteOrigin);
        count = view.findViewById(R.id.autoCompleteCount);
        destination = view.findViewById(R.id.autoCompleteDestination);
        cattleType = view.findViewById(R.id.autoCompletedCattleType);


        Date date = new Date();
        acquisition.setText(simpleFormat.format(date));

        feedProducedViewModel = new ViewModelProvider(requireActivity()).get(FeedProducedViewModel.class);

        try {
            feedProducedModel = feedProducedViewModel.selected.getValue();
            nameFeed.setText(feedProducedModel.getNameFeed());
            origin.setText(feedProducedModel.getOrigin());
            count.setText(feedProducedModel.getCount());
            destination.setText(feedProducedModel.getDestination());
            cattleType.setText(feedProducedModel.getCattleType());

            acquisition.setText(feedProducedModel.getAcquisition());

            edit = true;
        } catch (Exception e) {
            edit = false;
        }

        List<FeedProducedModel> temp_models_list = feedProducedViewModel.getLiveDatafromFireStore().getValue();
        List<String> nameFeeds = temp_models_list.stream().map(e -> e.getNameFeed()).distinct().collect(Collectors.toList());
        List<String> origins = temp_models_list.stream().map(e -> e.getOrigin()).distinct().collect(Collectors.toList());
        List<String> destinations = temp_models_list.stream().map(e -> e.getDestination()).distinct().collect(Collectors.toList());
        List<String> cattleTypes = temp_models_list.stream().map(e -> e.getCattleType()).distinct().collect(Collectors.toList());

        if (edit) {
            builder.setView(view)
                    .setTitle("Rejestr produkowanych pasz edycja");
        } else {
            builder.setView(view)
                    .setTitle("Rejestr produkowanych pasz dodanie");
        }


        origin.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, origins));
        nameFeed.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nameFeeds));
        destination.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, destinations));
        cattleType.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, cattleTypes));


        submitBtnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String acquisitionString = acquisition.getText().toString();
                String nameFeedString = nameFeed.getText().toString();
                String originString = origin.getText().toString();
                String countString = count.getText().toString();
                String destinationString = destination.getText().toString();
                String cattleTypeString = cattleType.getText().toString();



                if (TextUtils.isEmpty(nameFeedString)) {
                    nameFeed.setError("Proszę wprowadzić nazwę paszy");
                    return;
                }
                if (TextUtils.isEmpty(originString)) {
                    origin.setError("Proszę wprowadzić pochodzenie");
                    return;
                }
                if (TextUtils.isEmpty(countString)) {
                    count.setError("Proszę wprowadzić ilość paszy");
                    return;
                }
                if (TextUtils.isEmpty(destinationString)) {
                    destination.setError("Proszę wprowadzić przeznaczenie");
                    return;
                }
                if (TextUtils.isEmpty(cattleTypeString)) {
                    cattleType.setError("Proszę wprowadzić rodzaj zwierząt");
                    return;
                }
                String weightString = String.valueOf(Integer.parseInt(countString) * 0.2) + " ton";

                if (edit) {
                    FeedProducedModel model = new FeedProducedModel(feedProducedModel.getId(), nameFeedString, originString, countString, weightString, destinationString, cattleTypeString, acquisitionString);
                    feedProducedViewModel.feedProducedEdit(model);
                } else {
                    FeedProducedModel model = new FeedProducedModel(nameFeedString, originString, countString, weightString, destinationString, cattleTypeString, acquisitionString);
                    feedProducedViewModel.feedProducedAdd(model);
                }
                dismiss();

            }
        });

        return builder.create();
    }
}