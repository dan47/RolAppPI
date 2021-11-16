package com.example.rolapppi.ui.feedProduced;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolapppi.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FpAdapter extends  RecyclerView.Adapter<FpAdapter.MyHolder> implements Filterable {
    List<FeedProducedModel> feedProducedModelList;
    List<FeedProducedModel> feedProducedModelListAll;
    private FpAdapter.OnModelListener mOnModelListener;
    private final static int FADE_DURATION = 1000;

    public void setFeedProducedModelData(List<FeedProducedModel> feedModels) {
        this.feedProducedModelList = new ArrayList<>();
        this.feedProducedModelListAll = new ArrayList<>();
        this.feedProducedModelList.addAll(feedModels);
        this.feedProducedModelListAll.addAll(feedModels);
    }

    public FpAdapter(FpAdapter.OnModelListener onModelListener) {
        this.mOnModelListener = onModelListener;
    }

    @NonNull
    @Override
    public FpAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_feed_produced, parent, false);
        return new FpAdapter.MyHolder(view, mOnModelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FpAdapter.MyHolder holder, int position) {


        holder.acquisition.setText(feedProducedModelList.get(position).getAcquisition());
        holder.nameFeed.setText(feedProducedModelList.get(position).getNameFeed());
        holder.count.setText(feedProducedModelList.get(position).getCount());
//        setFadeAnimation(holder.itemView);
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

        if (feedProducedModelList == null) {

            return 0;
        } else {

            return feedProducedModelList.size();
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

            List<FeedProducedModel> filteredList = new ArrayList<>();
            if (charSequence.toString().equals("Wszystko")) {
                filteredList.addAll(feedProducedModelListAll);
            } else {
                filteredList = feedProducedModelListAll.stream().filter(e->e.getAcquisition().substring(6,10).equals(charSequence)).collect(Collectors.toList());
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
                feedProducedModelList.clear();
                feedProducedModelList.addAll((Collection<? extends FeedProducedModel>) filterResults.values);
            }

            notifyDataSetChanged();

        }
    };


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView acquisition, nameFeed, count;
        RelativeLayout relativeLayoutCard;
        FpAdapter.OnModelListener onModelListener;

        public MyHolder(@NonNull View itemView, FpAdapter.OnModelListener onModelListener) {
            super(itemView);

            acquisition = itemView.findViewById(R.id.acquisitionDate);
            nameFeed = itemView.findViewById(R.id.nameFeed);
            count = itemView.findViewById(R.id.count);
            relativeLayoutCard = itemView.findViewById(R.id.relativeLayoutCard);


            this.onModelListener = onModelListener;
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
