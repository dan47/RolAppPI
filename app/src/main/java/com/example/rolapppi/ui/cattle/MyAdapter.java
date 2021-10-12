package com.example.rolapppi.ui.cattle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolapppi.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    List<CattleModel> cattleModelList;

    OnItemClicked itemClicked;

    public void setCattleModelData(List<CattleModel> cattleModelData) {
        this.cattleModelList = cattleModelData;
    }

    public MyAdapter(OnItemClicked itemClicked) {

        this.itemClicked = itemClicked;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        holder.animal_id.setText(cattleModelList.get(position).getAnimal_id());
        holder.birthday.setText(cattleModelList.get(position).getBirthday());
        holder.gender.setText(cattleModelList.get(position).getGender());

//        Glide.with(holder.itemView.getContext()).load(cattleModelList.get(position).getImage())
//                .placeholder(R.drawable.placeholder_image).centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        if (cattleModelList == null) {

            return 0;
        } else {

            return  cattleModelList.size();
        }


    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        TextView animal_id, birthday, gender;
//        Button button;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            animal_id = itemView.findViewById(R.id.animal_id);
            birthday = itemView.findViewById(R.id.birthday);
            gender = itemView.findViewById(R.id.gender);
//            button = itemView.findViewById(R.id.takequizBtn);

//            button.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            itemClicked.somethingClicked(getAdapterPosition());
        }



    }

    public interface OnItemClicked {


        void somethingClicked(int position);

    }
}