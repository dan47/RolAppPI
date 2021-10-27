package com.example.rolapppi.ui.feed;

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

public class FAdapter extends  RecyclerView.Adapter<FAdapter.MyHolder> implements Filterable {
    List<FeedModel> feedModelList;
    List<FeedModel> feedModelListAll;
    private FAdapter.OnModelListener mOnModelListener;
    private final static int FADE_DURATION = 1000;

    public void setFeedModelData(List<FeedModel> feedModels) {
        this.feedModelList = new ArrayList<>();
        this.feedModelListAll = new ArrayList<>();
        this.feedModelList.addAll(feedModels);
        this.feedModelListAll.addAll(feedModels);
    }

    public FAdapter(FAdapter.OnModelListener onModelListener) {
        this.mOnModelListener = onModelListener;
    }

    @NonNull
    @Override
    public FAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_items, parent, false);
        return new FAdapter.MyHolder(view, mOnModelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FAdapter.MyHolder holder, int position) {


        holder.purchaseDate.setText(feedModelList.get(position).getPurchaseDate());
        holder.nameFeed.setText(feedModelList.get(position).getNameFeed());
        holder.count.setText(feedModelList.get(position).getCount());
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

        if (feedModelList == null) {

            return 0;
        } else {

            return feedModelList.size();
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

            List<FeedModel> filteredList = new ArrayList<>();
            if (charSequence.toString().equals("Wszystko")) {
                filteredList.addAll(feedModelListAll);
            } else {
                filteredList = feedModelListAll.stream().filter(e->e.getPurchaseDate().substring(6,10).equals(charSequence)).collect(Collectors.toList());
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
                feedModelList.clear();
                feedModelList.addAll((Collection<? extends FeedModel>) filterResults.values);
            }

            notifyDataSetChanged();

        }
    };


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView purchaseDate, nameFeed, count;
        RelativeLayout relativeLayoutCard;
        FAdapter.OnModelListener onModelListener;

        public MyHolder(@NonNull View itemView, FAdapter.OnModelListener onModelListener) {
            super(itemView);

            purchaseDate = itemView.findViewById(R.id.purchaseDate);
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
