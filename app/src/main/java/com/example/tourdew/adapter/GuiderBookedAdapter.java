package com.example.tourdew.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourdew.Message;
import com.example.tourdew.R;
import com.example.tourdew.model.GuideRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GuiderBookedAdapter extends RecyclerView.Adapter<GuiderBookedAdapter.ViewHolder> {
    Context context;
    List<GuideRequest> DataList;

    public GuiderBookedAdapter(Context context, List<GuideRequest> dataList) {
        this.context = context;
        DataList = dataList;
    }

    @NonNull
    @Override
    public GuiderBookedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.guidebooked2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuiderBookedAdapter.ViewHolder holder, int position) {
        holder.date.setText(DataList.get(position).getDate());
        holder.place.setText(DataList.get(position).getPlace());
        holder.price.setText(DataList.get(position).getPrice()+"");
        FirebaseDatabase.getInstance().getReference().child("User Data").child(DataList.get(position).getGuideId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue(String.class);
                String image=snapshot.child("imageUrl").getValue(String.class);
                holder.SenderName.setText(name);
                Picasso.get().load(image).placeholder(R.drawable.ic_male_user_96).into(holder.profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(), Message.class);
                intent.putExtra("userid",DataList.get(position).getGuideId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("User Data").child(DataList.get(position).getGuideId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phone=snapshot.child("phone").getValue(String.class);
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+phone));
                        holder.itemView.getContext().startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Guide Booked").child(DataList.get(position).getId()).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView SenderName,date,place,price;
        ImageButton chat,call;
        Button complete;
        ImageView profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.textView97);
            SenderName=itemView.findViewById(R.id.textView98);
            date=itemView.findViewById(R.id.textView102);
            place=itemView.findViewById(R.id.textView103);
            price=itemView.findViewById(R.id.textView104);
            call=itemView.findViewById(R.id.button3);
            chat=itemView.findViewById(R.id.button2);
            complete=itemView.findViewById(R.id.button4);
        }
    }
}
