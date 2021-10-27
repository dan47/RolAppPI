package com.example.rolapppi.ui.feed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rolapppi.R;
import com.example.rolapppi.ui.cropProtection.AddCropProtectionDialog;
import com.example.rolapppi.ui.cropProtection.CropProtectionViewModel;

public class DetailsFeedFragment extends Fragment {
    TextView purchaseDate, seller, producer, nameFeed, batch, count, packageType, remarks;
    Button editBtn, deleteBtn;


    public DetailsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_feed, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FeedViewModel feedViewModel = new ViewModelProvider(requireActivity()).get(FeedViewModel.class);
        purchaseDate = view.findViewById(R.id.purchaseDate);
        seller = view.findViewById(R.id.seller);
        producer = view.findViewById(R.id.producer);
        nameFeed = view.findViewById(R.id.nameFeed);
        batch = view.findViewById(R.id.batch);
        count = view.findViewById(R.id.count);
        packageType = view.findViewById(R.id.packageType);
        remarks = view.findViewById(R.id.remarks);

        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);

        feedViewModel.getSelected().observe(getViewLifecycleOwner(), feedModel -> {
            purchaseDate.setText(feedModel.getPurchaseDate());
            seller.setText(feedModel.getSeller());
            producer.setText(feedModel.getProducer());
            nameFeed.setText(feedModel.getNameFeed());
            batch.setText(feedModel.getBatch());
            count.setText(feedModel.getCount());
            packageType.setText(feedModel.getPackageType());
            remarks.setText(feedModel.getRemarks());
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFeedDialog exampleDialog = new AddFeedDialog();
                exampleDialog.show(getParentFragmentManager(), "example dialog");
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedViewModel.feedDelete(feedViewModel.getSelected().getValue());
                getActivity().onBackPressed();
            }
        });
    }

}