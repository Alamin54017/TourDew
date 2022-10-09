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
import com.example.tourdew.model.FeedModel;
import com.example.tourdew.model.UserDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.util.Date;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    Context context;
    List<FeedModel> DataList;

    public FeedAdapter(Context context, List<FeedModel> dataList) {
        this.context = context;
        DataList = dataList;
    }

    @NonNull
    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.feedview,parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.FeedViewHolder holder, int position) {
        holder.Status.setText(DataList.get(position).getStatus());
        holder.Likes.setText(DataList.get(position).getLikesCount()+"");
        holder.Comments.setText(DataList.get(position).getCommentCount()+"");
        Picasso.get().load(DataList.get(position).getPostImg()).placeholder(R.drawable.index).into(holder.postImage);

        long diff = new Date().getTime();
        diff=diff- DataList.get(position).getPostedAt();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + " day ago ";
            } else {
                time = diffDays + " days ago ";
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + " hr ago";
                } else {
                    time = diffHours + " hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + " min ago";
                    } else {
                        time = diffMinutes + " mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + " secs ago";
                    }
                }

            }

        }
        holder.postDate.setText(time);

        FirebaseDatabase.getInstance().getReference().child("User Data").child(DataList.get(position).getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails=snapshot.getValue(UserDetails.class);
                holder.pName.setText(userDetails.getName());
                Picasso.get().load(userDetails.getProfilePic()).placeholder(R.drawable.ic_male_user_96).into(holder.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage,postImage;
        TextView pName,postDate,Status,Likes,Comments;
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            pName=itemView.findViewById(R.id.pName);
            postDate=itemView.findViewById(R.id.postDate);
            Status=itemView.findViewById(R.id.Status);
            Likes=itemView.findViewById(R.id.Likes);
            profileImage=itemView.findViewById(R.id.profileImage);
            postImage=itemView.findViewById(R.id.postImage);
            Comments=itemView.findViewById(R.id.comments);
        }
    }
}
