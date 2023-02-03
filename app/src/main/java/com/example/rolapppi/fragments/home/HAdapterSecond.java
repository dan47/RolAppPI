package com.example.rolapppi.fragments.home;

import static java.time.temporal.ChronoUnit.DAYS;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolapppi.R;
import com.example.rolapppi.fragments.cattle.CattleModel;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HAdapterSecond extends RecyclerView.Adapter<HAdapterSecond.MyHolder> {

    List<CattleModel> cattleModelList;
    private OnModelListener mOnModelListener;

    public void setCattleModelData(List<CattleModel> cattleModelData) {
        this.cattleModelList = new ArrayList<>();
        this.cattleModelList.addAll(cattleModelData);
    }
    public HAdapterSecond(OnModelListener onModelListener) {
        this.mOnModelListener = onModelListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_cattle_dryness, parent, false);
        return new MyHolder(view, mOnModelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.animal_id.setText(cattleModelList.get(position).getAnimal_id());
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView animal_id;
        RelativeLayout relativeLayoutCard;
        OnModelListener onModelListener;

        public MyHolder(@NonNull View itemView, OnModelListener onModelListener) {
            super(itemView);

            animal_id = itemView.findViewById(R.id.animal_id);
            relativeLayoutCard = itemView.findViewById(R.id.relativeLayoutCard);

            this.onModelListener = onModelListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onModelListener.onModelClickSecond(getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {

        if (cattleModelList == null) {

            return 0;
        } else {

            return cattleModelList.size();
        }

    }

    public interface OnModelListener {
        void onModelClickSecond(int position);
    }
}