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

import com.example.tourdew.BookedTripDetails;
import com.example.tourdew.R;
import com.example.tourdew.model.AgentData;
import com.example.tourdew.model.BookModel;
import com.example.tourdew.model.PostTripData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.BookedViewHolder>{
    Context context;
    List<BookModel> BookedList;

    public BookedAdapter(Context context, List<BookModel> bookedList) {
        this.context = context;
        BookedList = bookedList;
    }

    @NonNull
    @Override
    public BookedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bookedview,parent,false);
        return new BookedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedViewHolder holder, int position) {
        holder.seat.setText(BookedList.get(position).getSeatsTaken()+"");
        holder.amount.setText(BookedList.get(position).getPrice());
        holder.postId=BookedList.get(position).getPostId();
        FirebaseDatabase.getInstance().getReference().child("Trips Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    PostTripData postTripData= dataSnapshot.child(BookedList.get(position).getPostId()).getValue(PostTripData.class);
                    try{
                        holder.placeName.setText(postTripData.getPlaceName());
                        holder.endDate.setText(postTripData.getEndDate());
                        holder.postedBy=postTripData.getPostedBy();
                        Picasso.get().load(postTripData.getImageUrl()).placeholder(R.drawable.index).into(holder.placeImage);

                        FirebaseDatabase.getInstance().getReference().child("Agents").child(holder.postedBy).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                AgentData agentData=snapshot.getValue(AgentData.class);
                                try {
                                    holder.agencyName.setText(agentData.getAgentName());
                                    holder.rating.setText(agentData.getRating());
                                }
                                catch (Exception e){

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    catch (Exception e){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), BookedTripDetails.class);
                intent.putExtra("postedBy",holder.postedBy);
                intent.putExtra("postId",holder.postId);
                intent.putExtra("seat",holder.seat.getText().toString());
                intent.putExtra("amount",holder.amount.getText().toString());
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return BookedList.size();
    }

    public class BookedViewHolder extends RecyclerView.ViewHolder {
        TextView placeName,agencyName,rating,endDate,seat,amount;
        ImageView placeImage;
        String postedBy,postId;
        public BookedViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName=itemView.findViewById(R.id.placeName);
            agencyName=itemView.findViewById(R.id.textView53);
            rating=itemView.findViewById(R.id.rating);
            endDate=itemView.findViewById(R.id.endDate);
            seat=itemView.findViewById(R.id.seat);
            amount=itemView.findViewById(R.id.payment2);
            placeImage=itemView.findViewById(R.id.placeImage);

        }
    }
}
