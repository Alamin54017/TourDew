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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    List<PostTripData> Searchdatalist;

    public SearchAdapter(Context context, List<PostTripData> searchdatalist) {
        this.context = context;
        Searchdatalist = searchdatalist;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.searchview,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.placename.setText(Searchdatalist.get(position).getPlaceName());
        holder.price.setText(Searchdatalist.get(position).getPrice());
        holder.date.setText(Searchdatalist.get(position).getStartDate());
        holder.postId=Searchdatalist.get(position).getPostId();
        holder.postedBy=Searchdatalist.get(position).getPostedBy();
        Picasso.get().load(Searchdatalist.get(position).getImageUrl()).placeholder(R.drawable.index).into(holder.placeimage);
        FirebaseDatabase.getInstance().getReference().child("Agents").child(Searchdatalist.get(position).getPostedBy())
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
        return Searchdatalist.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView placeimage;
        TextView placename,agencyname,price,rating,date;
        String postId,postedBy;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            placeimage=itemView.findViewById(R.id.placeImage);
            placename=itemView.findViewById(R.id.placeName);
            price=itemView.findViewById(R.id.Payment);
            date=itemView.findViewById(R.id.StartDate);
            agencyname=itemView.findViewById(R.id.textView53);
            rating=itemView.findViewById(R.id.textView55);
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
