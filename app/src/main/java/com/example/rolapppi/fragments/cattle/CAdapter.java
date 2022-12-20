package com.example.rolapppi.fragments.cattle;

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


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CAdapter extends RecyclerView.Adapter<CAdapter.MyHolder> implements Filterable {

    List<CattleModel> cattleModelList;
    List<CattleModel> cattleModelListAll;
    private OnModelListener mOnModelListener;

    public void setCattleModelData(List<CattleModel> cattleModelData) {
        this.cattleModelList = new ArrayList<>();
        this.cattleModelListAll = new ArrayList<>();
        this.cattleModelList.addAll(cattleModelData);
        this.cattleModelListAll.addAll(cattleModelData);
    }

    public CAdapter(OnModelListener onModelListener) {
        this.mOnModelListener = onModelListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_cattle, parent, false);
        return new MyHolder(view, mOnModelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String gender = cattleModelList.get(position).getGender();
        holder.animal_id.setText(cattleModelList.get(position).getAnimal_id());
        holder.birthday.setText(cattleModelList.get(position).getBirthday());
        holder.gender.setText(gender);
        String calving = cattleModelList.get(position).getCaliving();
        String previousCalving = cattleModelList.get(position).getPreviousCaliving();


        if (gender.equals("Samica")) {
            if (!calving.isEmpty()) {
                holder.calving.setVisibility(View.VISIBLE);
                holder.birthday.setVisibility(View.GONE);
                holder.calving.setText(calving);
                holder.textBC.setText("Za.");
                long duration = duration(calving);
                if (duration > 235) {
                    holder.relativeLayoutCard.setBackgroundColor(Color.parseColor("#FF7F7F"));
                } else if (duration > 140) {
                    holder.relativeLayoutCard.setBackgroundColor(Color.parseColor("#FFFF99"));
                } else {
                    holder.relativeLayoutCard.setBackgroundColor(Color.parseColor("#90EE90"));
                }
            } else if (!previousCalving.isEmpty()) {
                long duration = duration(previousCalving);
                if (duration < 90) {
                    holder.relativeLayoutCard.setBackgroundColor(Color.parseColor("#EE90DE"));
                }
            } else {
                holder.relativeLayoutCard.setBackgroundColor(Color.WHITE);
            }
        } else {

            holder.relativeLayoutCard.setBackgroundColor(Color.WHITE);
        } //change color card
        if (calving.isEmpty()) {
            holder.calving.setVisibility(View.GONE);
            holder.birthday.setVisibility(View.VISIBLE);
            holder.textBC.setText("Ur.");
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView animal_id, birthday, gender, calving, textBC;
        RelativeLayout relativeLayoutCard;
        OnModelListener onModelListener;

        public MyHolder(@NonNull View itemView, OnModelListener onModelListener) {
            super(itemView);

            animal_id = itemView.findViewById(R.id.animal_id);
            birthday = itemView.findViewById(R.id.birthday);
            gender = itemView.findViewById(R.id.gender);
            calving = itemView.findViewById(R.id.calving);
            textBC = itemView.findViewById(R.id.textBirthCalving);
            relativeLayoutCard = itemView.findViewById(R.id.relativeLayoutCard);

            this.onModelListener = onModelListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onModelListener.onModelClick(getAdapterPosition());
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<CattleModel> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty() || charSequence.equals("Brak")) {
                filteredList.addAll(cattleModelListAll);
            } else {
                if (charSequence.equals("Samiec") || charSequence.equals("Samica")) {
                    for (CattleModel item : cattleModelListAll) {
                        if (item.getGender().equals(charSequence.toString())) {
                            filteredList.add(item);
                        }
                    }
                } else if (charSequence.equals("Zacielenie")) {
                    for (CattleModel item : cattleModelListAll) {
                        if (!item.getCaliving().isEmpty()) {
                            filteredList.add(item);
                            filteredList.sort(Comparator.comparingLong(a -> duration(a.getCaliving())));
                            Collections.reverse(filteredList);
                        }
                    }
                } else if (charSequence.equals("Zasuszenie")) {
                    for (CattleModel item : cattleModelListAll) {
                        if (!item.getCaliving().isEmpty()) {
                            if (duration(item.getCaliving()) > 235) {
                                filteredList.add(item);
                                filteredList.sort(Comparator.comparingLong(a -> duration(a.getCaliving())));
                                Collections.reverse(filteredList);
                            }
                        }
                    }
                } else if (charSequence.equals("Karmienie")) {
                    for (CattleModel item : cattleModelListAll) {
                        if (!item.getPreviousCaliving().isEmpty()) {
                            if (duration(item.getPreviousCaliving()) < 90) {
                                if (item.getCaliving().isEmpty()) {
                                    filteredList.add(item);
                                    filteredList.sort(Comparator.comparingLong(a -> duration(a.getPreviousCaliving())));
                                    Collections.reverse(filteredList);
                                }
                            }
                        }
                    }
                } else {
                    for (CattleModel item : cattleModelListAll) {
                        if (item.getAnimal_id().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            filterResults.count = filteredList.size();
            return filterResults;
        }

        //run on a ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            if (filterResults.values != null) {
                cattleModelList.clear();
                cattleModelList.addAll((Collection<? extends CattleModel>) filterResults.values);
            }

            notifyDataSetChanged();

        }
    };

    private long duration(String calving) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = java.time.LocalDate.parse(calving, formatter);
        long duration = DAYS.between(localDate, LocalDate.now());
        return duration;
    }

    public interface OnModelListener {
        void onModelClick(int position);
    }
}