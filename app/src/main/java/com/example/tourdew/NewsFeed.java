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
import com.example.tourdew.adapter.FeedAdapter;
import com.example.tourdew.model.AgentModel1;
import com.example.tourdew.model.FeedModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsFeed extends AppCompatActivity {
    RecyclerView recentRecycler;
    FeedAdapter FeedAdapter;
    ImageView profile;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth fAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        profile=findViewById(R.id.profile);
        database.getReference().child("User Data").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image=snapshot.child("imageUrl").getValue(String.class);
                Picasso.get().load(image).placeholder(R.drawable.ic_male_user_96).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewsFeed.this,AddFeedPost.class);
                startActivity(intent);
            }
        });

        List<FeedModel> DataList=new ArrayList<>();



        recentRecycler= findViewById(R.id.posts);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recentRecycler.setLayoutManager(layoutManager);
        FeedAdapter=new FeedAdapter(this,DataList);
        recentRecycler.setAdapter(FeedAdapter);
        database.getReference().child("Feed Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    FeedModel feedModel=dataSnapshot.getValue(FeedModel.class);
                    DataList.add(feedModel);
                }
                FeedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}