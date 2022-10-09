package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.tourdew.adapter.AgentAdapter1;
import com.example.tourdew.adapter.GuideRequestAdapter;
import com.example.tourdew.model.GuideRequest;
import com.example.tourdew.model.PostTripData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GuiderMenu extends AppCompatActivity {
    ImageView settings;
    RecyclerView guideRecycler;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    ImageView mytask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider_menu);

        settings=findViewById(R.id.settings);
        mytask=findViewById(R.id.imageView30);
        mytask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GuiderMenu.this,GuideAccepted.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GuiderMenu.this,GuiderProfile.class);
                startActivity(intent);
            }
        });



        List<GuideRequest> DataList=new ArrayList<>();

        guideRecycler= findViewById(R.id.guideRecycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        guideRecycler.setLayoutManager(layoutManager);
        GuideRequestAdapter guideRequestAdapter =new GuideRequestAdapter(this,DataList);
        guideRecycler.setAdapter(guideRequestAdapter);
        database.getReference().child("Guider Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if (dataSnapshot.child("guideId").getValue().equals(fAuth.getCurrentUser().getUid())){
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