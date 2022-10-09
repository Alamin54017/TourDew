package com.example.tourdew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourdew.R;
import com.example.tourdew.model.AgentModel1;
import com.example.tourdew.model.PostTripData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AgentAdapter1 extends RecyclerView.Adapter<AgentAdapter1.Agent1ViewHolder> {
    Context context;
    List<PostTripData> DataList;

    public AgentAdapter1(Context context, List<PostTripData> DataList) {
        this.context = context;
        this.DataList = DataList;
    }

    @NonNull
    @Override
    public AgentAdapter1.Agent1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.agent_trip1,parent,false);
        return new Agent1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentAdapter1.Agent1ViewHolder holder, int position) {

        holder.placeName.setText(DataList.get(position).getPlaceName());
        holder.StartDate.setText(DataList.get(position).getStartDate());
        holder.BookSeats.setText(DataList.get(position).getBookingCount()+"");
        Picasso.get().load(DataList.get(position).getImageUrl()).placeholder(R.drawable.index).into(holder.placeImage);
        holder.Payment.setText(DataList.get(position).getPayment()+"");
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public class Agent1ViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImage;
        TextView placeName,StartDate,BookSeats,Payment;
        public Agent1ViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage=itemView.findViewById(R.id.placeImage);
            placeName=itemView.findViewById(R.id.placeName);
            StartDate=itemView.findViewById(R.id.StartDate);
            BookSeats=itemView.findViewById(R.id.BookSeats);
            Payment=itemView.findViewById(R.id.Payment);
        }
    }
}
