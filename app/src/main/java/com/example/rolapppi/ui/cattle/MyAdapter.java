package com.example.rolapppi.ui.cattle;

import static java.time.temporal.ChronoUnit.DAYS;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> implements Filterable {

    List<CattleModel> cattleModelList;
    List<CattleModel> cattleModelListAll;
    private OnModelListener mOnModelListener;

    public void setCattleModelData(List<CattleModel> cattleModelData) {
        this.cattleModelList = new ArrayList<>();
        this.cattleModelListAll = new ArrayList<>();
        this.cattleModelList.addAll(cattleModelData);
        this.cattleModelListAll.addAll(cattleModelData);
    }

    public MyAdapter(OnModelListener onModelListener) {
        this.mOnModelListener = onModelListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyHolder(view, mOnModelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        String gender = cattleModelList.get(position).getGender();
        holder.animal_id.setText(cattleModelList.get(position).getAnimal_id());
        holder.birthday.setText(cattleModelList.get(position).getBirthday());
        holder.gender.setText(gender);


        if (gender.equals("Samica")) {

            String calving = cattleModelList.get(position).getCaliving();
            if (!calving.isEmpty()) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate localDate = java.time.LocalDate.parse(calving, formatter);
                long duration = DAYS.between(localDate, LocalDate.now());
                if (duration > 250) {
                    holder.relativeLayoutCard.setBackgroundColor(Color.parseColor("#FF7F7F"));
                } else if (duration > 140) {
                    holder.relativeLayoutCard.setBackgroundColor(Color.parseColor("#FFFF99"));
                } else {
                    holder.relativeLayoutCard.setBackgroundColor(Color.parseColor("#90EE90"));
                }
            } else {
                holder.relativeLayoutCard.setBackgroundColor(Color.WHITE);
            }
        } else {
            holder.relativeLayoutCard.setBackgroundColor(Color.WHITE);
        }


//        Glide.with(holder.itemView.getContext()).load(cattleModelList.get(position).getImage())
//                .placeholder(R.drawable.placeholder_image).centerCrop().into(holder.imageView);
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
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(cattleModelListAll);
            } else {
                for (CattleModel item : cattleModelListAll) {
                    if (item.getAnimal_id().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(item);
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

    //    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView animal_id, birthday, gender;
        RelativeLayout relativeLayoutCard;
        Button button;
        OnModelListener onModelListener;

        public MyHolder(@NonNull View itemView, OnModelListener onModelListener) {
            super(itemView);

            animal_id = itemView.findViewById(R.id.animal_id);
            birthday = itemView.findViewById(R.id.birthday);
            gender = itemView.findViewById(R.id.gender);
            relativeLayoutCard = itemView.findViewById(R.id.relativeLayoutCard);

            button = itemView.findViewById(R.id.button);
            this.onModelListener = onModelListener;
//            button.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            onModelListener.onModelClick(getAdapterPosition());
        }
    }

    public interface OnModelListener {
        void onModelClick(int position);
    }
}