package com.example.rolapppi.fragments.feedProduced;

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

public class DetailsFeedProducedFragment extends Fragment {

    TextView acquisition, nameFeed, origin, count, weight, destination, cattleType;
    Button editBtn, deleteBtn;

    public DetailsFeedProducedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_feed_produced, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FeedProducedViewModel feedProducedViewModel = new ViewModelProvider(requireActivity()).get(FeedProducedViewModel.class);
        acquisition = view.findViewById(R.id.acquisitionDate);
        nameFeed = view.findViewById(R.id.nameFeed);
        origin = view.findViewById(R.id.origin);
        count = view.findViewById(R.id.count);
        destination = view.findViewById(R.id.destination);
        cattleType = view.findViewById(R.id.cattleType);

        editBtn = view.findViewById(R.id.editBtn);
        deleteBtn = view.findViewById(R.id.deleteBtn);

        feedProducedViewModel.getSelected().observe(getViewLifecycleOwner(), feedProducedModel -> {
            acquisition.setText(feedProducedModel.getAcquisition());
            nameFeed.setText(feedProducedModel.getNameFeed());
            origin.setText(feedProducedModel.getOrigin());
            count.setText(feedProducedModel.getCount());
            destination.setText(feedProducedModel.getDestination());
            cattleType.setText(feedProducedModel.getCattleType());

        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFeedProducedDialog exampleDialog = new AddFeedProducedDialog();
                exampleDialog.show(getParentFragmentManager(), "example dialog");
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedProducedViewModel.feedProducedDelete(feedProducedViewModel.getSelected().getValue());
                getActivity().onBackPressed();
            }
        });
    }
}