package com.example.tourdew.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourdew.GuideDetails;
import com.example.tourdew.R;
import com.example.tourdew.TripActivity;
import com.example.tourdew.model.Guide;
import com.example.tourdew.model.GuiderModel;
import com.example.tourdew.model.UserDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GuiderAdapter extends RecyclerView.Adapter<GuiderAdapter.GuiderViewHolder> {
    Context context;
    List<Guide> DataList;

    public GuiderAdapter(Context context, List<Guide> DataList) {
        this.context = context;
        this.DataList = DataList;
    }

    @NonNull
    @Override
    public GuiderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.guidershow,parent,false);
        return new GuiderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuiderViewHolder holder, int position) {
        holder.places.setText(DataList.get(position).getGuideFor());
        holder.price.setText(DataList.get(position).getPrice()+"");
        holder.ratings.setRating(DataList.get(position).getRating());
        holder.ratingScore.setText(DataList.get(position).getRating()+"");

        FirebaseDatabase.getInstance().getReference().child("User Data").child(DataList.get(position).getGuideId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserDetails userDetails=snapshot.getValue(UserDetails.class);
                    holder.personName.setText(userDetails.getName());
                    Picasso.get().load(userDetails.getProfilePic()).placeholder(R.drawable.male_icon).into(holder.personImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(holder.itemView.getContext(), GuideDetails.class);
                intent.putExtra("GuideId",DataList.get(position).getGuideId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public class GuiderViewHolder extends RecyclerView.ViewHolder {
        ImageView personImage;
        TextView personName,places,price,ratingScore;
        RatingBar ratings;
        public GuiderViewHolder(@NonNull final View itemView) {
            super(itemView);
            personImage=itemView.findViewById(R.id.person_img);
            places=itemView.findViewById(R.id.places);
            personName=itemView.findViewById(R.id.person_name);
            price=itemView.findViewById(R.id.price);
            ratings=itemView.findViewById(R.id.ratingbar);
            ratingScore=itemView.findViewById(R.id.rating_score);
        }
    }
}
