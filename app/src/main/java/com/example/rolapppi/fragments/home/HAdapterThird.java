package com.example.rolapppi.fragments.home;

import static java.time.temporal.ChronoUnit.DAYS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolapppi.R;
import com.example.rolapppi.fragments.cattle.CattleModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HAdapterThird extends RecyclerView.Adapter<HAdapterThird.MyHolder> {

    List<CattleModel> cattleModelList;
    private HAdapterThird.OnModelListener mOnModelListener;

    public void setCattleModelData(List<CattleModel> cattleModelData) {
        this.cattleModelList = new ArrayList<>();
        this.cattleModelList.addAll(cattleModelData);
    }

    public HAdapterThird(HAdapterThird.OnModelListener onModelListener) {
        this.mOnModelListener = onModelListener;
    }

    @NonNull
    @Override
    public HAdapterThird.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_cattle_dryness, parent, false);
        return new HAdapterThird.MyHolder(view, mOnModelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HAdapterThird.MyHolder holder, int position) {

        holder.animal_id.setText(cattleModelList.get(position).getAnimal_id());


        long duration = cattleModelList.get(position).getDurationCalving();
        int result = (int) (duration);

        holder.time_dryness.setText(Integer.toString(result));
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView animal_id, time_dryness;
        RelativeLayout relativeLayoutCard;
        HAdapterThird.OnModelListener onModelListener;

        public MyHolder(@NonNull View itemView, HAdapterThird.OnModelListener onModelListener) {
            super(itemView);

            animal_id = itemView.findViewById(R.id.animal_id);
            time_dryness = itemView.findViewById(R.id.time_dryness);
            relativeLayoutCard = itemView.findViewById(R.id.relativeLayoutCard);

            this.onModelListener = onModelListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onModelListener.onModelClickThird(getAdapterPosition());
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


    private long duration(String calving) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = java.time.LocalDate.parse(calving, formatter);
        long duration = DAYS.between(localDate, LocalDate.now());
        return duration;
    }

    public interface OnModelListener {
        void onModelClickThird(int position);
    }
}