package com.example.tourdew.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourdew.R;
import com.example.tourdew.TripActivity;
import com.example.tourdew.model.AgentData;
import com.example.tourdew.model.PostTripData;
import com.example.tourdew.model.UserDetails;
import com.example.tourdew.model.homemodel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class homeadapter extends RecyclerView.Adapter<homeadapter.HomeViewHolder> {
    Context context;
    List<PostTripData> recentdatalist;

    public homeadapter(Context context, List<PostTripData> recentdatalist) {
        this.context = context;
        this.recentdatalist = recentdatalist;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recents_row,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        holder.placename.setText(recentdatalist.get(position).getPlaceName());
        holder.price.setText(recentdatalist.get(position).getPrice());
        Picasso.get().load(recentdatalist.get(position).getImageUrl()).placeholder(R.drawable.index).into(holder.placeimage);
        holder.postId=recentdatalist.get(position).getPostId();
        holder.postedBy=recentdatalist.get(position).getPostedBy();
        FirebaseDatabase.getInstance().getReference().child("Agents").child(recentdatalist.get(position).getPostedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AgentData user=snapshot.getValue(AgentData.class);
                        holder.agencyname.setText(user.getAgentName());
                        holder.rating.setText(user.getRating());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return recentdatalist.size();
    }

    public static final class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView placeimage;
        TextView placename,agencyname,price,rating;
        String postId,postedBy;
        public HomeViewHolder(@NonNull final View itemView) {
            super(itemView);
            placeimage=itemView.findViewById(R.id.place_image);
            placename=itemView.findViewById(R.id.placename);
            price=itemView.findViewById(R.id.price);
            agencyname=itemView.findViewById(R.id.agencyname);
            rating=itemView.findViewById(R.id.rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(itemView.getContext(), TripActivity.class);
                    intent.putExtra("postId",postId);
                    intent.putExtra("postedBy",postedBy);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
