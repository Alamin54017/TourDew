package com.example.tourdew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tourdew.adapter.SearchAdapter;
import com.example.tourdew.adapter.homeadapter;
import com.example.tourdew.model.PostTripData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    RecyclerView searchRecycler;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchRecycler=findViewById(R.id.searchRecycle);
        database=FirebaseDatabase.getInstance();
        Intent intent= getIntent();
        String placeName= intent.getStringExtra("placeName");
        String date= intent.getStringExtra("Date");

        List<PostTripData> SearchDataList= new ArrayList<>();

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        searchRecycler.setLayoutManager(layoutManager);
        SearchAdapter searchAdapter =new SearchAdapter(this,SearchDataList);
        searchRecycler.setAdapter(searchAdapter);
        database.getReference().child("Trips Post").child(placeName).orderByChild("startDate").equalTo(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        PostTripData postTripData=dataSnapshot.getValue(PostTripData.class);
                        postTripData.setPostId(dataSnapshot.getKey());
                        SearchDataList.add(postTripData);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(Search.this,"No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}