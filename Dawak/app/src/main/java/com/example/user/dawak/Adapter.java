package com.example.user.dawak;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {
    private Context mContext;
    private List<Pill> mPillList;

    public Adapter(List<Pill> pillList,
                   Context context) {
        this.mPillList = pillList;
        mContext = context;
    }//end constructor

    public void setPillList(List<Pill> pillList, Context context) {
        this.mPillList = pillList;
        this.mContext = mContext;
    }// end set

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pill_card,viewGroup,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.time.setText(mPillList.get(position).getTimeOfTaken());
        holder.pillName.setText(mPillList.get(position).getPillName());
        holder.takenDay.setText( mPillList.get(position).getTakenDay());

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.pill)
                .into(holder.pillIcon);
    }

    @Override
    public int getItemCount() {
        return (mPillList != null) ? mPillList.size():0;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        private TextView time;
        private TextView pillName;
        private TextView takenDay;
        private ImageView pillIcon;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            pillName= itemView.findViewById(R.id.pill_name);
            takenDay = itemView.findViewById(R.id.taken_day);
            pillIcon = itemView.findViewById(R.id.card_image);




        }// end constructor

    }// end class myViewHolder

}// end Adapter
