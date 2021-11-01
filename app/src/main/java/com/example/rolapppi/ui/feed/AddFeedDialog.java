package com.example.rolapppi.ui.feed;

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


public class AddFeedDialog extends AppCompatDialogFragment {
    private Button mPickDateButton;
    private FeedViewModel feedViewModel;
    private FeedModel feedModel;
    private Button submitBtnE;
    private Boolean edit;
    private TextView purchaseDate;
    private AutoCompleteTextView seller, producer, nameFeed, batch, count, packageType;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addfeed, null);

        mPickDateButton = view.findViewById(R.id.purchaseDateBtn);
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
                        purchaseDate.setText(simpleFormat.format(date));
                    }
                });


        purchaseDate = view.findViewById(R.id.purchaseDate);
        submitBtnE = view.findViewById(R.id.submitBtn);

        seller = view.findViewById(R.id.autoCompleteSeller);
        producer = view.findViewById(R.id.autoCompleteProducer);
        nameFeed = view.findViewById(R.id.autoCompleteNameFeed);
        batch = view.findViewById(R.id.autoCompleteBatch);
        count = view.findViewById(R.id.autoCompleteCount);
        packageType = view.findViewById(R.id.autoCompletePackageType);


        Date date = new Date();
        purchaseDate.setText(simpleFormat.format(date));

        feedViewModel = new ViewModelProvider(requireActivity()).get(FeedViewModel.class);

        try {
            feedModel = feedViewModel.selected.getValue();
            seller.setText(feedModel.getSeller());
            producer.setText(feedModel.getProducer());
            nameFeed.setText(feedModel.getNameFeed());
            batch.setText(feedModel.getBatch());
            count.setText(feedModel.getCount());
            packageType.setText(feedModel.getPackageType());

            purchaseDate.setText(feedModel.getPurchaseDate());

            edit = true;
        } catch (Exception e) {
            edit = false;
        }

        List<FeedModel> temp_models_list = feedViewModel.getLiveDatafromFireStore().getValue();
        List<String> sellers = temp_models_list.stream().map(e -> e.getSeller()).distinct().collect(Collectors.toList());
        List<String> producers = temp_models_list.stream().map(e -> e.getProducer()).distinct().collect(Collectors.toList());
        List<String> nameFeeds = temp_models_list.stream().map(e -> e.getNameFeed()).distinct().collect(Collectors.toList());
        List<String> batches = temp_models_list.stream().map(e -> e.getBatch()).distinct().collect(Collectors.toList());
        List<String> counts = temp_models_list.stream().map(e -> e.getCount()).distinct().collect(Collectors.toList());
        List<String> packagetypes = temp_models_list.stream().map(e -> e.getPackageType()).distinct().collect(Collectors.toList());

        if (edit) {
            builder.setView(view)
                    .setTitle("Rejestr pasz edycja");
        } else {
            builder.setView(view)
                    .setTitle("Rejestr pasz dodanie");
        }


        seller.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, sellers));
        producer.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, producers));
        nameFeed.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, nameFeeds));
        batch.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, batches));
        count.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, counts));
        packageType.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, packagetypes));


        submitBtnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String purchaseDateString = purchaseDate.getText().toString();
                String sellerString = seller.getText().toString();
                String producerString = producer.getText().toString();
                String nameFeedString = nameFeed.getText().toString();
                String batchString = batch.getText().toString();
                String countString = count.getText().toString();
                String packageTypeString = packageType.getText().toString();


                if (TextUtils.isEmpty(sellerString)) {
                    seller.setError("Proszę wprowadzić sprzedawcę");
                    return;
                }
                if (TextUtils.isEmpty(producerString)) {
                    producer.setError("Proszę wprowadzić producenta");
                    return;
                }
                if (TextUtils.isEmpty(nameFeedString)) {
                    nameFeed.setError("Proszę wprowadzić nazwę paszy");
                    return;
                }
                if (TextUtils.isEmpty(batchString)) {
                    batch.setError("Proszę wprowadzić numer partii");
                    return;
                }
                if (TextUtils.isEmpty(countString)) {
                    count.setError("Proszę wprowadzić ilość paszy");
                    return;
                }
                if (TextUtils.isEmpty(packageTypeString)) {
                    packageType.setError("Proszę wprowadzić rodzaj opakowania");
                    return;
                }


                if (edit) {
                    FeedModel model = new FeedModel(feedModel.getId(), purchaseDateString, sellerString, producerString, nameFeedString, batchString, countString, packageTypeString);
                    feedViewModel.feedEdit(model);
                } else {
                    FeedModel model = new FeedModel(purchaseDateString, sellerString, producerString, nameFeedString, batchString, countString, packageTypeString);
                    feedViewModel.feedAdd(model);
                }
                dismiss();

            }
        });

        return builder.create();
    }
}