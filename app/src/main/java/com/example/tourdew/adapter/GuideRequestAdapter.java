package com.example.tourdew.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourdew.GuideAccepted;
import com.example.tourdew.GuiderMenu;
import com.example.tourdew.R;
import com.example.tourdew.model.Guide;
import com.example.tourdew.model.GuideRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GuideRequestAdapter extends RecyclerView.Adapter<GuideRequestAdapter.ViewHolder> {
    Context context;
    List<GuideRequest> DataList;

    public GuideRequestAdapter(Context context, List<GuideRequest> dataList) {
        this.context = context;
        DataList = dataList;
    }

    @NonNull
    @Override
    public GuideRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.guiderequest,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuideRequestAdapter.ViewHolder holder, int position) {
        holder.date.setText(DataList.get(position).getDate());
        holder.place.setText(DataList.get(position).getPlace());
        holder.price.setText(DataList.get(position).getPrice()+"");
        FirebaseDatabase.getInstance().getReference().child("User Data").child(DataList.get(position).getSenderId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue(String.class);
                holder.SenderName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuideRequest guideRequest=new GuideRequest();
                guideRequest.setPrice(DataList.get(position).getPrice());
                guideRequest.setPlace(DataList.get(position).getPlace());
                guideRequest.setSenderId(DataList.get(position).getSenderId());
                guideRequest.setDate(DataList.get(position).getDate());
                guideRequest.setGuideId(DataList.get(position).getGuideId());
                guideRequest.setId(DataList.get(position).getId());
                FirebaseDatabase.getInstance().getReference().child("Guide Booked").child(DataList.get(position).getId()).setValue(guideRequest);
                FirebaseDatabase.getInstance().getReference().child("Guider Request").child(DataList.get(position).getId()).removeValue();
                Intent intent=new Intent(holder.itemView.getContext(), GuideAccepted.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Guider Request").child(DataList.get(position).getId()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView SenderName,date,place,price;
        Button accept,cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            SenderName=itemView.findViewById(R.id.textView97);
            date=itemView.findViewById(R.id.textView102);
            place=itemView.findViewById(R.id.textView103);
            price=itemView.findViewById(R.id.textView104);
            accept=itemView.findViewById(R.id.button2);
            cancel=itemView.findViewById(R.id.button3);
        }
    }
}
