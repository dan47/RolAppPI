package com.example.rolapppi.ui.cropProtection;

import static java.time.temporal.ChronoUnit.DAYS;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolapppi.R;
import com.example.rolapppi.ui.cattle.CAdapter;
import com.example.rolapppi.ui.cattle.CattleModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CpAdapter extends  RecyclerView.Adapter<CpAdapter.MyHolder> implements Filterable{

    List<CropProtectionModel> cropProtectionModelList;
    List<CropProtectionModel> cropProtectionModelListAll;
    private CpAdapter.OnModelListener mOnModelListener;
    private final static int FADE_DURATION = 1000;

    public void setCropProtectionModelData(List<CropProtectionModel> cropProtectionModels) {
        this.cropProtectionModelList = new ArrayList<>();
        this.cropProtectionModelListAll = new ArrayList<>();
        this.cropProtectionModelList.addAll(cropProtectionModels);
        this.cropProtectionModelListAll.addAll(cropProtectionModels);
    }

    public CpAdapter(CpAdapter.OnModelListener onModelListener) {
        this.mOnModelListener = onModelListener;
    }

    @NonNull
    @Override
    public CpAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_protection_list_items, parent, false);
        return new CpAdapter.MyHolder(view, mOnModelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CpAdapter.MyHolder holder, int position) {


        holder.cpId.setText(cropProtectionModelList.get(position).getTreatmentTime());
        holder.treatmentTime.setText(cropProtectionModelList.get(position).getCrop());
        holder.crop.setText(cropProtectionModelList.get(position).getReason());
        setFadeAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {

        if (cropProtectionModelList == null) {

            return 0;
        } else {

            return cropProtectionModelList.size();
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

            List<CropProtectionModel> filteredList = new ArrayList<>();
            if (charSequence.toString().equals("Wszystko")) {
                filteredList.addAll(cropProtectionModelListAll);
            } else {
                filteredList = cropProtectionModelListAll.stream().filter(e->e.getTreatmentTime().substring(12,16).equals(charSequence)).collect(Collectors.toList());
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
                cropProtectionModelList.clear();
                cropProtectionModelList.addAll((Collection<? extends CropProtectionModel>) filterResults.values);
            }

            notifyDataSetChanged();

        }
    };

    //    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView cpId, treatmentTime, crop;
        RelativeLayout relativeLayoutCard;
//        Button button;
        CpAdapter.OnModelListener onModelListener;

        public MyHolder(@NonNull View itemView, CpAdapter.OnModelListener onModelListener) {
            super(itemView);

            cpId = itemView.findViewById(R.id.cpId);
            treatmentTime = itemView.findViewById(R.id.treatmentTime);
            crop = itemView.findViewById(R.id.crop);
            relativeLayoutCard = itemView.findViewById(R.id.relativeLayoutCard);

//            button = itemView.findViewById(R.id.button);
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
