package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tourdew.adapter.GuideAcceptAdapter;
import com.example.tourdew.adapter.GuiderBookedAdapter;
import com.example.tourdew.model.GuideRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GuideBooked extends AppCompatActivity {
    RecyclerView guideRecycler;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_booked);

        List<GuideRequest> DataList=new ArrayList<>();

        guideRecycler= findViewById(R.id.guide);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        guideRecycler.setLayoutManager(layoutManager);
        GuiderBookedAdapter guideRequestAdapter =new GuiderBookedAdapter(this,DataList);
        guideRecycler.setAdapter(guideRequestAdapter);
        database.getReference().child("Guide Booked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if (dataSnapshot.child("senderId").getValue().equals(fAuth.getCurrentUser().getUid())){
                        GuideRequest guideRequest=dataSnapshot.getValue(GuideRequest.class);
                        DataList.add(guideRequest);
                    }
                    guideRequestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}